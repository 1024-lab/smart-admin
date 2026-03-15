/**
 * Relay 模块枚举常量
 */

export const ENDPOINT_TYPE_ENUM = {
  CHAT:      { value: 1, desc: 'Chat' },
  EMBEDDING: { value: 2, desc: 'Embedding' },
  BOTH:      { value: 3, desc: 'Chat+Embedding' },
};

export const PROVIDER_TYPE_ENUM = {
  OPENAI:   { value: 'OPENAI',   desc: 'OpenAI' },
  AZURE:    { value: 'AZURE',    desc: 'Azure OpenAI' },
  CLAUDE:   { value: 'CLAUDE',   desc: 'Anthropic Claude' },
  GEMINI:   { value: 'GEMINI',   desc: 'Google Gemini' },
  DEEPSEEK: { value: 'DEEPSEEK', desc: 'DeepSeek' },
};

export const BILLING_STATE_ENUM = {
  PRECONSUMED: { value: 0, desc: '预扣中' },
  SETTLED:     { value: 1, desc: '已结算' },
  REFUNDED:    { value: 2, desc: '已退款' },
  FAILED:      { value: 3, desc: '结算失败' },
};

export const QUOTA_ACTION_TYPE_ENUM = {
  RECHARGE:      { value: 1, desc: '充值' },
  PRECONSUME:    { value: 2, desc: '预扣' },
  SETTLE_EXTRA:  { value: 3, desc: '结算补扣' },
  SETTLE_REFUND: { value: 4, desc: '结算返还' },
  ROLLBACK:      { value: 5, desc: '回滚' },
};

export default {
  ENDPOINT_TYPE_ENUM,
  PROVIDER_TYPE_ENUM,
  BILLING_STATE_ENUM,
  QUOTA_ACTION_TYPE_ENUM,
};
