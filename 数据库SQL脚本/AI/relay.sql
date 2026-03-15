-- =============================================================================
-- AI Relay 网关数据库表结构
-- 说明：为 AI API 网关提供用户账户、Token 鉴权、上游渠道管理、
--       计费结算及用量审计等完整支撑能力。
-- 依赖：t_employee（员工表，提供 employee_id 主键）
-- =============================================================================


-- =============================================================================
-- 1) AI用户额度账户
-- 说明：每位员工对应唯一一条账户记录，以 employee_id 为主键。
--       记录该员工当前剩余可用额度与历史累计消耗额度。
--       额度单位：整型（对应计费最小单位，如 0.0001 元 = 1 quota）。
--       disabled_flag 为 1 时该账户被冻结，所有请求应被拒绝。
-- =============================================================================
CREATE TABLE `t_ai_user_account`
(
    `employee_id`   bigint          NOT NULL COMMENT '关联 t_employee.employee_id，一人一户，业务主键',
    `quota_remain`  bigint          NOT NULL DEFAULT '0' COMMENT '当前剩余可用额度（单位：quota）',
    `quota_used`    bigint          NOT NULL DEFAULT '0' COMMENT '历史累计已消耗额度（单位：quota）',
    `disabled_flag` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '账户冻结标记：0=正常，1=已冻结',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间（由 MySQL 自动维护）',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
    PRIMARY KEY (`employee_id`) USING BTREE,
    KEY `idx_quota_remain` (`quota_remain`) USING BTREE COMMENT '辅助查询低余额/零余额账户（预警/排查用）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI用户额度账户（一人一户）';


-- =============================================================================
-- 2) API Token
-- 说明：客户端调用 /v1/* 接口时携带的鉴权凭证。
--       一名员工可持有多个 Token（按项目/场景隔离）。
--       token_hash 存储原始 Token 的 SHA-256 哈希，明文不落库。
--       token_prefix 用于在管理界面友好展示 Token 前几位（如 sk-abc...）。
--       expires_time 为 NULL 表示永不过期；过期后请求应被拒绝。
--       deleted_flag 为软删除标记，不物理删除记录以保留审计链。
-- =============================================================================
CREATE TABLE `t_ai_token`
(
    `token_id`       bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `employee_id`    bigint                                                       NOT NULL COMMENT '归属员工 ID，关联 t_employee.employee_id',
    `token_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Token 名称（同一员工下唯一，便于区分不同用途）',
    `token_prefix`   varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Token 展示前缀（明文，仅用于 UI 展示，不参与鉴权）',
    `token_hash`     char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL COMMENT '原始 Token 的 SHA-256 哈希值，用于鉴权比对，明文不落库',
    `expires_time`   datetime                                                              DEFAULT NULL COMMENT 'Token 过期时间；NULL 表示永不过期',
    `last_used_time` datetime                                                              DEFAULT NULL COMMENT '最近一次鉴权成功时间（用于活跃度统计与安全审计）',
    `request_count`  bigint unsigned                                              NOT NULL DEFAULT '0' COMMENT '累计使用次数（鉴权通过即计数）',
    `disabled_flag`  tinyint unsigned                                             NOT NULL DEFAULT '0' COMMENT '禁用标记：0=启用，1=已禁用',
    `deleted_flag`   tinyint unsigned                                             NOT NULL DEFAULT '0' COMMENT '软删除标记：0=正常，1=已删除',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '备注（用途说明等）',
    `update_time`    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `create_time`    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`token_id`) USING BTREE,
    UNIQUE KEY `uk_token_hash` (`token_hash`) USING BTREE COMMENT '哈希全局唯一，防止碰撞及重复录入',
    UNIQUE KEY `uk_employee_token_name` (`employee_id`, `token_name`) USING BTREE COMMENT '同一员工下 Token 名称唯一',
    KEY `idx_employee_status` (`employee_id`, `disabled_flag`, `deleted_flag`) USING BTREE COMMENT '按员工查询有效 Token 列表',
    KEY `idx_expires_time` (`expires_time`) USING BTREE COMMENT '定时清理/过期扫描用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI访问Token（客户端调用 /v1/* 接口的鉴权凭证）';


-- =============================================================================
-- 3) 渠道配置
-- 说明：管理所有上游 AI 服务提供商的接入配置（当前以 OpenAI 协议为主）。
--       api_key_cipher 存储加密后的 API Key，配合 api_key_kid 标识所用加密版本/密钥。
--       api_key_mask 为脱敏展示字段（如 sk-****1234），仅用于 UI。
--       priority 越小优先级越高；weight 用于同优先级下的加权随机路由。
--       timeout_ms 为单次请求超时时间（毫秒）；max_retries 为失败后最大重试次数。
-- =============================================================================
CREATE TABLE `t_ai_channel`
(
    `channel_id`     bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `channel_name`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称（全局唯一，便于管理识别）',
    `provider_type`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'OPENAI' COMMENT '上游服务商类型（如 OPENAI / AZURE / CLAUDE 等）',
    `base_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'https://api.openai.com' COMMENT '上游 API 基础 URL',
    `api_key_cipher` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '加密存储的 API Key 密文（对称加密，防止明文泄露）',
    `api_key_kid`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'local-v1' COMMENT '加密密钥 ID / 版本标识，用于密钥轮换时定位解密密钥',
    `api_key_mask`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL COMMENT 'API Key 脱敏展示（如 sk-****abcd），仅用于 UI 展示',
    `organization`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT 'OpenAI Organization ID（多组织账号时指定归属组织）',
    `priority`       int                                                           NOT NULL DEFAULT '0' COMMENT '路由优先级：数值越小优先级越高，相同优先级内按 weight 加权随机',
    `weight`         int                                                           NOT NULL DEFAULT '100' COMMENT '负载均衡权重（同优先级渠道间按此值做加权随机选取）',
    `timeout_ms`     int                                                           NOT NULL DEFAULT '60000' COMMENT '单次上游请求超时时间（毫秒），默认 60 秒',
    `max_retries`    tinyint unsigned                                              NOT NULL DEFAULT '1' COMMENT '失败后最大重试次数（0=不重试）',
    `disabled_flag`  tinyint unsigned                                              NOT NULL DEFAULT '0' COMMENT '禁用标记：0=启用，1=已禁用（禁用后不参与路由）',
    `deleted_flag`   tinyint unsigned                                              NOT NULL DEFAULT '0' COMMENT '软删除标记：0=正常，1=已删除',
    `created_by`     bigint                                                                 DEFAULT NULL COMMENT '创建人员工 ID，关联 t_employee.employee_id',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注（如用途、限速说明等）',
    `update_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `create_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`channel_id`) USING BTREE,
    UNIQUE KEY `uk_channel_name` (`channel_name`) USING BTREE COMMENT '渠道名称全局唯一',
    KEY `idx_route` (`disabled_flag`, `priority`, `weight`, `channel_id`) USING BTREE COMMENT '路由选取联合索引：过滤禁用 → 按优先级 → 按权重 → 按 ID 稳定排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI渠道配置（上游服务商接入管理）';


-- =============================================================================
-- 4) 模型配置
-- 说明：定义各 AI 模型的基本信息及计费价格。
--       endpoint_type 标识模型支持的能力类型：
--         1 = 仅支持 Chat Completion
--         2 = 仅支持 Embedding
--         3 = Chat + Embedding 均支持
--       input_price_per_1k / output_price_per_1k 分别为每 1000 token 的单价（元）。
--       价格精度 18位小数，支持极小单价（如 0.000001 元/1k token）。
-- =============================================================================
CREATE TABLE `t_ai_model`
(
    `model_id`            bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `model_name`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型标识名称（全局唯一，如 gpt-4o、text-embedding-3-small）',
    `endpoint_type`       tinyint unsigned                                              NOT NULL DEFAULT '3' COMMENT '端点类型：1=仅 Chat，2=仅 Embedding，3=Chat+Embedding 均支持',
    `input_price_per_k`  decimal(18, 8)                                                NOT NULL DEFAULT '0.00000000' COMMENT '输入（Prompt）Token 单价，每 1000 token 计费金额（元）',
    `output_price_per_k` decimal(18, 8)                                                NOT NULL DEFAULT '0.00000000' COMMENT '输出（Completion）Token 单价，每 1000 token 计费金额（元）',
    `disabled_flag`       tinyint unsigned                                              NOT NULL DEFAULT '0' COMMENT '禁用标记：0=启用，1=已禁用（禁用后不可被路由选取）',
    `deleted_flag`        tinyint unsigned                                              NOT NULL DEFAULT '0' COMMENT '软删除标记：0=正常，1=已删除',
    `created_by`          bigint                                                                 DEFAULT NULL COMMENT '创建人员工 ID，关联 t_employee.employee_id',
    `remark`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '备注（如上下文窗口大小、用途说明等）',
    `update_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`model_id`) USING BTREE,
    UNIQUE KEY `uk_model_name` (`model_name`) USING BTREE COMMENT '模型名称全局唯一',
    KEY `idx_model_status_endpoint` (`disabled_flag`, `endpoint_type`) USING BTREE COMMENT '路由查询：按启用状态+端点类型快速过滤可用模型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI模型配置（含输入/输出分价）';


-- =============================================================================
-- 5) 渠道-模型绑定
-- 说明：维护渠道与模型的多对多关系，控制哪个渠道支持哪些模型的路由。
--       路由时需同时满足：渠道启用 + 绑定关系启用 + 模型启用。
--       disabled_flag 可单独禁用某渠道的某个模型，而无需修改渠道或模型本身。
-- =============================================================================
CREATE TABLE `t_ai_channel_model`
(
    `id`            bigint          NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `channel_id`    bigint          NOT NULL COMMENT '渠道 ID，关联 t_ai_channel.channel_id',
    `model_id`      bigint          NOT NULL COMMENT '模型 ID，关联 t_ai_model.model_id',
    `disabled_flag` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '绑定关系禁用标记：0=启用，1=禁用（可单独屏蔽某渠道的某模型）',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_channel_model` (`channel_id`, `model_id`) USING BTREE COMMENT '渠道+模型组合唯一，防止重复绑定',
    KEY `idx_model_channel` (`model_id`, `disabled_flag`, `channel_id`) USING BTREE COMMENT '按模型查询可用渠道列表（路由核心索引）',
    KEY `idx_channel` (`channel_id`, `disabled_flag`) USING BTREE COMMENT '按渠道查询其绑定的有效模型列表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='渠道-模型绑定关系（多对多，控制路由范围）';


-- =============================================================================
-- 6) 计费会话
-- 说明：每次 AI 请求对应一条计费会话，记录完整的预扣→结算→退款状态机流转。
--       生命周期：
--         0（预扣）→ 1（结算）：请求成功，按实际用量结算
--         0（预扣）→ 2（已退款）：请求异常，预扣额度已退回账户
--         0（预扣）→ 3（失败）：结算失败（需人工介入或补偿任务处理）
--       preconsume_quota：请求发起时预估扣除的额度（按最大可能用量估算）。
--       settled_quota：最终实际消耗的额度（精确到 token 级别）。
--       delta_quota：结算时与预扣的差值，正值=补扣，负值=返还。
--       upstream_status_code：上游 AI 服务返回的 HTTP 状态码。
--       gateway_status_code：本网关向客户端返回的 HTTP 状态码。
--       latency_ms：从网关收到请求到上游响应完成的端到端耗时（毫秒）。
-- =============================================================================
CREATE TABLE `t_ai_billing_session`
(
    `billing_session_id`   bigint          NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `request_id`           char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求唯一标识（UUID v4），全局唯一，关联所有相关日志',
    `employee_id`          bigint          NOT NULL COMMENT '发起请求的员工 ID，关联 t_employee.employee_id',
    `token_id`             bigint          NOT NULL COMMENT '鉴权使用的 Token ID，关联 t_ai_token.token_id',
    `channel_id`           bigint          NOT NULL COMMENT '路由选取的上游渠道 ID，关联 t_ai_channel.channel_id',
    `model_id`             bigint          NOT NULL COMMENT '请求使用的模型 ID，关联 t_ai_model.model_id',
    `endpoint_type`        tinyint unsigned NOT NULL COMMENT '端点类型：1=Chat，2=Embedding（与模型能力对应）',
    `preconsume_quota`     bigint          NOT NULL DEFAULT '0' COMMENT '预扣额度：请求发起时按估算值从账户扣除（单位：quota）',
    `settled_quota`        bigint          NOT NULL DEFAULT '0' COMMENT '结算额度：请求完成后按实际 token 用量计算的最终消耗（单位：quota）',
    `delta_quota`          bigint          NOT NULL DEFAULT '0' COMMENT '结算差额：settled_quota - preconsume_quota，正值补扣，负值返还',
    `billing_state`        tinyint unsigned NOT NULL DEFAULT '0' COMMENT '计费状态：0=预扣中，1=已结算，2=已退款（异常退回），3=结算失败',
    `upstream_status_code` int                      DEFAULT NULL COMMENT '上游 AI 服务返回的 HTTP 状态码（如 200、429、500 等）',
    `gateway_status_code`  int                      DEFAULT NULL COMMENT '网关向客户端返回的 HTTP 状态码',
    `latency_ms`           int unsigned             DEFAULT NULL COMMENT '端到端请求耗时（毫秒），从网关接收请求至上游响应完成',
    `error_msg`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常信息（仅在 billing_state=2/3 时填写）',
    `started_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求开始时间（预扣发生时刻）',
    `settled_time`         datetime                 DEFAULT NULL COMMENT '结算完成时间（billing_state 变为 1 的时刻）',
    `update_time`          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `create_time`          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`billing_session_id`) USING BTREE,
    UNIQUE KEY `uk_request_id` (`request_id`) USING BTREE COMMENT 'request_id 全局唯一，防止重复计费',
    KEY `idx_employee_time` (`employee_id`, `create_time`) USING BTREE COMMENT '按员工查询计费历史（分页列表用）',
    KEY `idx_token_time` (`token_id`, `create_time`) USING BTREE COMMENT '按 Token 查询调用记录',
    KEY `idx_state_time` (`billing_state`, `create_time`) USING BTREE COMMENT '按状态查询待处理/异常会话（补偿任务扫描用）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='计费会话（预扣→结算→退款状态机）';


-- =============================================================================
-- 7) 额度流水
-- 说明：额度账户的完整变更流水，每次账户余额变动必须写入一条记录，用于审计与对账。
--       action_type 枚举：
--         1 = 充值（管理员手动充值或订单到账）
--         2 = 预扣（请求发起时扣除预估额度）
--         3 = 结算补扣（实际用量 > 预扣，追加扣除差额）
--         4 = 结算返还（实际用量 < 预扣，退回多扣额度）
--         5 = 回滚（预扣失败或请求异常，全额退回预扣额度）
--       delta_quota：本次变动量，正值=余额增加（充值/返还），负值=余额减少（预扣/补扣）。
--       balance_after：操作完成后的账户余额快照（便于审计，无需重放所有流水）。
-- =============================================================================
CREATE TABLE `t_ai_quota_ledger`
(
    `ledger_id`          bigint          NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `employee_id`        bigint          NOT NULL COMMENT '员工 ID，关联 t_employee.employee_id',
    `token_id`           bigint                   DEFAULT NULL COMMENT '触发本次变动的 Token ID（充值类为空）',
    `billing_session_id` bigint                   DEFAULT NULL COMMENT '关联的计费会话 ID（充值类为空）',
    `request_id`         char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的请求 ID（充值类为空，便于跨表追溯）',
    `action_type`        tinyint unsigned NOT NULL COMMENT '变动类型：1=充值，2=预扣，3=结算补扣，4=结算返还，5=回滚',
    `delta_quota`        bigint          NOT NULL COMMENT '额度变动量（正值=余额增加，负值=余额减少，单位：quota）',
    `balance_after`      bigint          NOT NULL COMMENT '操作后账户余额快照（单位：quota，用于快速核对，无需重放流水）',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注（如充值来源、操作人等）',
    `create_time`        datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '流水创建时间（不可修改，审计用）',
    PRIMARY KEY (`ledger_id`) USING BTREE,
    KEY `idx_employee_time` (`employee_id`, `create_time`) USING BTREE COMMENT '按员工查询余额变动历史（账单页用）',
    KEY `idx_request_id` (`request_id`) USING BTREE COMMENT '按请求 ID 查询关联流水（排障用）',
    KEY `idx_billing_session` (`billing_session_id`) USING BTREE COMMENT '按计费会话查询关联流水（对账用）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='额度流水（账户余额变动审计日志，只追加不修改）';


-- =============================================================================
-- 8) 用量日志
-- 说明：每次 AI 请求的完整用量与计费快照，用于对账、排障与数据分析。
--       与 t_ai_billing_session 一一对应（billing_session_id 唯一）。
--       该表为追加写入，不执行 UPDATE，保证审计完整性。
--       stream_flag：标识本次请求是否为 SSE 流式响应（0=非流式，1=流式）。
--       prompt_tokens / completion_tokens / total_tokens：上游实际返回的 token 用量。
--       input_cost / output_cost / total_cost：按模型单价计算的本次请求费用（元）。
--       preconsume_quota / settled_quota：同计费会话，便于单表统计分析（冗余设计）。
--       settle_delta_quota：= settled_quota - preconsume_quota，结算差额。
--       error_code / error_message：上游或网关的错误码与错误信息（成功时为空）。
-- =============================================================================
CREATE TABLE `t_ai_usage_log`
(
    `usage_log_id`         bigint          NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `request_id`           char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求唯一标识（UUID v4），与 t_ai_billing_session.request_id 一致',
    `billing_session_id`   bigint                   DEFAULT NULL COMMENT '关联的计费会话 ID，关联 t_ai_billing_session.billing_session_id',
    `employee_id`          bigint          NOT NULL COMMENT '发起请求的员工 ID，关联 t_employee.employee_id',
    `token_id`             bigint          NOT NULL COMMENT '鉴权使用的 Token ID，关联 t_ai_token.token_id',
    `channel_id`           bigint          NOT NULL COMMENT '路由选取的上游渠道 ID，关联 t_ai_channel.channel_id',
    `model_id`             bigint          NOT NULL COMMENT '请求使用的模型 ID，关联 t_ai_model.model_id',
    `endpoint_type`        tinyint unsigned NOT NULL COMMENT '端点类型：1=Chat，2=Embedding',
    `stream_flag`          tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否为流式响应：0=非流式，1=SSE 流式',
    `prompt_tokens`        int unsigned    NOT NULL DEFAULT '0' COMMENT '输入（Prompt）Token 数量（上游实际返回值）',
    `completion_tokens`    int unsigned    NOT NULL DEFAULT '0' COMMENT '输出（Completion）Token 数量（上游实际返回值）',
    `total_tokens`         int unsigned    NOT NULL DEFAULT '0' COMMENT '总 Token 数量（= prompt_tokens + completion_tokens）',
    `input_cost`           decimal(18, 8)  NOT NULL DEFAULT '0.00000000' COMMENT '输入 Token 费用（元）= prompt_tokens / 1000 * input_price_per_1k',
    `output_cost`          decimal(18, 8)  NOT NULL DEFAULT '0.00000000' COMMENT '输出 Token 费用（元）= completion_tokens / 1000 * output_price_per_1k',
    `total_cost`           decimal(18, 8)  NOT NULL DEFAULT '0.00000000' COMMENT '本次请求总费用（元）= input_cost + output_cost',
    `preconsume_quota`     bigint          NOT NULL DEFAULT '0' COMMENT '预扣额度（冗余自计费会话，便于单表统计，单位：quota）',
    `settled_quota`        bigint          NOT NULL DEFAULT '0' COMMENT '结算额度（实际消耗，单位：quota）',
    `settle_delta_quota`   bigint          NOT NULL DEFAULT '0' COMMENT '结算差额（= settled_quota - preconsume_quota，正值补扣，负值返还）',
    `upstream_status_code` int                      DEFAULT NULL COMMENT '上游 AI 服务返回的 HTTP 状态码',
    `gateway_status_code`  int                      DEFAULT NULL COMMENT '网关向客户端返回的 HTTP 状态码',
    `latency_ms`           int unsigned             DEFAULT NULL COMMENT '端到端请求耗时（毫秒）',
    `error_code`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '错误码（成功时为空，如 rate_limit_exceeded、context_length_exceeded 等）',
    `error_message`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '错误详情描述（成功时为空）',
    `create_time`          datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志写入时间（不可修改，审计用）',
    PRIMARY KEY (`usage_log_id`) USING BTREE,
    UNIQUE KEY `uk_usage_request_id` (`request_id`) USING BTREE COMMENT 'request_id 全局唯一，防止重复写入',
    UNIQUE KEY `uk_usage_billing_session` (`billing_session_id`) USING BTREE COMMENT '与计费会话一一对应',
    KEY `idx_employee_time` (`employee_id`, `create_time`) USING BTREE COMMENT '按员工查询用量历史（个人用量报表用）',
    KEY `idx_token_time` (`token_id`, `create_time`) USING BTREE COMMENT '按 Token 查询调用记录（Token 详情页用）',
    KEY `idx_model_time` (`model_id`, `create_time`) USING BTREE COMMENT '按模型统计用量趋势（模型分析报表用）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用量日志（每次请求的完整用量与计费快照，只追加不修改）';
