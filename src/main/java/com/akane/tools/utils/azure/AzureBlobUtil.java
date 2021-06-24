package com.akane.tools.utils.azure;

import com.akane.tools.model.azure.BlobFile;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;
import lombok.extern.slf4j.Slf4j;

/**
 * Azure Blob工具
 *
 * @author akane
 */
@Slf4j
public class AzureBlobUtil
{
    private static BlobServiceClient blobServiceClient;

    /**
     * 获取blobserviceclient
     * @param connectionString  example：DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;
     *                          AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;
     *                          BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;
     */
    private static void setBlobServiceClient(String connectionString)
    {
        if(blobServiceClient == null) {
            blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
        }
    }

    /**
     * 获取Blob Client
     * @param connectionString
     * @param containerName
     * @param blobName
     * @return
     */
    private static BlobClient getBlobClient(String connectionString, String containerName, String blobName)
    {
        setBlobServiceClient(connectionString);
        BlobContainerClient blobContainerClient;
        try {
            blobContainerClient = blobServiceClient.createBlobContainer(containerName);
        } catch (Exception e) {
            log.info(" - create azure blob container:{} exception - {}", containerName, e.toString());
            blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        }
        return blobContainerClient.getBlobClient(blobName);
    }

    /**
     * 上传本地文件
     * @param blobFile
     */
    public static void uploadLocalFile(BlobFile blobFile)
    {
        BlobClient blobClient = getBlobClient(blobFile.getConnectionString(), blobFile.getContainerName(),
                blobFile.getBlobName());
        // 如果存在相同的名称，覆盖写入
        blobClient.uploadFromFile(blobFile.getFilePath(), true);
    }

    /**
     * 上传文件流
     * @param blobFile
     */
    public static void uploadFileStream(BlobFile blobFile)
    {
        BlobClient blobClient = getBlobClient(blobFile.getConnectionString(), blobFile.getContainerName(),
                blobFile.getBlobName());
        int length;
        try {
            length = blobFile.getFileStream().available();
        } catch (Exception e) {
            log.info(" - upload blob file from stream exception - {}", e.toString());
            length = 0;
        }
        blobClient.upload(blobFile.getFileStream(), length, true);
    }

    /**
     * 获取blob的sas访问地址
     * @param blobFile
     * @return
     */
    public static String getBlobSasUrl(BlobFile blobFile)
    {
        setBlobServiceClient(blobFile.getConnectionString());
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(blobFile.getContainerName());
        BlobClient blobClient = blobContainerClient.getBlobClient(blobFile.getBlobName());
        // 设置blob的操作权限
        BlobContainerSasPermission blobContainerSasPermission = new BlobContainerSasPermission();
        if(blobFile.getReadPermission()) {
            blobContainerSasPermission.setReadPermission(true);
        }
        if(blobFile.getWritePermission()) {
            blobContainerSasPermission.setWritePermission(true);
        }
        if(blobFile.getDeletePermission()) {
            blobContainerSasPermission.setDeletePermission(true);
        }
        // 设置sas签名参数
        BlobServiceSasSignatureValues signatureValues = new BlobServiceSasSignatureValues(
                blobFile.getExpireTime(), blobContainerSasPermission);
        if(blobFile.getOnlyHttps()) {
            signatureValues.setProtocol(SasProtocol.HTTPS_ONLY);
        } else {
            signatureValues.setProtocol(SasProtocol.HTTPS_HTTP);
        }

        return blobClient.getBlobUrl() + "?" + blobClient.generateSas(signatureValues);
    }

}
