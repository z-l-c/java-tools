package com.akane.tools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SMTP Config
 *
 * @author akane
 */
@Component
@ConfigurationProperties(prefix = "smtp")
public class SMTPConfig
{
    // smtp host
    private static String host;
    // 是否启用ssl
    private static Boolean ssl;
    // 是否需要账号登录鉴权
    private static Boolean auth;
    // 认证账号，auth=ture时，填写
    private static String user;
    // 账号密钥，auth=ture时，填写
    private static String pass;

    public static String getHost() {
        return host;
    }

    public void setHost(String host) {
        SMTPConfig.host = host;
    }

    public static Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        SMTPConfig.ssl = ssl;
    }

    public static Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        SMTPConfig.auth = auth;
    }

    public static String getUser() {
        return user;
    }

    public void setUser(String user) {
        SMTPConfig.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        SMTPConfig.pass = pass;
    }
}
