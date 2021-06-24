package com.akane.tools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AzureBlobConfig
 *
 * @author akane
 */
@Component
@ConfigurationProperties(prefix = "azure.blob")
public class AzureBlobConfig {
    /** 连接字符串 */
    private static String connectionString;

    public static String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        AzureBlobConfig.connectionString = connectionString;
    }
}
