package net.lab1024.sa.admin.module.business.ai.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI 模型类型枚举（决定调哪个 API 端点）
 *
 * @Author Mission
 */
@Getter
@AllArgsConstructor
public enum AiModelTypeEnum {

    CHAT("chat", "文本对话"),
    EMBEDDING("embedding", "文本向量"),
    IMAGE_GEN("image_gen", "图片生成"),
    VIDEO_GEN("video_gen", "视频生成"),
    RERANK("rerank", "文本重排"),
    TTS("tts", "文字转语音"),
    STT("stt", "语音转文字");

    private final String code;
    private final String desc;
}
