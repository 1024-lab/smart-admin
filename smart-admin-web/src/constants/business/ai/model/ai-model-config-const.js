/**
 * 模型配置表 枚举
 *
 * @Author:    Mission
 * @Date:      2026-01-28 09:59:38
 * @Copyright  Mission
 */

export const MODEL_TYPE_LIST = [
  { value: 'chat', label: '文本对话 (chat)' },
  { value: 'embedding', label: '文本向量 (embedding)' },
  { value: 'image_gen', label: '图片生成 (image_gen)' },
  { value: 'video_gen', label: '视频生成 (video_gen)' },
  { value: 'rerank', label: '文本重排 (rerank)' },
  { value: 'tts', label: '文字转语音 (tts)' },
  { value: 'stt', label: '语音转文字 (stt)' },
];

export const MODEL_FEATURE_LIST = [
  { value: 'VISION', label: '视觉输入 (VISION)' },
  { value: 'FUNCTION_CALL', label: '函数调用 (FUNCTION_CALL)' },
  { value: 'STREAMING', label: '流式输出 (STREAMING)' },
];

export default {
  MODEL_TYPE_LIST,
  MODEL_FEATURE_LIST,
};
