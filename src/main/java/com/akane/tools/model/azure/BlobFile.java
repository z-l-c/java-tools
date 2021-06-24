package com.akane.tools.model.azure;

import java.io.InputStream;
import java.time.OffsetDateTime;

/**
 * Blob File
 *
 * @author akane
 */
public class BlobFile {
    /** Blob 存储连接字符串 */
    private String connectionString;
    /** Blob 容器名称 */
    private String containerName;
    /** Blob 实例名称，可为文件名 */
    private String blobName;
    /** 上传文件本地路径 */
    private String filePath;
    /** 上传文件流 */
    private InputStream fileStream;
    /** 过期时间 默认1天 */
    private OffsetDateTime expireTime = OffsetDateTime.now().plusDays(1);
    /** 是否有读取权限 */
    private Boolean readPermission = true;
    /** 是否有写权限 */
    private Boolean writePermission = false;
    /** 是否有删除权限 */
    private Boolean deletePermission = false;
    /** 是否只支持https */
    private Boolean onlyHttps = false;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getBlobName() {
        return blobName;
    }

    public void setBlobName(String blobName) {
        this.blobName = blobName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public OffsetDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(OffsetDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(Boolean readPermission) {
        this.readPermission = readPermission;
    }

    public Boolean getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(Boolean writePermission) {
        this.writePermission = writePermission;
    }

    public Boolean getDeletePermission() {
        return deletePermission;
    }

    public void setDeletePermission(Boolean deletePermission) {
        this.deletePermission = deletePermission;
    }

    public Boolean getOnlyHttps() {
        return onlyHttps;
    }

    public void setOnlyHttps(Boolean onlyHttps) {
        this.onlyHttps = onlyHttps;
    }
}
