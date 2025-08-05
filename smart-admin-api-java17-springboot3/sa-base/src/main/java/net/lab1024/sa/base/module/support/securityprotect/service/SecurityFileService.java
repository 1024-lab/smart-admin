package net.lab1024.sa.base.module.support.securityprotect.service;

import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.apache.commons.io.IOUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 三级等保 文件 相关
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2024/08/22 19:25:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Service
@Slf4j
public class SecurityFileService {

    @Resource
    private Level3ProtectConfigService level3ProtectConfigService;

    // 定义白名单MIME类型
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("application/json", "application/zip", "application/x-7z-compressed", "application/pdf", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-works", "text/csv", "audio/*", "video/*",
            // 图片类型 svg有安全隐患，所以不使用"image/*"
            "image/jpeg", "image/png", "image/gif", "image/bmp");

    /**
     * 检测文件安全类型
     */
    public ResponseDTO<String> checkFile(MultipartFile file) {

        // 检验文件大小
        if (level3ProtectConfigService.getMaxUploadFileSizeMb() > 0) {
            long maxSize = level3ProtectConfigService.getMaxUploadFileSizeMb() * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return ResponseDTO.userErrorParam("上传文件最大为:" + level3ProtectConfigService.getMaxUploadFileSizeMb() + " mb");
            }
        }

        // 文件类型安全检测
        if (level3ProtectConfigService.isFileDetectFlag()) {
            String fileType = getFileMimeType(file);
            if (ALLOWED_MIME_TYPES.stream().noneMatch(allowedType -> matchesMimeType(fileType, allowedType))) {
                return ResponseDTO.userErrorParam("禁止上传此文件类型");
            }
        }

        return ResponseDTO.ok();
    }

    /**
     * 获取文件的 MIME 类型
     *
     * @param file 要检查的文件
     * @return 文件的 MIME 类型
     */
    public static String getFileMimeType(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            TikaConfig tika = new TikaConfig();
            Metadata metadata = new Metadata();
            metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, file.getOriginalFilename());
            TikaInputStream stream = TikaInputStream.get(inputStream);
            MediaType mimetype = tika.getDetector().detect(stream, metadata);
            return mimetype.toString();
        } catch (IOException | TikaException e) {
            log.error(e.getMessage(), e);
            return MimeTypes.OCTET_STREAM;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 检查文件的 MIME 类型是否与指定的MIME 类型匹配（支持通配符）
     *
     * @param fileType 文件的 MIME 类型
     * @param mimetype MIME 类型（支持通配符）
     * @return 是否匹配
     */
    private static boolean matchesMimeType(String fileType, String mimetype) {
        if (mimetype.endsWith("/*")) {
            String prefix = mimetype.substring(0, mimetype.length() - 1);
            return fileType.startsWith(prefix);
        } else {
            return fileType.equalsIgnoreCase(mimetype);
        }
    }
}
