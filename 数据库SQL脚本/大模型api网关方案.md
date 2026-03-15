## 简化版 AI Gateway（OpenAI-Only v1）实施计划

### 1. Summary
- 实现一个简化网关，首期只交付：`渠道配置`、`模型配置`、`token计费`。
- 对外兼容接口首期范围：`/v1/chat/completions`、`/v1/embeddings`、`/v1/models`。
- 渠道仅支持 OpenAI 规范上游，但在架构层预留 provider 扩展点。
- 计费采用**预扣 + 最终结算**，额度归属为**用户总额度**（token 仅鉴权与统计，不单独扣减额度）。

### 2. Public APIs / Interfaces / Types
- 管理端鉴权：
  - `POST /admin/auth/login`：管理员登录，返回 JWT。
- 渠道配置 API：
  - `GET /admin/channels`
  - `POST /admin/channels`
  - `PUT /admin/channels/{id}`
  - `DELETE /admin/channels/{id}`
- 模型配置 API：
  - `GET /admin/models`
  - `POST /admin/models`
  - `PUT /admin/models/{id}`
  - `DELETE /admin/models/{id}`
- 用户与 token（计费支撑）API：
  - `POST /admin/users`
  - `GET /admin/users/{id}`
  - `POST /admin/users/{id}/quota/recharge`
  - `GET /admin/users/{id}/tokens`
  - `POST /admin/users/{id}/tokens`
  - `DELETE /admin/tokens/{tokenId}`
- OpenAI 兼容 API（API Key 鉴权）：
  - `GET /v1/models`
  - `POST /v1/chat/completions`（支持流式与非流式）
  - `POST /v1/embeddings`
- 核心扩展接口（代码层）：
  - `ProviderAdapter`：`buildUpstreamRequest`、`parseUsage`、`handleError`、`supportsEndpoint`
  - `RoutingStrategy`：优先级 + 权重随机
  - `BillingEngine`：`preConsume`、`settle`、`refund`

### 3. Implementation Changes
- 技术栈与工程骨架：
  - Spring Boot 3 + Maven + MyBatis + MySQL（首版）。
  - 模块分层：`controller -> service -> repository(mapper)`，并拆分 `admin`、`relay`、`billing`、`provider` 子包。
- 数据模型（Flyway 迁移）：
  - `users`：`quota_remain`、`quota_used`、状态与审计字段。
  - `api_tokens`：`key_hash`、`key_prefix`、`user_id`、状态、过期时间、最后使用时间。
  - `channels`：`provider_type(OPENAI)`、`base_url`、`api_key_cipher`、`organization`、`priority`、`weight`、`status`。
  - `channel_models`：渠道与模型绑定关系。
  - `model_configs`：`model_name`、`input_price_per_1k`、`output_price_per_1k`、`status`。
  - `billing_sessions`：`request_id`、预扣额度、结算额度、状态（PRECONSUMED/SETTLED/REFUNDED）。
  - `usage_logs`：请求、usage、额度变化、上游状态、耗时与错误信息。
- Relay 主链路（对齐当前 Go 项目最小闭环）：
  - API Key 鉴权 -> 解析模型 -> 选渠道 -> 预扣额度 -> 转发上游 -> 解析 usage -> 结算/退款 -> 记录日志。
  - 渠道选择：只选 `enabled` 且支持目标模型的渠道；先最高优先级，再权重随机；重试时排除已失败渠道。
  - Stream 场景：透传 SSE，同时聚合文本用于 usage 缺失时估算。
- 计费规则（首版固定）：
  - 单位：内部用整数额度（避免浮点误差）。
  - 成本公式：`prompt_tokens * input_price + completion_tokens * output_price`（按每1K价格换算）。
  - 预扣估算：基于请求 token 估算 + `max_tokens/max_completion_tokens`（缺省按固定上限）计算预扣。
  - 上游无 usage 且请求成功：走估算结算。
  - 上游失败：预扣全额退款。
- 扩展预留：
  - `provider_type` 已入库，OpenAI 以外 provider 仅需新增 Adapter 与映射，不改计费/路由主流程。
  - 账单兼容接口（`/dashboard/billing/*`）明确不在首期，二期再加。

### 4. Test Plan
- 单元测试：
  - 价格换算与额度取整。
  - 预扣/结算/退款状态机与幂等。
  - 路由策略（优先级与权重）正确性。
- 集成测试（Mock 上游）：
  - `chat/completions` 非流式成功与失败。
  - `chat/completions` 流式成功、流式 usage 缺失估算。
  - `embeddings` 成功与 usage 解析。
  - 多渠道失败重试与最终错误返回。
- 安全与权限测试：
  - 管理端 JWT 校验。
  - API Key 鉴权、过期/禁用 token 行为。
- 事务一致性测试：
  - 并发请求下用户额度不出现负值/重复扣减。
  - 同一 `request_id` 重试不重复结算。

### 5. Assumptions / Defaults
- 独立仓库开发，不改当前 Go 仓库。
- 首版数据库仅 MySQL；PostgreSQL/SQLite 作为后续扩展。
- 首版仅管理 API，无管理前端页面。
- 首版渠道为单 key 模式。
- 首版不做 OpenAI 账单查询兼容接口。
- 计费主体为用户总额度；token 不做独立额度扣减。
