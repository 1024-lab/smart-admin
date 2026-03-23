package net.lab1024.sa.admin.module.business.ai.merch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.merch.client.OpenAiPromptClient;
import net.lab1024.sa.admin.module.business.ai.merch.client.VolcengineMediaClient;
import net.lab1024.sa.admin.module.business.ai.merch.constant.AiMerchTaskStatusEnum;
import net.lab1024.sa.admin.module.business.ai.merch.dao.AiMerchTaskDao;
import net.lab1024.sa.admin.module.business.ai.model.domain.entity.AiModelConfigEntity;
import net.lab1024.sa.admin.module.business.ai.model.service.AiModelConfigService;
import net.lab1024.sa.admin.module.business.ai.merch.domain.entity.AiMerchTaskEntity;
import net.lab1024.sa.admin.module.business.ai.merch.domain.form.AiMerchTaskCreateForm;
import net.lab1024.sa.admin.module.business.ai.merch.domain.form.AiMerchTaskQueryForm;
import net.lab1024.sa.admin.module.business.ai.merch.domain.vo.AiMerchTaskVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.file.constant.FileFolderTypeEnum;
import net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiMerchTaskService {

    @Resource
    private AiMerchTaskDao aiMerchTaskDao;

    @Resource
    private AiModelConfigService aiModelConfigService;

    @Resource
    private AiMerchFileService aiMerchFileService;

    @Resource
    private OpenAiPromptClient openAiPromptClient;

    @Resource
    private VolcengineMediaClient volcengineMediaClient;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<AiMerchTaskVO> createTask(AiMerchTaskCreateForm createForm) {
        FileVO sourceFile = aiMerchFileService.getFileByKey(createForm.getSourceFileKey());
        if (sourceFile == null) {
            return ResponseDTO.userErrorParam("服装图片不存在");
        }
        if (!isImageFile(sourceFile.getFileType())) {
            return ResponseDTO.userErrorParam("请上传图片文件");
        }

        AiModelConfigEntity promptConfig;
        AiModelConfigEntity imageConfig;
        AiModelConfigEntity videoConfig;
        try {
            promptConfig = aiModelConfigService.getConfigOrThrow(createForm.getPromptModelId());
            imageConfig = aiModelConfigService.getConfigOrThrow(createForm.getImageModelId());
            videoConfig = aiModelConfigService.getConfigOrThrow(createForm.getVideoModelId());
            assertProvider(promptConfig, "OPENAI", "提示词模型");
            assertProvider(imageConfig, "OPENAI", "图片模型");
            assertProvider(videoConfig, "OPENAI", "视频模型");
        } catch (IllegalArgumentException e) {
            return ResponseDTO.userErrorParam(e.getMessage());
        }

        AiMerchTaskEntity taskEntity = new AiMerchTaskEntity();
        taskEntity.setSourceFileId(sourceFile.getFileId());
        taskEntity.setSourceFileKey(sourceFile.getFileKey());
        taskEntity.setPromptTemplate(StringUtils.trim(createForm.getPromptTemplate()));
        taskEntity.setPromptModelId(promptConfig.getId());
        taskEntity.setPromptModelName(promptConfig.getModelName());
        taskEntity.setImageModelId(imageConfig.getId());
        taskEntity.setImageModelName(imageConfig.getModelName());
        taskEntity.setVideoModelId(videoConfig.getId());
        taskEntity.setVideoModelName(videoConfig.getModelName());
        taskEntity.setPromptStatus(AiMerchTaskStatusEnum.WAITING.getValue());
        taskEntity.setImageStatus(AiMerchTaskStatusEnum.WAITING.getValue());
        taskEntity.setVideoStatus(AiMerchTaskStatusEnum.WAITING.getValue());
        taskEntity.setDeletedFlag(0);
        aiMerchTaskDao.insert(taskEntity);

        try {
            String generatedPrompt = openAiPromptClient.generatePrompt(promptConfig, taskEntity.getPromptTemplate(), sourceFile.getFileUrl());
            taskEntity.setGeneratedPrompt(generatedPrompt);
            taskEntity.setPromptStatus(AiMerchTaskStatusEnum.SUCCESS.getValue());
            taskEntity.setPromptErrorMsg(null);
        } catch (Exception e) {
            taskEntity.setPromptStatus(AiMerchTaskStatusEnum.FAILED.getValue());
            taskEntity.setPromptErrorMsg(truncateError(e.getMessage()));
        }
        aiMerchTaskDao.updateById(taskEntity);
        return ResponseDTO.ok(toTaskVO(taskEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<AiMerchTaskVO> generateImage(Long taskId) {
        AiMerchTaskEntity taskEntity = getTask(taskId);
        if (taskEntity == null) {
            return ResponseDTO.userErrorParam("任务不存在");
        }
        if (!AiMerchTaskStatusEnum.SUCCESS.getValue().equals(taskEntity.getPromptStatus())) {
            return ResponseDTO.userErrorParam("请先成功生成提示词");
        }
        if (AiMerchTaskStatusEnum.PROCESSING.getValue().equals(taskEntity.getImageStatus())) {
            return ResponseDTO.userErrorParam("图片正在生成中");
        }
        if (AiMerchTaskStatusEnum.SUCCESS.getValue().equals(taskEntity.getImageStatus())) {
            return ResponseDTO.userErrorParam("图片已生成完成");
        }

        try {
            AiModelConfigEntity imageConfig = aiModelConfigService.getConfigOrThrow(taskEntity.getImageModelId());
            assertProvider(imageConfig, "VOLCENGINE", "图片模型");
            VolcengineMediaClient.SubmitResult submitResult = volcengineMediaClient.submitImage(imageConfig, taskEntity.getGeneratedPrompt());
            taskEntity.setImageTaskId(submitResult.getTaskId());
            taskEntity.setImageResultUrl(submitResult.getMediaUrl());
            taskEntity.setImageErrorMsg(null);

            if (Boolean.TRUE.equals(submitResult.getCompleted()) && StringUtils.isNotBlank(submitResult.getMediaUrl())) {
                saveMediaFile(taskEntity, true, submitResult.getMediaUrl());
            } else if (StringUtils.isNotBlank(submitResult.getTaskId())) {
                taskEntity.setImageStatus(AiMerchTaskStatusEnum.PROCESSING.getValue());
            } else {
                taskEntity.setImageStatus(AiMerchTaskStatusEnum.FAILED.getValue());
                taskEntity.setImageErrorMsg("图片生成未返回任务号或结果地址");
            }
        } catch (Exception e) {
            taskEntity.setImageStatus(AiMerchTaskStatusEnum.FAILED.getValue());
            taskEntity.setImageErrorMsg(truncateError(e.getMessage()));
        }

        aiMerchTaskDao.updateById(taskEntity);
        return ResponseDTO.ok(toTaskVO(taskEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<AiMerchTaskVO> generateVideo(Long taskId) {
        AiMerchTaskEntity taskEntity = getTask(taskId);
        if (taskEntity == null) {
            return ResponseDTO.userErrorParam("任务不存在");
        }
        if (!AiMerchTaskStatusEnum.SUCCESS.getValue().equals(taskEntity.getImageStatus())) {
            return ResponseDTO.userErrorParam("请先成功生成图片");
        }
        if (AiMerchTaskStatusEnum.PROCESSING.getValue().equals(taskEntity.getVideoStatus())) {
            return ResponseDTO.userErrorParam("视频正在生成中");
        }
        if (AiMerchTaskStatusEnum.SUCCESS.getValue().equals(taskEntity.getVideoStatus())) {
            return ResponseDTO.userErrorParam("视频已生成完成");
        }

        FileVO imageFile = aiMerchFileService.getFileByKey(taskEntity.getImageFileKey());
        if (imageFile == null || StringUtils.isBlank(imageFile.getFileUrl())) {
            return ResponseDTO.userErrorParam("生成图片不存在，请重新生成");
        }

        try {
            AiModelConfigEntity videoConfig = aiModelConfigService.getConfigOrThrow(taskEntity.getVideoModelId());
            assertProvider(videoConfig, "VOLCENGINE", "视频模型");
            VolcengineMediaClient.SubmitResult submitResult = volcengineMediaClient.submitVideo(videoConfig, taskEntity.getGeneratedPrompt(), imageFile.getFileUrl());
            taskEntity.setVideoTaskId(submitResult.getTaskId());
            taskEntity.setVideoResultUrl(submitResult.getMediaUrl());
            taskEntity.setVideoErrorMsg(null);

            if (Boolean.TRUE.equals(submitResult.getCompleted()) && StringUtils.isNotBlank(submitResult.getMediaUrl())) {
                saveMediaFile(taskEntity, false, submitResult.getMediaUrl());
            } else if (StringUtils.isNotBlank(submitResult.getTaskId())) {
                taskEntity.setVideoStatus(AiMerchTaskStatusEnum.PROCESSING.getValue());
            } else {
                taskEntity.setVideoStatus(AiMerchTaskStatusEnum.FAILED.getValue());
                taskEntity.setVideoErrorMsg("视频生成未返回任务号或结果地址");
            }
        } catch (Exception e) {
            taskEntity.setVideoStatus(AiMerchTaskStatusEnum.FAILED.getValue());
            taskEntity.setVideoErrorMsg(truncateError(e.getMessage()));
        }

        aiMerchTaskDao.updateById(taskEntity);
        return ResponseDTO.ok(toTaskVO(taskEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<AiMerchTaskVO> getDetail(Long taskId) {
        AiMerchTaskEntity taskEntity = getTask(taskId);
        if (taskEntity == null) {
            return ResponseDTO.userErrorParam("任务不存在");
        }
        refreshTaskStatus(taskEntity);
        return ResponseDTO.ok(toTaskVO(taskEntity));
    }

    public PageResult<AiMerchTaskVO> queryPage(AiMerchTaskQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiMerchTaskVO> list = aiMerchTaskDao.queryPage(page, queryForm);
        list.forEach(this::fillTaskFileInfo);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    private void refreshTaskStatus(AiMerchTaskEntity taskEntity) {
        boolean changed = false;

        if (AiMerchTaskStatusEnum.PROCESSING.getValue().equals(taskEntity.getImageStatus())
                && StringUtils.isNotBlank(taskEntity.getImageTaskId())
                && StringUtils.isBlank(taskEntity.getImageFileKey())) {
            try {
                AiModelConfigEntity imageConfig = aiModelConfigService.getConfigOrThrow(taskEntity.getImageModelId());
                assertProvider(imageConfig, "VOLCENGINE", "图片模型");
                VolcengineMediaClient.QueryResult queryResult = volcengineMediaClient.queryTask(imageConfig, taskEntity.getImageTaskId());
                if (Boolean.TRUE.equals(queryResult.getCompleted()) && StringUtils.isNotBlank(queryResult.getMediaUrl())) {
                    saveMediaFile(taskEntity, true, queryResult.getMediaUrl());
                    changed = true;
                } else if (Boolean.TRUE.equals(queryResult.getFailed())) {
                    taskEntity.setImageStatus(AiMerchTaskStatusEnum.FAILED.getValue());
                    taskEntity.setImageErrorMsg(truncateError(queryResult.getErrorMessage()));
                    changed = true;
                }
            } catch (IllegalArgumentException e) {
                taskEntity.setImageStatus(AiMerchTaskStatusEnum.FAILED.getValue());
                taskEntity.setImageErrorMsg(truncateError(e.getMessage()));
                changed = true;
            } catch (Exception e) {
                log.warn("刷新图片任务失败, taskId={}, error={}", taskEntity.getTaskId(), e.getMessage());
            }
        }

        if (AiMerchTaskStatusEnum.PROCESSING.getValue().equals(taskEntity.getVideoStatus())
                && StringUtils.isNotBlank(taskEntity.getVideoTaskId())
                && StringUtils.isBlank(taskEntity.getVideoFileKey())) {
            try {
                AiModelConfigEntity videoConfig = aiModelConfigService.getConfigOrThrow(taskEntity.getVideoModelId());
                assertProvider(videoConfig, "VOLCENGINE", "视频模型");
                VolcengineMediaClient.QueryResult queryResult = volcengineMediaClient.queryTask(videoConfig, taskEntity.getVideoTaskId());
                if (Boolean.TRUE.equals(queryResult.getCompleted()) && StringUtils.isNotBlank(queryResult.getMediaUrl())) {
                    saveMediaFile(taskEntity, false, queryResult.getMediaUrl());
                    changed = true;
                } else if (Boolean.TRUE.equals(queryResult.getFailed())) {
                    taskEntity.setVideoStatus(AiMerchTaskStatusEnum.FAILED.getValue());
                    taskEntity.setVideoErrorMsg(truncateError(queryResult.getErrorMessage()));
                    changed = true;
                }
            } catch (IllegalArgumentException e) {
                taskEntity.setVideoStatus(AiMerchTaskStatusEnum.FAILED.getValue());
                taskEntity.setVideoErrorMsg(truncateError(e.getMessage()));
                changed = true;
            } catch (Exception e) {
                log.warn("刷新视频任务失败, taskId={}, error={}", taskEntity.getTaskId(), e.getMessage());
            }
        }

        if (changed) {
            aiMerchTaskDao.updateById(taskEntity);
        }
    }

    private void saveMediaFile(AiMerchTaskEntity taskEntity, boolean image, String mediaUrl) {
        FileUploadVO uploadVO = aiMerchFileService.importRemoteFile(
                mediaUrl,
                FileFolderTypeEnum.AI_MERCH.getValue(),
                (image ? "ai-merch-image-" : "ai-merch-video-") + taskEntity.getTaskId()
        );
        if (image) {
            taskEntity.setImageStatus(AiMerchTaskStatusEnum.SUCCESS.getValue());
            taskEntity.setImageResultUrl(mediaUrl);
            taskEntity.setImageFileId(uploadVO.getFileId());
            taskEntity.setImageFileKey(uploadVO.getFileKey());
            taskEntity.setImageErrorMsg(null);
        } else {
            taskEntity.setVideoStatus(AiMerchTaskStatusEnum.SUCCESS.getValue());
            taskEntity.setVideoResultUrl(mediaUrl);
            taskEntity.setVideoFileId(uploadVO.getFileId());
            taskEntity.setVideoFileKey(uploadVO.getFileKey());
            taskEntity.setVideoErrorMsg(null);
        }
    }

    private AiMerchTaskEntity getTask(Long taskId) {
        AiMerchTaskEntity taskEntity = aiMerchTaskDao.selectById(taskId);
        return taskEntity == null || Integer.valueOf(1).equals(taskEntity.getDeletedFlag()) ? null : taskEntity;
    }

    private AiMerchTaskVO toTaskVO(AiMerchTaskEntity taskEntity) {
        AiMerchTaskVO taskVO = SmartBeanUtil.copy(taskEntity, AiMerchTaskVO.class);
        fillTaskFileInfo(taskVO);
        return taskVO;
    }

    private void fillTaskFileInfo(AiMerchTaskVO taskVO) {
        FileVO sourceFile = aiMerchFileService.getFileByKey(taskVO.getSourceFileKey());
        if (sourceFile != null) {
            taskVO.setSourceFileId(sourceFile.getFileId());
            taskVO.setSourceFileName(sourceFile.getFileName());
            taskVO.setSourceFileType(sourceFile.getFileType());
            taskVO.setSourceFileUrl(sourceFile.getFileUrl());
        }

        FileVO imageFile = aiMerchFileService.getFileByKey(taskVO.getImageFileKey());
        if (imageFile != null) {
            taskVO.setImageFileId(imageFile.getFileId());
            taskVO.setImageFileName(imageFile.getFileName());
            taskVO.setImageFileType(imageFile.getFileType());
            taskVO.setImageFileUrl(imageFile.getFileUrl());
        }

        FileVO videoFile = aiMerchFileService.getFileByKey(taskVO.getVideoFileKey());
        if (videoFile != null) {
            taskVO.setVideoFileId(videoFile.getFileId());
            taskVO.setVideoFileName(videoFile.getFileName());
            taskVO.setVideoFileType(videoFile.getFileType());
            taskVO.setVideoFileUrl(videoFile.getFileUrl());
        }
    }

    private boolean isImageFile(String fileType) {
        return StringUtils.equalsAnyIgnoreCase(fileType, "jpg", "jpeg", "png", "gif", "bmp", "webp");
    }

    private String truncateError(String message) {
        if (StringUtils.isBlank(message)) {
            return "未知错误";
        }
        return message.length() > 500 ? message.substring(0, 500) : message;
    }

    private void assertProvider(AiModelConfigEntity config, String provider, String modelLabel) {
        if (config == null || !StringUtils.equalsIgnoreCase(config.getProvider(), provider)) {
            throw new IllegalArgumentException(modelLabel + "不支持当前厂商");
        }
    }
}
