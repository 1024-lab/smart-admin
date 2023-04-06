package net.lab1024.sa.common.module.support.file.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.code.SystemErrorCode;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.module.support.file.domain.vo.FileDownloadVO;
import net.lab1024.sa.common.module.support.file.domain.vo.FileMetadataVO;
import net.lab1024.sa.common.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.common.module.support.config.ConfigKeyEnum;
import net.lab1024.sa.common.module.support.config.ConfigService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地存储 实现
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
public class FileStorageLocalServiceImpl implements IFileStorageService {

    @Value("${file.storage.local.path}")
    private String localPath;

    @Autowired
    private ConfigService configService;

    @Override
    public ResponseDTO<FileUploadVO> fileUpload(MultipartFile multipartFile, String path) {
        if (null == multipartFile) {
            return ResponseDTO.userErrorParam("上传文件不能为空");
        }
        String filePath = localPath + path;
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
        String newFileName = this.generateFileName(originalFileName);
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
     * @param fileKey
     * @return
     */
    public String generateFileUrl(String fileKey) {
        String configValue = configService.getConfigValue(ConfigKeyEnum.LOCAL_UPLOAD_URL_PREFIX);
        String fileUrl = configValue + fileKey;
        return fileUrl;
    }

    /**
     * 获取文件Url
     *
     * @param fileKey
     * @return
     */
    @Override
    public ResponseDTO<String> getFileUrl(String fileKey) {
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
    public ResponseDTO<FileDownloadVO> fileDownload(String fileKey) {
        String filePath = localPath + fileKey;
        File localFile = new File(filePath);
        InputStream in = null;
        try {
            in = new FileInputStream(localFile);
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
        String filePath = localPath + fileKey;
        File localFile = new File(filePath);
        try {
            FileUtils.forceDelete(localFile);
        } catch (IOException e) {
            log.error("删除本地文件失败：{}", e);
        }
        return ResponseDTO.ok();
    }
}
