package net.lab1024.sa.base.module.support.file.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.config.FileConfig;
import net.lab1024.sa.base.constant.RedisKeyConst;
import net.lab1024.sa.base.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.sa.base.module.support.file.dao.FileDao;
import net.lab1024.sa.base.module.support.file.domain.vo.FileDownloadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileMetadataVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import net.lab1024.sa.base.module.support.redis.RedisService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 云计算 实现
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
public class FileStorageCloudServiceImpl implements IFileStorageService {

    /**
     * 自定义元数据 文件名称
     */
    private static final String USER_METADATA_FILE_NAME = "file-name";

    /**
     * 自定义元数据 文件格式
     */
    private static final String USER_METADATA_FILE_FORMAT = "file-format";

    /**
     * 自定义元数据 文件大小
     */
    private static final String USER_METADATA_FILE_SIZE = "file-size";

    @Resource
    private S3Client s3Client;

    @Resource
    private FileConfig cloudConfig;

    @Resource
    private RedisService redisService;

    @Resource
    private FileDao fileDao;

    @Override
    public ResponseDTO<FileUploadVO> upload(MultipartFile file, String path) {
        // 设置文件 key
        String originalFileName = file.getOriginalFilename();
        if (SmartStringUtil.isEmpty(originalFileName)) {
            return ResponseDTO.userErrorParam("上传文件名为空");
        }

        String fileType = FilenameUtils.getExtension(originalFileName);
        String uuid = IdUtil.fastSimpleUUID();
        String time = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_FORMATTER);
        String fileKey = path + uuid + "_" + time + "." + fileType;

        // 文件名称 URL 编码
        String urlEncoderFilename;
        urlEncoderFilename = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8);
        Map<String, String> userMetadata = new HashMap<>(10);
        userMetadata.put(USER_METADATA_FILE_NAME, urlEncoderFilename);
        userMetadata.put(USER_METADATA_FILE_FORMAT, fileType);
        userMetadata.put(USER_METADATA_FILE_SIZE, String.valueOf(file.getSize()));

        // 根据文件路径获取并设置访问权限
        ObjectCannedACL acl = this.getACL(path);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(cloudConfig.getBucketName())
                .key(fileKey)
                .metadata(userMetadata)
                .contentLength(file.getSize())
                .contentType(this.getContentType(fileType))
                .contentEncoding(StandardCharsets.UTF_8.name())
                .contentDisposition("attachment;filename=" + urlEncoderFilename)
                .acl(acl)
                .build();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (IOException e) {
            log.error("文件上传-发生异常：", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        // 返回上传结果
        FileUploadVO uploadVO = new FileUploadVO();
        uploadVO.setFileName(originalFileName);
        uploadVO.setFileType(fileType);
        // 根据 访问权限 返回不同的 URL
        String url = cloudConfig.getUrlPrefix() + fileKey;
        if (ObjectCannedACL.PRIVATE.equals(acl)) {
            // 获取临时访问的URL
            url = this.getFileUrl(fileKey).getData();
        }
        uploadVO.setFileUrl(url);
        uploadVO.setFileKey(fileKey);
        uploadVO.setFileSize(file.getSize());
        return ResponseDTO.ok(uploadVO);
    }

    /**
     * 获取文件url
     *
     * @param fileKey 文件key
     * @return url
     */
    @Override
    public ResponseDTO<String> getFileUrl(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return ResponseDTO.userErrorParam("文件不存在，key为空");
        }

        if (!fileKey.startsWith(FileFolderTypeEnum.FOLDER_PRIVATE)) {
            // 不是私有的 都公共读
            return ResponseDTO.ok(cloudConfig.getUrlPrefix() + fileKey);
        }

        // 如果是私有的，则规定时间内可以访问，超过规定时间，则连接失效

        String fileRedisKey = RedisKeyConst.Support.FILE_PRIVATE_VO + fileKey;
        FileVO fileVO = redisService.getObject(fileRedisKey, FileVO.class);
        if (fileVO == null) {
            fileVO = fileDao.getByFileKey(fileKey);
            if (fileVO == null) {
                return ResponseDTO.userErrorParam("文件不存在");
            }
            GetObjectRequest getUrlRequest = GetObjectRequest.builder().bucket(cloudConfig.getBucketName()).key(fileKey).build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofSeconds(cloudConfig.getPrivateUrlExpireSeconds())).getObjectRequest(getUrlRequest).build();

            S3Presigner presigner = S3Presigner.builder().region(Region.of(cloudConfig.getRegion())).build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
            String url = presignedGetObjectRequest.url().toString();
            fileVO.setFileUrl(url);
            redisService.set(fileRedisKey, fileVO, cloudConfig.getPrivateUrlExpireSeconds() - 5);
        }

        return ResponseDTO.ok(fileVO.getFileUrl());
    }


    /**
     * 流式下载（名称为原文件）
     */
    @Override
    public ResponseDTO<FileDownloadVO> download(String key) {

        // 获取文件 meta
        HeadObjectRequest objectRequest = HeadObjectRequest.builder().bucket(this.cloudConfig.getBucketName()).key(key).build();
        HeadObjectResponse headObjectResponse = s3Client.headObject(objectRequest);
        Map<String, String> userMetadata = headObjectResponse.metadata();
        FileMetadataVO metadataDTO = null;
        if (MapUtils.isNotEmpty(userMetadata)) {
            metadataDTO = new FileMetadataVO();
            metadataDTO.setFileFormat(userMetadata.get(USER_METADATA_FILE_FORMAT));
            metadataDTO.setFileName(userMetadata.get(USER_METADATA_FILE_NAME));
            String fileSizeStr = userMetadata.get(USER_METADATA_FILE_SIZE);
            Long fileSize = StringUtils.isBlank(fileSizeStr) ? null : Long.valueOf(fileSizeStr);
            metadataDTO.setFileSize(fileSize);
        }

        //获取oss对象
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(cloudConfig.getBucketName()).key(key).build();
        ResponseBytes<GetObjectResponse> s3ClientObject = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes());

        // 输入流转换为字节流
        byte[] buffer = s3ClientObject.asByteArray();
        FileDownloadVO fileDownloadVO = new FileDownloadVO();
        fileDownloadVO.setData(buffer);
        fileDownloadVO.setMetadata(metadataDTO);
        return ResponseDTO.ok(fileDownloadVO);
    }

    /**
     * 根据文件夹路径 返回对应的访问权限
     *
     * @param fileKey 文件key
     * @return 权限
     */
    private ObjectCannedACL getACL(String fileKey) {
        // 公用读
        if (fileKey.contains(FileFolderTypeEnum.FOLDER_PUBLIC)) {
            return ObjectCannedACL.PUBLIC_READ;
        }
        // 其他默认私有读写
        return ObjectCannedACL.PRIVATE;
    }

    /**
     * 单个删除文件
     * 根据 file key 删除文件
     * ps：不能删除fileKey不为空的文件夹
     *
     * @param fileKey 文件or文件夹
     */
    @Override
    public ResponseDTO<String> delete(String fileKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(cloudConfig.getBucketName()).key(fileKey).build();
        s3Client.deleteObject(deleteObjectRequest);
        return ResponseDTO.ok();
    }

}
