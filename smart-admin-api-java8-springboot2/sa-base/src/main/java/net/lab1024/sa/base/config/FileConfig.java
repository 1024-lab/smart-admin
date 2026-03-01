package net.lab1024.sa.base.config;

import lombok.Data;
import net.lab1024.sa.base.module.support.file.service.FileStorageCloudServiceImpl;
import net.lab1024.sa.base.module.support.file.service.FileStorageLocalServiceImpl;
import net.lab1024.sa.base.module.support.file.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * 文件上传 配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019-09-02 23:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Configuration
public class FileConfig implements WebMvcConfigurer {

    private static final String MODE_CLOUD = "cloud";

    private static final String MODE_LOCAL = "local";

    @Value("${file.storage.mode}")
    private String mode;

    @Value("${file.storage.cloud.region}")
    private String cloudRegion;

    @Value("${file.storage.cloud.endpoint}")
    private String cloudEndpoint;

    @Value("${file.storage.cloud.bucket-name}")
    private String cloudBucketName;

    @Value("${file.storage.cloud.access-key}")
    private String cloudAccessKey;

    @Value("${file.storage.cloud.secret-key}")
    private String cloudSecretKey;

    @Value("${file.storage.cloud.private-url-expire-seconds}")
    private Long cloudPrivateUrlExpireSeconds;

    @Value("${file.storage.cloud.public-url-prefix}")
    private String cloudPublicUrlPrefix;

    @Value("${file.storage.local.upload-path}")
    private String localUploadPath;


    /**
     * 初始化 s3 client 配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = MODE_CLOUD)
    public S3Client initS3Client() {
        return S3Client.builder()
                .region(Region.of(cloudRegion))
                .endpointOverride(URI.create(cloudEndpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(cloudAccessKey, cloudSecretKey)))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(false)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    /**
     * 初始化 s3 预签名
     */
    @Bean
    @ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = MODE_CLOUD)
    public S3Presigner initS3Presigner() {
        return S3Presigner
                .builder()
                .region(Region.of(cloudRegion))
                .endpointOverride(URI.create(cloudEndpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(cloudAccessKey, cloudSecretKey)))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(false)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = MODE_CLOUD)
    public IFileStorageService initCloudFileService() {
        return new FileStorageCloudServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = MODE_LOCAL)
    public IFileStorageService initLocalFileService() {
        return new FileStorageLocalServiceImpl();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (MODE_LOCAL.equals(mode)) {
            String path = localUploadPath.endsWith("/") ? localUploadPath : localUploadPath + "/";
            registry.addResourceHandler(FileStorageLocalServiceImpl.UPLOAD_MAPPING + "/**").addResourceLocations("file:" + path);
        }
    }

}
