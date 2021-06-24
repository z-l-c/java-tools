package com.akane.tools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Email Config
 *
 * @author akane
 */
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig
{
    // 发送人
    private static String sender;

    public static String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        EmailConfig.sender = sender;
    }
}
