package net.lab1024.smartadmin.module.support.file.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.file.constant.FileResponseCodeConst;
import net.lab1024.smartadmin.module.support.file.constant.FileServiceNameConst;
import net.lab1024.smartadmin.module.support.file.domain.dto.OSSConfig;
import net.lab1024.smartadmin.module.support.file.domain.vo.UploadVO;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigService;
import net.lab1024.smartadmin.module.system.systemconfig.constant.SystemConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/10 0010 上午 8:29
 * @since JDK1.8
 */
@Slf4j
@Service(FileServiceNameConst.ALI_OSS)
public class FileServiceAliYun implements IFileService {

    @Autowired
    private SystemConfigService systemConfigService;

    OSSClient ossClient = null;

    String accessConfig = null;

    @Override
    public ResponseDTO<UploadVO> fileUpload(MultipartFile multipartFile, String path) {
        OSSConfig ossConfig = systemConfigService.selectByKey2Obj(SystemConfigEnum.Key.ALI_OSS.name(), OSSConfig.class);
        try {
            InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
            if (! ossConfig.toString().equals(accessConfig)) {
                //accessKeyId 发生变动自动创建新的
                if (ossClient != null) {
                    ossClient.shutdown();
                }
                ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
                accessConfig = ossConfig.toString();
            }
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String ossPath = path + "/" + uuid;
            String fileName = multipartFile.getOriginalFilename();
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentDisposition("attachment;filename=" + fileName);
            Map<String, String> userMetadata = new HashMap();
            userMetadata.put("fileName", fileName);
            userMetadata.put("fileExt", fileExt);
            userMetadata.put("fileSize", String.valueOf(multipartFile.getSize()));
            meta.setUserMetadata(userMetadata);
            meta.setContentType(this.getContentType(fileExt));
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), ossPath, inputStream, meta);
            ossClient.putObject(putObjectRequest);
            UploadVO localUploadVO = new UploadVO();
            localUploadVO.setUrl(this.getUrl(ossPath, ossConfig.getBucketName(), ossClient));
            localUploadVO.setFileName(fileName);
            localUploadVO.setFilePath(ossPath);
            localUploadVO.setFileSize(multipartFile.getSize());
            return ResponseDTO.succData(localUploadVO);
        } catch (Exception e) {
            log.error("ALI UPLOAD ERROR : {}", e);
        }
        return ResponseDTO.wrap(FileResponseCodeConst.UPLOAD_ERROR);
    }

    @Override
    public ResponseDTO<String> getFileUrl(String path) {
        OSSConfig ossConfig = systemConfigService.selectByKey2Obj(SystemConfigEnum.Key.ALI_OSS.name(), OSSConfig.class);
        try {
            if (! ossConfig.toString().equals(accessConfig)) {
                //accessKeyId 发生变动自动创建新的
                if (ossClient != null) {
                    ossClient.shutdown();
                }
                ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
                accessConfig = ossConfig.toString();
            }
            String url = this.getUrl(path, ossConfig.getBucketName(), ossClient);
            return ResponseDTO.succData(url);
        } catch (Exception e) {
            log.error("ALI getFileUrl ERROR : {}", e);
        }
        return ResponseDTO.wrap(FileResponseCodeConst.URL_ERROR);
    }

    private String getUrl(String path, String bucketName, OSSClient ossClient) {
        Date expiration = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
        URL url = ossClient.generatePresignedUrl(bucketName, path, expiration);
        return url.toString();
    }

    /**
     * 流式下载（名称为原文件）
     */
    @Override
    public ResponseEntity<byte[]> fileDownload(String key, String fileName, HttpServletRequest request) {
        File file = this.getFile(key, fileName);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }
        return this.downloadMethod(file, request);
    }

    /**
     * 根据osskey获取文件
     *
     * @param key
     * @return
     */
    public File getFile(String key, String fileName) {
        OSSConfig ossConfig = systemConfigService.selectByKey2Obj(SystemConfigEnum.Key.ALI_OSS.name(), OSSConfig.class);
        if (! ossConfig.toString().equals(accessConfig)) {
            //accessKeyId 发生变动自动创建新的
            if (ossClient != null) {
                ossClient.shutdown();
            }
            ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
            accessConfig = ossConfig.toString();
        }
        //获取oss对象
        OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), key);
        if (StringUtils.isBlank(fileName)) {
            // 获取元信息
            ObjectMetadata objectMetadata = ossObject.getObjectMetadata();
            // 获取下载时文件名
            Map<String, String> userMetadata = objectMetadata.getUserMetadata();
            fileName = userMetadata == null ? "" : userMetadata.get("filename");
            if (StringUtils.isBlank(fileName)) {
                fileName = objectMetadata.getContentDisposition();
            }
        }
        // 创建文件
        File file = new File(fileName);
        // 获得输入流
        InputStream objectContent = ossObject.getObjectContent();
        try {
            // 输入流转换为字节流
            byte[] buffer = FileCopyUtils.copyToByteArray(objectContent);
            // 字节流写入文件
            FileCopyUtils.copy(buffer, file);
            // 关闭输入流
            objectContent.close();
        } catch (IOException e) {
            log.error("文件获取失败：" + e);
            return null;
        } finally {
            try {
                ossObject.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return file;
    }
}
