package com.akane.tools.utils.http;

import com.akane.tools.config.HttpClientConfig;
import com.akane.tools.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * apache http client
 *
 * @author akane
 */
@Slf4j
public class HttpClient
{
    // 连接管理器
    private static PoolingHttpClientConnectionManager pool;
    // 请求配置
    private static RequestConfig requestConfig;

    static {
        try {
            log.info("HttpClient - 初始化HttpClient开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register(
                    "http", PlainConnectionSocketFactory.getSocketFactory()).register(
                    "https", sslsf).build();
            // 初始化连接管理器
            pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到200
            pool.setMaxTotal(Integer.parseInt(HttpClientConfig.getPoolMaxActive()));
            // 设置最大路由
            pool.setDefaultMaxPerRoute(Integer.parseInt(HttpClientConfig.getPoolMaxPerRoute()));
            // 根据默认超时限制初始化requestConfig
            int socketTimeout = Integer.parseInt(HttpClientConfig.getSocketTimeOut());
            int connectTimeout = Integer.parseInt(HttpClientConfig.getConnectTimeout());
            int connectionRequestTimeout = Integer.parseInt(HttpClientConfig.getConnectionRequestTimeout());
            // 设置请求超时时间
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setConnectTimeout(connectTimeout)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            log.error("HttpClient初始化 NoSuchAlgorithmException", e);
        } catch (KeyStoreException e) {
            log.error("HttpClient初始化 KeyStoreException", e);
        } catch (KeyManagementException e) {
            log.error("HttpClient初始化 KeyManagementException", e);
        } catch (Exception e) {
            log.error("HttpClient初始化 Exception", e);
        }
    }

    // 创建httpclient对象
    private static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(pool)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();

        return httpClient;
    }

    /**
     * 发送Get请求（utf-8）
     * @param url 发送请求的 URL
     * @param headers 附加请求头参数
     * @return
     */
    public static String sendGet(String url, Map<String, String> headers)
    {
        return sendGet(url, Constants.UTF8, headers);
    }

    /**
     * 发送Get请求
     * @param url 发送请求的 URL
     * @param charset 编码类型
     * @param headers 附加请求头参数
     * @return
     */
    public static String sendGet(String url, String charset, Map<String, String> headers) {
        CloseableHttpClient httpClient;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = getHttpClient();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息
            if(MapUtils.isNotEmpty(headers)) {
                for(String key : headers.keySet()){
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("调用HttpClient.sendGet Exception, url=" + url, e);
        } finally {
            // 关闭资源
            if (null != response) {
                log.info("调用HttpClient.sendGet url={}, response status={}",
                        url, response.getStatusLine().getStatusCode());
                if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    log.info("调用HttpClient.sendGet result={}", result);
                }
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("调用HttpClient.sendGet.response.close IOException, url=" + url, e);
                }
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求(form)
     * @param url  发送请求的 URL
     * @param paramMap 参数map
     * @param headers  附加请求头参数
     * @return
     */
    public static String sendFormPost(String url, Map<String, Object> paramMap, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        // 设置请求头信息
        if(MapUtils.isNotEmpty(headers)) {
            for(String key : headers.keySet()){
                httpPost.addHeader(key, headers.get(key));
            }
        }
        // 设置请求头
        httpPost.addHeader("Content-Type", Constants.CONTENT_TYPE_FORM);
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvp.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvp, Constants.UTF8));
            } catch (UnsupportedEncodingException e) {
                log.error("调用HttpClient.sendFormPost UnsupportedEncodingException, url=" + url, e);
            }
        }
        return sendPost(httpPost);
    }

    /**
     * 向指定 URL 发送POST方法的请求(JSON)
     * @param url  发送请求的 URL
     * @param jsonParam  请求参数，json字符串
     * @param headers  附加请求头参数
     * @return
     */
    public static String sendJsonPost(String url, String jsonParam, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        try {
            // 设置请求头信息
            if(MapUtils.isNotEmpty(headers)) {
                for(String key : headers.keySet()){
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            // 设置参数
            StringEntity stringEntity = new StringEntity(jsonParam, Constants.UTF8);
            stringEntity.setContentType(Constants.CONTENT_TYPE_JSON);
            httpPost.setEntity(stringEntity);
        } catch (UnsupportedCharsetException e) {
            log.error("调用HttpClient.sendFormPost UnsupportedCharsetException, url=" + url, e);
        }
        return sendPost(httpPost);
    }

    /**
     * 发送Post请求
     * @param httpPost
     * @return
     */
    private static String sendPost(HttpPost httpPost)
    {
        CloseableHttpClient httpClient;
        CloseableHttpResponse response = null;
        // 响应内容
        String result = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            // 配置请求信息
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            // 得到响应实例
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("调用HttpClient.sendPost Exception, url=" + httpPost.getURI(), e);
        } finally {
            // 关闭资源
            if (response != null) {
                log.info("调用HttpClient.sendPost url={}, response status={}",
                        httpPost.getURI(), response.getStatusLine().getStatusCode());
                if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    log.info("调用HttpClient.sendPost result={}", result);
                }
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("调用HttpClient.sendPost.response.close IOException, url=" + httpPost.getURI(), e);
                }
            }
        }
        return result;
    }

}
