package net.lab1024.sa.common.module.support.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.code.SystemErrorCode;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.config.FileCloudConfig;
import net.lab1024.sa.common.module.support.file.domain.vo.FileDownloadVO;
import net.lab1024.sa.common.module.support.file.domain.vo.FileMetadataVO;
import net.lab1024.sa.common.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.common.module.support.file.constant.FileFolderTypeEnum;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 云计算 实现
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
public class FileStorageCloudServiceImpl implements IFileStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private FileCloudConfig cloudConfig;

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

    @Override
    public ResponseDTO<FileUploadVO> fileUpload(MultipartFile file, String path) {
        // 设置文件 key
        String originalFilename = file.getOriginalFilename();
        String fileType = FilenameUtils.getExtension(originalFilename);
        String fileKey = path + this.generateFileName(originalFilename);
        // 文件名称 URL 编码
        String urlEncoderFilename;
        try {
            urlEncoderFilename = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error("阿里云文件上传服务URL ENCODE-发生异常：", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR,"上传失败");
        }
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentEncoding(StandardCharsets.UTF_8.name());
        meta.setContentDisposition("attachment;filename=" + urlEncoderFilename);
        Map<String, String> userMetadata = new HashMap(10);
        userMetadata.put(USER_METADATA_FILE_NAME, urlEncoderFilename);
        userMetadata.put(USER_METADATA_FILE_FORMAT, fileType);
        userMetadata.put(USER_METADATA_FILE_SIZE, String.valueOf(file.getSize()));
        meta.setUserMetadata(userMetadata);
        meta.setContentLength(file.getSize());
        meta.setContentType(this.getContentType(fileType));
        try {
            amazonS3.putObject(cloudConfig.getBucketName(), fileKey, file.getInputStream(), meta);
        } catch (IOException e) {
            log.error("文件上传-发生异常：", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR,"上传失败");
        }
        // 根据文件路径获取并设置访问权限
        CannedAccessControlList acl = this.getACL(path);
        amazonS3.setObjectAcl(cloudConfig.getBucketName(), fileKey, acl);
        // 返回上传结果
        FileUploadVO uploadVO = new FileUploadVO();
        uploadVO.setFileName(originalFilename);
        uploadVO.setFileType(fileType);
        // 根据 访问权限 返回不同的 URL
        String url = cloudConfig.getPublicUrl() + fileKey;
        if (CannedAccessControlList.Private.equals(acl)) {
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
     * @param fileKey
     * @return
     */
    @Override
    public ResponseDTO<String> getFileUrl(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return ResponseDTO.userErrorParam();
        }
        if (!fileKey.startsWith(FileFolderTypeEnum.FOLDER_PRIVATE)) {
            // 不是私有的 都公共读
            return ResponseDTO.ok(cloudConfig.getPublicUrl() + fileKey);
        }
        Date expiration = new Date(System.currentTimeMillis() + cloudConfig.getUrlExpire());
        URL url = amazonS3.generatePresignedUrl(cloudConfig.getBucketName(), fileKey, expiration);
        String urlStr = url.toString().replace("http://", "https://");
        return ResponseDTO.ok(urlStr);
    }

    /**
     * 流式下载（名称为原文件）
     */
    @Override
    public ResponseDTO<FileDownloadVO> fileDownload(String key) {
        //获取oss对象
        S3Object s3Object = amazonS3.getObject(cloudConfig.getBucketName(), key);
        // 获取文件 meta
        ObjectMetadata metadata = s3Object.getObjectMetadata();
        Map<String, String> userMetadata = metadata.getUserMetadata();
        FileMetadataVO metadataDTO = null;
        if (MapUtils.isNotEmpty(userMetadata)) {
            metadataDTO = new FileMetadataVO();
            metadataDTO.setFileFormat(userMetadata.get(USER_METADATA_FILE_FORMAT));
            metadataDTO.setFileName(userMetadata.get(USER_METADATA_FILE_NAME));
            String fileSizeStr = userMetadata.get(USER_METADATA_FILE_SIZE);
            Long fileSize = StringUtils.isBlank(fileSizeStr) ? null : Long.valueOf(fileSizeStr);
            metadataDTO.setFileSize(fileSize);
        }

        // 获得输入流
        InputStream objectContent = s3Object.getObjectContent();
        try {
            // 输入流转换为字节流
            byte[] buffer = FileCopyUtils.copyToByteArray(objectContent);

            FileDownloadVO fileDownloadVO = new FileDownloadVO();
            fileDownloadVO.setData(buffer);
            fileDownloadVO.setMetadata(metadataDTO);
            return ResponseDTO.ok(fileDownloadVO);
        } catch (IOException e) {
            log.error("文件下载-发生异常：", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR,"下载失败");
        } finally {
            try {
                // 关闭输入流
                objectContent.close();
                s3Object.close();
            } catch (IOException e) {
                log.error("文件下载-发生异常：", e);
            }
        }
    }

    /**
     * 根据文件夹路径 返回对应的访问权限
     *
     * @param fileKey
     * @return
     */
    private CannedAccessControlList getACL(String fileKey) {
        // 公用读
        if (fileKey.contains(FileFolderTypeEnum.FOLDER_PUBLIC)) {
            return CannedAccessControlList.PublicRead;
        }
        // 其他默认私有读写
        return CannedAccessControlList.Private;
    }

    /**
     * 单个删除文件
     * 根据 file key 删除文件
     * ps：不能删除fileKey不为空的文件夹
     *
     * @param fileKey 文件or文件夹
     * @return
     */
    @Override
    public ResponseDTO<String> delete(String fileKey) {
        amazonS3.deleteObject(cloudConfig.getBucketName(), fileKey);
        return ResponseDTO.ok();
    }


    @Override
    public Long cacheExpireSecond(){
        return cloudConfig.getUrlExpire() - 1800;
    }

}
