package net.lab1024.sa.base.module.support.file.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.code.SystemErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.file.domain.vo.FileDownloadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileMetadataVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 本地存储 实现
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ <a href="https://1024lab.net">1024创新实验室</a> ）
 */
@Slf4j
public class FileStorageLocalServiceImpl implements IFileStorageService {


    public static final String UPLOAD_MAPPING = "/upload";

    @Value("${file.storage.local.upload-path}")
    private String uploadPath;

    @Value("${file.storage.local.url-prefix}")
    private String urlPrefix;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port}")
    private String port;

    @PostConstruct
    public void initUrlPrefix() {
        if (SmartStringUtil.isNotEmpty(urlPrefix)) {
            return;
        }

        String localhostIp = NetUtil.getLocalhostStr();
        String finalContextPath = contextPath.startsWith("/") ? contextPath : "/" + contextPath;
        if (finalContextPath.endsWith("/")) {
            finalContextPath = finalContextPath.substring(0, finalContextPath.length() - 1);
        }
        urlPrefix = "http://" + localhostIp + ":" + port + finalContextPath + UPLOAD_MAPPING;
        urlPrefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
    }

    @Override
    public ResponseDTO<FileUploadVO> upload(MultipartFile multipartFile, String path) {
        if (null == multipartFile) {
            return ResponseDTO.userErrorParam("上传文件不能为空");
        }
        String filePath = uploadPath + path;
        File directory = new File(filePath);
        if (!directory.exists()) {
            // 目录不存在，新建
            directory.mkdirs();
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        FileUploadVO fileUploadVO = new FileUploadVO();
        //原文件名
        String originalFileName = multipartFile.getOriginalFilename();
        //新文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_FORMATTER);
        String newFileName = uuid + "_" + time;
        String fileType = FilenameUtils.getExtension(originalFileName);
        if (SmartStringUtil.isNotEmpty(fileType)) {
            newFileName = newFileName + "." + fileType;
        }
        //生成文件key
        String fileKey = path + newFileName;
        //创建文件
        File fileTemp = new File(new File(filePath + newFileName).getAbsolutePath());
        try {
            multipartFile.transferTo(fileTemp);
            fileUploadVO.setFileUrl(this.generateFileUrl(fileKey));
            fileUploadVO.setFileName(newFileName);
            fileUploadVO.setFileKey(fileKey);
            fileUploadVO.setFileSize(multipartFile.getSize());
            fileUploadVO.setFileType(FilenameUtils.getExtension(originalFileName));
        } catch (IOException e) {
            if (fileTemp.exists() && fileTemp.isFile()) {
                fileTemp.delete();
            }
            log.error("", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return ResponseDTO.ok(fileUploadVO);
    }

    /**
     * 生成fileUrl地址
     *
     * @param filePath
     * @return
     */
    public String generateFileUrl(String filePath) {
        return urlPrefix + filePath;
    }

    /**
     * 获取文件Url
     *
     * @param fileKey
     * @return
     */
    @Override
    public ResponseDTO<String> getFileUrl(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return ResponseDTO.userErrorParam("文件不存在，key为空");
        }

        String fileUrl = this.generateFileUrl(fileKey);
        return ResponseDTO.ok(fileUrl);
    }

    /**
     * 文件下载
     *
     * @param fileKey
     * @return
     */
    @Override
    public ResponseDTO<FileDownloadVO> download(String fileKey) {
        String filePath = uploadPath + fileKey;
        File localFile = new File(filePath);
        InputStream in = null;
        try {
            in = Files.newInputStream(localFile.toPath());
            // 输入流转换为字节流
            byte[] buffer = FileCopyUtils.copyToByteArray(in);
            FileDownloadVO fileDownloadVO = new FileDownloadVO();
            fileDownloadVO.setData(buffer);

            FileMetadataVO fileMetadataDTO = new FileMetadataVO();
            fileMetadataDTO.setFileName(localFile.getName());
            fileMetadataDTO.setFileSize(localFile.length());
            fileMetadataDTO.setFileFormat(FilenameUtils.getExtension(localFile.getName()));
            fileDownloadVO.setMetadata(fileMetadataDTO);

            return ResponseDTO.ok(fileDownloadVO);
        } catch (IOException e) {
            log.error("文件下载-发生异常：", e);
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "文件下载失败");
        } finally {
            try {
                // 关闭输入流
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("文件下载-发生异常：", e);
            }
        }
    }

    @Override
    public ResponseDTO<String> delete(String fileKey) {
        String filePath = uploadPath + fileKey;
        File localFile = new File(filePath);
        try {
            FileUtils.forceDelete(localFile);
        } catch (IOException e) {
            log.error("删除本地文件失败：{}", filePath, e);
        }
        return ResponseDTO.ok();
    }
}
