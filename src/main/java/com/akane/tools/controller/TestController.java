package com.akane.tools.controller;

import com.akane.tools.config.AzureBlobConfig;
import com.akane.tools.model.azure.BlobFile;
import com.akane.tools.model.email.AttachProp;
import com.akane.tools.model.email.SendMailProp;
import com.akane.tools.utils.JwtUtils;
import com.akane.tools.utils.azure.AzureBlobUtil;
import com.akane.tools.utils.encrypt.AesUtils;
import com.akane.tools.utils.http.HttpClient;
import com.akane.tools.utils.smtp.SMTPEmailUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TestController
 *
 * @author akane
 */
@RestController
@RequestMapping("/test")
public class TestController
{
    @RequestMapping("/blob")
    public void blob() throws Exception
    {
        BlobFile blobFile = new BlobFile();
        // 连接串
        blobFile.setConnectionString(AzureBlobConfig.getConnectionString());
        // 容器名称
        blobFile.setContainerName("test-container");
        // 生成的文件名
        blobFile.setBlobName(System.currentTimeMillis() + ".jpg");

        //TODO 方式一 上传的文件的本地完整路径
        blobFile.setFilePath("/path/to/file.jpg");
        // 文件路径
        AzureBlobUtil.uploadLocalFile(blobFile);

        //TODO 方式二 上传文件流
//        URL url  = new URL("https://www.w3school.com.cn/i/eg_tulip.jpg");
//        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
//        httpUrl.connect();
//        httpUrl.getInputStream();
//        InputStream is = httpUrl.getInputStream();
//        blobFile.setFileStream(is);
//        AzureBlobUtil.uploadFileStream(blobFile);

        //获取临时访问地址，每次获取
        String uri = AzureBlobUtil.getBlobSasUrl(blobFile);
        System.out.println(uri);
    }

    @RequestMapping("/email")
    public void email()
    {
        String[] to = new String[]{"test@test.com"};
        String[] cc = new String[]{"test@test.com"};

        List<AttachProp> attachPropList = new ArrayList<>();
        AttachProp attachProp = new AttachProp();
        attachProp.setFileName("file.jpg");
        attachProp.setFilePath("/path/to/file.jpg");
        attachPropList.add(attachProp);

        SendMailProp sendMailProp = new SendMailProp();
        sendMailProp.setTo(Arrays.asList(to));
        sendMailProp.setCc(Arrays.asList(cc));
        sendMailProp.setBcc(Arrays.asList(to));
        sendMailProp.setSubject("测试");
        sendMailProp.setContent("<h1>标题</h1><br/>测试邮件发送，请忽略");
        sendMailProp.setMimeType("text/html;charset=gbk");
        sendMailProp.setAttachment(attachPropList);
        try {
            SMTPEmailUtils.sendEmail(sendMailProp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/httpClient")
    public void httpClient()
    {
        String url = "https://www.baidu.com";
        String response = HttpClient.sendGet(url, null);
        System.out.println(response);
    }

    @RequestMapping("/jwt")
    public void jwt()
    {
        String token = "valid jwt token";
        DecodedJWT decodedJWT = JwtUtils.parseJWT(token);
        System.out.println(decodedJWT.getClaim("name"));
    }

    @RequestMapping("/aes")
    public void aes() throws Exception
    {
        String encryptText = AesUtils.encrypt("I am a string");
        System.out.println(encryptText);

        String decryptText = AesUtils.decrypt(encryptText);
        System.out.println(decryptText);
    }
}
