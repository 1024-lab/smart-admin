package net.lab1024.smartadmin.module.support.file.service;

import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.file.constant.FileResponseCodeConst;
import net.lab1024.smartadmin.module.support.file.constant.FileServiceNameConst;
import net.lab1024.smartadmin.module.support.file.domain.vo.UploadVO;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigDao;
import net.lab1024.smartadmin.module.system.systemconfig.constant.SystemConfigEnum;
import net.lab1024.smartadmin.module.system.systemconfig.domain.entity.SystemConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 下午 16:15
 * @since JDK1.8
 */
@Slf4j
@Service(FileServiceNameConst.LOCAL)
public class FileServiceLocal implements IFileService {

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${file-upload-service.path}")
    private String fileParentPath;

    private static final Long DEFAULT_SIZE = 10 * 1024 * 1024L;

    @Override
    public ResponseDTO<UploadVO> fileUpload(MultipartFile multipartFile, String path) {
        if (null == multipartFile) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_EMPTY);
        }
        Long maxSize = DEFAULT_SIZE;
        if (StringUtils.isNotEmpty(maxFileSize)) {
            String maxSizeStr = maxFileSize.toLowerCase().replace("mb", "");
            maxSize = Integer.valueOf(maxSizeStr) * 1024 * 1024L;
        }
        if (multipartFile.getSize() > maxSize) {
            return ResponseDTO.wrap(FileResponseCodeConst.FILE_SIZE_ERROR, String.format(FileResponseCodeConst.FILE_SIZE_ERROR.getMsg(), maxFileSize));
        }
        String filePath = fileParentPath;
        String urlParent = this.localUrlPrefix();
        if (urlParent == null) {
            return ResponseDTO.wrap(FileResponseCodeConst.LOCAL_UPDATE_PREFIX_ERROR);
        }
        if (StringUtils.isNotEmpty(path)) {
            filePath = filePath + path + "/";
            urlParent = urlParent + path + "/";
        }
        File directory = new File(filePath);
        if (!directory.exists()) {
            // 目录不存在，新建
            directory.mkdirs();
        }
        UploadVO localUploadVO = new UploadVO();
        String newFileName;
        File fileTemp;
        String originalFileName;
        originalFileName = multipartFile.getOriginalFilename();
        newFileName = this.generateFileName(originalFileName);
        fileTemp = new File(new File(filePath + newFileName).getAbsolutePath());
        try {
            multipartFile.transferTo(fileTemp);
            localUploadVO.setUrl(urlParent + newFileName);
            localUploadVO.setFileName(newFileName);
            localUploadVO.setFilePath(path + "/" + newFileName);
            localUploadVO.setFileSize(multipartFile.getSize());
        } catch (IOException e) {
            if (fileTemp.exists() && fileTemp.isFile()) {
                fileTemp.delete();
            }
            log.error("", e);
            return ResponseDTO.wrap(FileResponseCodeConst.UPLOAD_ERROR);
        }
        return ResponseDTO.succData(localUploadVO);
    }

    @Override
    public ResponseDTO<String> getFileUrl(String path) {
        String urlParent = this.localUrlPrefix();
        if (urlParent == null) {
            return ResponseDTO.wrap(FileResponseCodeConst.LOCAL_UPDATE_PREFIX_ERROR);
        }
        String url = urlParent + path;
        return ResponseDTO.succData(url);
    }

    private String localUrlPrefix() {
        SystemConfigEntity configEntity = systemConfigDao.getByKey(SystemConfigEnum.Key.LOCAL_UPLOAD_URL_PREFIX.name());
        if (configEntity == null) {
            return null;
        }
        return configEntity.getConfigValue();
    }

    @Override
    public ResponseEntity<byte[]> fileDownload(String key, String fileName, HttpServletRequest request) {

        String url = fileParentPath + key;
        // 创建文件
        File file = new File(url);
        return this.downloadMethod(file, request);
    }
}
