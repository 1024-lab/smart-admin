package net.lab1024.sa.base.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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

/**
 * 文件上传 配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019-09-02 23:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Configuration
public class FileConfig implements WebMvcConfigurer {

    private static final String MODE_CLOUD = "cloud";

    private static final String MODE_LOCAL = "local";

    @Value("${file.storage.cloud.region}")
    private String region;

    @Value("${file.storage.cloud.endpoint}")
    private String endpoint;

    @Value("${file.storage.cloud.bucket-name}")
    private String bucketName;

    @Value("${file.storage.cloud.access-key}")
    private String accessKey;

    @Value("${file.storage.cloud.secret-key}")
    private String secretKey;

    @Value("${file.storage.cloud.private-url-expire-seconds}")
    private Long privateUrlExpireSeconds;

    @Value("${file.storage.cloud.url-prefix}")
    private String urlPrefix;

    @Value("${file.storage.local.upload-path}")
    private String uploadPath;

    @Value("${file.storage.mode}")
    private String mode;

    /**
     * 初始化 云oss client 配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "file.storage", name = {"mode"}, havingValue = "cloud")
    public AmazonS3 initAmazonS3() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withClientConfiguration(clientConfig)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withPathStyleAccessEnabled(false)
                .withChunkedEncodingDisabled(true)
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
            String path = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
            registry.addResourceHandler(FileStorageLocalServiceImpl.UPLOAD_MAPPING + "/**").addResourceLocations("file:" + path);
        }
    }

}
