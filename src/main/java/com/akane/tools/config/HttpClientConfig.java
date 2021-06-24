package com.akane.tools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * apache http client config
 *
 * @author akane
 */
@Component
@ConfigurationProperties(prefix = "httpclient")
public class HttpClientConfig {

    //最大连接数
    private static String poolMaxActive;
    //最大路由
    private static String poolMaxPerRoute;
    //socket超时时间
    private static String socketTimeOut;
    //连接超时时间
    private static String connectTimeout;
    //连接请求超时时间
    private static String connectionRequestTimeout;

    public static String getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(String maxActive) {
        HttpClientConfig.poolMaxActive = maxActive;
    }

    public static String getPoolMaxPerRoute() {
        return poolMaxPerRoute;
    }

    public void setPoolMaxPerRoute(String maxPerRoute) {
        HttpClientConfig.poolMaxPerRoute = maxPerRoute;
    }

    public static String getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(String socketTimeOut) {
        HttpClientConfig.socketTimeOut = socketTimeOut;
    }

    public static String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        HttpClientConfig.connectTimeout = connectTimeout;
    }

    public static String getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(String connectionRequestTimeout) {
        HttpClientConfig.connectionRequestTimeout = connectionRequestTimeout;
    }
}
