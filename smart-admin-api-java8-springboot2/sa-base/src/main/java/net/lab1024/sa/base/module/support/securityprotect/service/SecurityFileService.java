package net.lab1024.sa.base.module.support.securityprotect.service;

import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

/**
 * 三级等保 文件上传 相关
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2024/08/22 19:25:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Service
public class SecurityFileService {

    @Resource
    private Level3ProtectConfigService level3ProtectConfigService;


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
        if (!level3ProtectConfigService.isFileDetectFlag()) {
            return ResponseDTO.ok();
        }

        // 检测文件类型
        // .....

        return ResponseDTO.ok();
    }

}
;