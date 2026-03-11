-- 1) AI用户额度账户（一人一户，绑定 t_employee.employee_id）
CREATE TABLE `t_ai_user_account`
(
    `employee_id`   bigint   NOT NULL COMMENT '关联 t_employee.employee_id',
    `quota_remain`  bigint   NOT NULL DEFAULT '0' COMMENT '剩余额度',
    `quota_used`    bigint   NOT NULL DEFAULT '0' COMMENT '已用额度',
    `disabled_flag` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '0否1是',
    `update_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`employee_id`) USING BTREE,
    KEY             `idx_quota_remain` (`quota_remain`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI用户额度账户';


-- 2) API Token（客户端调用 /v1/* 用）
CREATE TABLE `t_ai_token`
(
    `token_id`       bigint                                                       NOT NULL AUTO_INCREMENT,
    `employee_id`    bigint                                                       NOT NULL COMMENT '归属员工',
    `token_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `token_prefix`   varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '展示前缀',
    `token_hash`     char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL COMMENT 'SHA256',
    `expires_time`   datetime                                                              DEFAULT NULL,
    `last_used_time` datetime                                                              DEFAULT NULL,
    `request_count`  bigint unsigned NOT NULL DEFAULT '0',
    `disabled_flag`  tinyint unsigned NOT NULL DEFAULT '0',
    `deleted_flag`   tinyint unsigned NOT NULL DEFAULT '0',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL,
    `update_time`    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time`    datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`token_id`) USING BTREE,
    UNIQUE KEY `uk_token_hash` (`token_hash`) USING BTREE,
    UNIQUE KEY `uk_employee_token_name` (`employee_id`,`token_name`) USING BTREE,
    KEY              `idx_employee_status` (`employee_id`,`disabled_flag`,`deleted_flag`) USING BTREE,
    KEY              `idx_expires_time` (`expires_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI访问Token';


-- 3) 渠道配置（先仅 OpenAI 规范）
CREATE TABLE `t_ai_channel`
(
    `channel_id`     bigint                                                        NOT NULL AUTO_INCREMENT,
    `channel_name`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `provider_type`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'OPENAI',
    `base_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'https://api.openai.com',
    `api_key_cipher` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密存储',
    `api_key_kid`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'local-v1',
    `api_key_mask`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci           DEFAULT NULL,
    `organization`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `priority`       int                                                           NOT NULL DEFAULT '0',
    `weight`         int                                                           NOT NULL DEFAULT '100',
    `timeout_ms`     int                                                           NOT NULL DEFAULT '60000',
    `max_retries`    tinyint unsigned NOT NULL DEFAULT '1',
    `disabled_flag`  tinyint unsigned NOT NULL DEFAULT '0',
    `deleted_flag`   tinyint unsigned NOT NULL DEFAULT '0',
    `created_by`     bigint                                                                 DEFAULT NULL COMMENT 't_employee.employee_id',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`channel_id`) USING BTREE,
    UNIQUE KEY `uk_channel_name` (`channel_name`) USING BTREE,
    KEY              `idx_route` (`disabled_flag`,`priority`,`weight`,`channel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI渠道配置';


-- 4) 模型配置（输入/输出分价）
CREATE TABLE `t_ai_model`
(
    `model_id`            bigint                                                        NOT NULL AUTO_INCREMENT,
    `model_name`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `endpoint_type`       tinyint unsigned NOT NULL DEFAULT '3' COMMENT '1 chat, 2 embedding, 3 both',
    `input_price_per_1k`  decimal(18, 8)                                                NOT NULL DEFAULT '0.00000000',
    `output_price_per_1k` decimal(18, 8)                                                NOT NULL DEFAULT '0.00000000',
    `disabled_flag`       tinyint unsigned NOT NULL DEFAULT '0',
    `deleted_flag`        tinyint unsigned NOT NULL DEFAULT '0',
    `created_by`          bigint                                                                 DEFAULT NULL COMMENT 't_employee.employee_id',
    `remark`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL,
    `update_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`model_id`) USING BTREE,
    UNIQUE KEY `uk_model_name` (`model_name`) USING BTREE,
    KEY                   `idx_model_status_endpoint` (`disabled_flag`,`endpoint_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI模型配置';


-- 5) 渠道-模型绑定
CREATE TABLE `t_ai_channel_model`
(
    `id`            bigint   NOT NULL AUTO_INCREMENT,
    `channel_id`    bigint   NOT NULL,
    `model_id`      bigint   NOT NULL,
    `disabled_flag` tinyint unsigned NOT NULL DEFAULT '0',
    `update_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_channel_model` (`channel_id`,`model_id`) USING BTREE,
    KEY             `idx_model_channel` (`model_id`,`disabled_flag`,`channel_id`) USING BTREE,
    KEY             `idx_channel` (`channel_id`,`disabled_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='渠道模型绑定';


-- 6) 计费会话（预扣/结算/退款状态机）
CREATE TABLE `t_ai_billing_session`
(
    `billing_session_id`   bigint                                                    NOT NULL AUTO_INCREMENT,
    `request_id`           char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `employee_id`          bigint                                                    NOT NULL,
    `token_id`             bigint                                                    NOT NULL,
    `channel_id`           bigint                                                    NOT NULL,
    `model_id`             bigint                                                    NOT NULL,
    `endpoint_type`        tinyint unsigned NOT NULL COMMENT '1 chat, 2 embedding',
    `preconsume_quota`     bigint                                                    NOT NULL DEFAULT '0',
    `settled_quota`        bigint                                                    NOT NULL DEFAULT '0',
    `delta_quota`          bigint                                                    NOT NULL DEFAULT '0',
    `billing_state`        tinyint unsigned NOT NULL DEFAULT '0' COMMENT '0预扣,1结算,2已退款,3失败',
    `upstream_status_code` int                                                                DEFAULT NULL,
    `gateway_status_code`  int                                                                DEFAULT NULL,
    `latency_ms`           int unsigned DEFAULT NULL,
    `error_msg`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      DEFAULT NULL,
    `started_time`         datetime                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `settled_time`         datetime                                                           DEFAULT NULL,
    `update_time`          datetime                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time`          datetime                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`billing_session_id`) USING BTREE,
    UNIQUE KEY `uk_request_id` (`request_id`) USING BTREE,
    KEY                    `idx_employee_time` (`employee_id`,`create_time`) USING BTREE,
    KEY                    `idx_token_time` (`token_id`,`create_time`) USING BTREE,
    KEY                    `idx_state_time` (`billing_state`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='计费会话';


-- 7) 额度流水（审计）
CREATE TABLE `t_ai_quota_ledger`
(
    `ledger_id`          bigint   NOT NULL AUTO_INCREMENT,
    `employee_id`        bigint   NOT NULL,
    `token_id`           bigint                                                        DEFAULT NULL,
    `billing_session_id` bigint                                                        DEFAULT NULL,
    `request_id`         char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `action_type`        tinyint unsigned NOT NULL COMMENT '1充值,2预扣,3结算补扣,4结算返还,5回滚',
    `delta_quota`        bigint   NOT NULL,
    `balance_after`      bigint   NOT NULL,
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `create_time`        datetime NOT NULL                                             DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ledger_id`) USING BTREE,
    KEY                  `idx_employee_time` (`employee_id`,`create_time`) USING BTREE,
    KEY                  `idx_request_id` (`request_id`) USING BTREE,
    KEY                  `idx_billing_session` (`billing_session_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='额度流水';


-- 8) 用量日志（对账+排障）
CREATE TABLE `t_ai_usage_log`
(
    `usage_log_id`         bigint                                                    NOT NULL AUTO_INCREMENT,
    `request_id`           char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `billing_session_id`   bigint                                                             DEFAULT NULL,
    `employee_id`          bigint                                                    NOT NULL,
    `token_id`             bigint                                                    NOT NULL,
    `channel_id`           bigint                                                    NOT NULL,
    `model_id`             bigint                                                    NOT NULL,
    `endpoint_type`        tinyint unsigned NOT NULL COMMENT '1 chat, 2 embedding',
    `stream_flag`          tinyint unsigned NOT NULL DEFAULT '0',
    `prompt_tokens`        int unsigned NOT NULL DEFAULT '0',
    `completion_tokens`    int unsigned NOT NULL DEFAULT '0',
    `total_tokens`         int unsigned NOT NULL DEFAULT '0',
    `input_cost`           decimal(18, 8)                                            NOT NULL DEFAULT '0.00000000',
    `output_cost`          decimal(18, 8)                                            NOT NULL DEFAULT '0.00000000',
    `total_cost`           decimal(18, 8)                                            NOT NULL DEFAULT '0.00000000',
    `preconsume_quota`     bigint                                                    NOT NULL DEFAULT '0',
    `settled_quota`        bigint                                                    NOT NULL DEFAULT '0',
    `settle_delta_quota`   bigint                                                    NOT NULL DEFAULT '0',
    `upstream_status_code` int                                                                DEFAULT NULL,
    `gateway_status_code`  int                                                                DEFAULT NULL,
    `latency_ms`           int unsigned DEFAULT NULL,
    `error_code`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       DEFAULT NULL,
    `error_message`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      DEFAULT NULL,
    `create_time`          datetime                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`usage_log_id`) USING BTREE,
    UNIQUE KEY `uk_usage_request_id` (`request_id`) USING BTREE,
    UNIQUE KEY `uk_usage_billing_session` (`billing_session_id`) USING BTREE,
    KEY                    `idx_employee_time` (`employee_id`,`create_time`) USING BTREE,
    KEY                    `idx_token_time` (`token_id`,`create_time`) USING BTREE,
    KEY                    `idx_model_time` (`model_id`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用量日志';