/*
 Navicat Premium Dump SQL

 Source Server         : 阿里云mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : 120.24.169.111:3306
 Source Schema         : smart_admin_v3

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 17/03/2026 21:16:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_ai_billing_session
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_billing_session`;
CREATE TABLE `t_ai_billing_session`  (
  `billing_session_id` bigint NOT NULL COMMENT '自增主键',
  `request_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求唯一标识（UUID v4），全局唯一，关联所有相关日志',
  `employee_id` bigint NOT NULL COMMENT '发起请求的员工 ID，关联 t_employee.employee_id',
  `token_id` bigint NOT NULL COMMENT '鉴权使用的 Token ID，关联 t_ai_token.token_id',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '路由选取的上游渠道 ID，关联 t_ai_channel.channel_id',
  `model_id` bigint NOT NULL COMMENT '请求使用的模型 ID，关联 t_ai_model.model_id',
  `endpoint_type` tinyint UNSIGNED NOT NULL COMMENT '端点类型：1=Chat，2=Embedding（与模型能力对应）',
  `preconsume_quota` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '预扣额度：请求发起时按估算值从账户扣除（单位：quota）',
  `settled_quota` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '结算额度：请求完成后按实际 token 用量计算的最终消耗（单位：quota）',
  `delta_quota` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '结算差额：settled_quota - preconsume_quota，正值补扣，负值返还',
  `billing_state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '计费状态：0=预扣中，1=已结算，2=已退款（异常退回），3=结算失败',
  `upstream_status_code` int NULL DEFAULT NULL COMMENT '上游 AI 服务返回的 HTTP 状态码（如 200、429、500 等）',
  `gateway_status_code` int NULL DEFAULT NULL COMMENT '网关向客户端返回的 HTTP 状态码',
  `latency_ms` int UNSIGNED NULL DEFAULT NULL COMMENT '端到端请求耗时（毫秒），从网关接收请求至上游响应完成',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息（仅在 billing_state=2/3 时填写）',
  `started_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求开始时间（预扣发生时刻）',
  `settled_time` datetime NULL DEFAULT NULL COMMENT '结算完成时间（billing_state 变为 1 的时刻）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`billing_session_id`) USING BTREE,
  UNIQUE INDEX `uk_request_id`(`request_id` ASC) USING BTREE COMMENT 'request_id 全局唯一，防止重复计费',
  INDEX `idx_employee_time`(`employee_id` ASC, `create_time` ASC) USING BTREE COMMENT '按员工查询计费历史（分页列表用）',
  INDEX `idx_token_time`(`token_id` ASC, `create_time` ASC) USING BTREE COMMENT '按 Token 查询调用记录',
  INDEX `idx_state_time`(`billing_state` ASC, `create_time` ASC) USING BTREE COMMENT '按状态查询待处理/异常会话（补偿任务扫描用）'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '计费会话（预扣→结算→退款状态机）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_billing_session
-- ----------------------------
INSERT INTO `t_ai_billing_session` VALUES (2033717522052624386, '074550a5-402e-4a0f-8241-62e58fdc030a', 1, 1, NULL, 1, 1, 3.00000000, 0.00000000, 0.00000000, 0, NULL, NULL, NULL, NULL, '2026-03-17 01:30:19', NULL, '2026-03-17 01:30:19', '2026-03-17 01:30:19');
INSERT INTO `t_ai_billing_session` VALUES (2033720514323668993, '8ebf4c0a-e80a-4c14-9dd1-953c5d14633c', 1, 1, 1, 1, 1, 3.00000000, 2.00000000, 1.00000000, 1, 200, 200, 24809, NULL, '2026-03-17 01:42:12', '2026-03-17 09:42:35', '2026-03-17 01:42:12', '2026-03-17 01:42:12');
INSERT INTO `t_ai_billing_session` VALUES (2033752020702117889, '780a5471-c8ce-4c61-b76c-1ec2ca7feaa5', 1, 1, 1, 1, 1, 2.00000000, 2.00000000, 0.00000000, 1, 200, 200, 25087, NULL, '2026-03-17 03:47:24', '2026-03-17 11:47:47', '2026-03-17 03:47:24', '2026-03-17 03:47:24');
INSERT INTO `t_ai_billing_session` VALUES (2033793671126515714, '2e7f2526-13c5-43a7-a513-2ea87156c6d9', 1, 1, 1, 1, 1, 2.00000000, 2.00000000, 0.00000000, 1, 200, 200, 25890, NULL, '2026-03-17 06:32:52', '2026-03-17 14:33:52', '2026-03-17 06:32:52', '2026-03-17 06:32:52');
INSERT INTO `t_ai_billing_session` VALUES (2033796436414365698, '6a8d9393-e6b9-4de2-a85c-e40895c5fce6', 1, 1, 1, 1, 1, 2.10000000, 0.19600000, 1.90400000, 1, 200, 200, 3386, NULL, '2026-03-17 06:43:51', '2026-03-17 14:44:10', '2026-03-17 06:43:51', '2026-03-17 06:43:51');
INSERT INTO `t_ai_billing_session` VALUES (2033797370662027266, 'd4275df7-b4f0-459c-906d-da0a4f094b65', 1, 1, 1, 1, 1, 2.10000000, 0.38200000, 1.71800000, 1, 200, 200, 2975, NULL, '2026-03-17 06:47:34', '2026-03-17 14:48:46', '2026-03-17 06:47:34', '2026-03-17 06:47:34');
INSERT INTO `t_ai_billing_session` VALUES (2033798313755475970, '3e4b2c10-b040-4a00-ac18-d75547b9b19e', 1, 1, NULL, 1, 1, 2.10000000, 0.00000000, 0.00000000, 0, NULL, NULL, NULL, NULL, '2026-03-17 06:51:19', NULL, '2026-03-17 06:51:19', '2026-03-17 06:51:19');
INSERT INTO `t_ai_billing_session` VALUES (2033798815524278274, '1e38f9dc-7675-4ca1-91ab-9256eb1a6d84', 1, 1, 1, 1, 1, 2.10000000, 0.03900000, 2.06100000, 1, 200, 200, 11684, NULL, '2026-03-17 06:53:18', '2026-03-17 14:53:55', '2026-03-17 06:53:18', '2026-03-17 06:53:18');
INSERT INTO `t_ai_billing_session` VALUES (2033805846322827265, 'b9853308-1815-47ce-988c-7489eb9a2c09', 1, 1, 1, 1, 1, 0.00620000, 0.00060800, 0.00559200, 1, 200, 200, 5161, NULL, '2026-03-17 07:21:14', '2026-03-17 15:21:23', '2026-03-17 07:21:14', '2026-03-17 07:21:14');

-- ----------------------------
-- Table structure for t_ai_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_channel`;
CREATE TABLE `t_ai_channel`  (
  `channel_id` bigint NOT NULL COMMENT '自增主键',
  `channel_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称（全局唯一，便于管理识别）',
  `provider_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OPENAI' COMMENT '上游服务商类型（如 OPENAI / AZURE / CLAUDE 等）',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'https://api.openai.com' COMMENT '上游 API 基础 URL',
  `api_key_cipher` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密存储的 API Key 密文（对称加密，防止明文泄露）',
  `api_key_kid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'local-v1' COMMENT '加密密钥 ID / 版本标识，用于密钥轮换时定位解密密钥',
  `api_key_mask` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API Key 脱敏展示（如 sk-****abcd），仅用于 UI 展示',
  `organization` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'OpenAI Organization ID（多组织账号时指定归属组织）',
  `priority` int NOT NULL DEFAULT 0 COMMENT '路由优先级：数值越小优先级越高，相同优先级内按 weight 加权随机',
  `weight` int NOT NULL DEFAULT 100 COMMENT '负载均衡权重（同优先级渠道间按此值做加权随机选取）',
  `timeout_ms` int NOT NULL DEFAULT 60000 COMMENT '单次上游请求超时时间（毫秒），默认 60 秒',
  `max_retries` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '失败后最大重试次数（0=不重试）',
  `disabled_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用标记：0=启用，1=已禁用（禁用后不参与路由）',
  `deleted_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '软删除标记：0=正常，1=已删除',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人员工 ID，关联 t_employee.employee_id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注（如用途、限速说明等）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`channel_id`) USING BTREE,
  UNIQUE INDEX `uk_channel_name`(`channel_name` ASC) USING BTREE COMMENT '渠道名称全局唯一',
  INDEX `idx_route`(`disabled_flag` ASC, `priority` ASC, `weight` ASC, `channel_id` ASC) USING BTREE COMMENT '路由选取联合索引：过滤禁用 → 按优先级 → 按权重 → 按 ID 稳定排序'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI渠道配置（上游服务商接入管理）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_channel
-- ----------------------------
INSERT INTO `t_ai_channel` VALUES (1, 'vllm', 'OPENAI', 'https://api.deepseek.com', 'I3KkTtyFLzHWcn7eTgeRZTJfCMLCkVrcq97GqN4ROLZ5xnPDa0Bz1gnfgbAdCmBfpIxiRRdQ+6/JAmBonMWT', 'local-v1', 'sk-2****9680', NULL, 0, 1, 30000, 1, 0, 0, NULL, NULL, '2026-03-15 09:34:03', '2026-03-15 09:34:03');

-- ----------------------------
-- Table structure for t_ai_channel_group
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_channel_group`;
CREATE TABLE `t_ai_channel_group`  (
  `channel_group_id` bigint NOT NULL COMMENT '主键',
  `channel_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',
  `channel_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道id（,分割的字符串）',
  `disabled_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用标记：0=启用，1=已禁用',
  `deleted_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '软删除标记：0=正常，1=已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注（说明等）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`channel_group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '渠道分组配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_channel_group
-- ----------------------------
INSERT INTO `t_ai_channel_group` VALUES (2033703417493303297, '测试', '1', 0, 0, '测试分组', '2026-03-17 00:34:16', '2026-03-17 00:34:16');

-- ----------------------------
-- Table structure for t_ai_channel_model
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_channel_model`;
CREATE TABLE `t_ai_channel_model`  (
  `id` bigint NOT NULL COMMENT '自增主键',
  `channel_id` bigint NOT NULL COMMENT '渠道 ID，关联 t_ai_channel.channel_id',
  `model_id` bigint NOT NULL COMMENT '模型 ID，关联 t_ai_model.model_id',
  `disabled_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '绑定关系禁用标记：0=启用，1=禁用（可单独屏蔽某渠道的某模型）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_model`(`channel_id` ASC, `model_id` ASC) USING BTREE COMMENT '渠道+模型组合唯一，防止重复绑定',
  INDEX `idx_model_channel`(`model_id` ASC, `disabled_flag` ASC, `channel_id` ASC) USING BTREE COMMENT '按模型查询可用渠道列表（路由核心索引）',
  INDEX `idx_channel`(`channel_id` ASC, `disabled_flag` ASC) USING BTREE COMMENT '按渠道查询其绑定的有效模型列表'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '渠道-模型绑定关系（多对多，控制路由范围）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_channel_model
-- ----------------------------
INSERT INTO `t_ai_channel_model` VALUES (2, 1, 1, 0, '2026-03-15 09:47:38', '2026-03-15 09:47:38');

-- ----------------------------
-- Table structure for t_ai_model
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_model`;
CREATE TABLE `t_ai_model`  (
  `model_id` bigint NOT NULL COMMENT '自增主键',
  `model_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型标识名称（全局唯一，如 gpt-4o、text-embedding-3-small）',
  `endpoint_type` tinyint UNSIGNED NOT NULL DEFAULT 3 COMMENT '端点类型：1=仅 Chat，2=仅 Embedding，3=Chat+Embedding 均支持',
  `input_price_per_k` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '输入（Prompt）Token 单价，每 1000 token 计费金额（元）',
  `output_price_per_k` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '输出（Completion）Token 单价，每 1000 token 计费金额（元）',
  `disabled_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用标记：0=启用，1=已禁用（禁用后不可被路由选取）',
  `deleted_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '软删除标记：0=正常，1=已删除',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人员工 ID，关联 t_employee.employee_id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注（如上下文窗口大小、用途说明等）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`model_id`) USING BTREE,
  UNIQUE INDEX `uk_model_name`(`model_name` ASC) USING BTREE COMMENT '模型名称全局唯一',
  INDEX `idx_model_status_endpoint`(`disabled_flag` ASC, `endpoint_type` ASC) USING BTREE COMMENT '路由查询：按启用状态+端点类型快速过滤可用模型'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI模型配置（含输入/输出分价）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_model
-- ----------------------------
INSERT INTO `t_ai_model` VALUES (1, 'deepseek-chat', 1, 0.00200000, 0.00300000, 0, 0, NULL, NULL, '2026-03-15 09:17:45', '2026-03-15 09:17:45');

-- ----------------------------
-- Table structure for t_ai_model_config
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_model_config`;
CREATE TABLE `t_ai_model_config`  (
  `id` bigint NOT NULL,
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'BaseUrl',
  `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API密钥',
  `model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型名称',
  `is_active` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否启用',
  `end_point_mode_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求端点中模型名称',
  `max_tokens` int NULL DEFAULT 2000 COMMENT '输出响应最大令牌数',
  `model_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'chat' COMMENT '模型类型: chat/embedding/image_gen/video_gen/rerank/tts/stt',
  `model_features` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '能力标签(逗号分隔): VISION/FUNCTION_CALL/STREAMING',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人姓名',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted_flag` int NULL DEFAULT 0 COMMENT '0=未删除, 1=已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模型配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_model_config
-- ----------------------------
INSERT INTO `t_ai_model_config` VALUES (1, 'openai', 'https://api.deepseek.com', 'sk-20ad9ff497f14602ba2ae7d921979680', 'deepseek-chat', '1', 'deepseek-chat', 2000, 'chat', '/v1/chat/completions', NULL, NULL, NULL, '1', '管理员', '2026-03-04 21:51:38', 0);

-- ----------------------------
-- Table structure for t_ai_quota_ledger
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_quota_ledger`;
CREATE TABLE `t_ai_quota_ledger`  (
  `ledger_id` bigint NOT NULL COMMENT '自增主键',
  `employee_id` bigint NOT NULL COMMENT '员工 ID，关联 t_employee.employee_id',
  `token_id` bigint NULL DEFAULT NULL COMMENT '触发本次变动的 Token ID（充值类为空）',
  `billing_session_id` bigint NULL DEFAULT NULL COMMENT '关联的计费会话 ID（充值类为空）',
  `request_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的请求 ID（充值类为空，便于跨表追溯）',
  `action_type` tinyint UNSIGNED NOT NULL COMMENT '变动类型：1=充值，2=预扣，3=结算补扣，4=结算返还，5=回滚',
  `delta_quota` decimal(18, 8) NOT NULL COMMENT '额度变动量（正值=余额增加，负值=余额减少，单位：quota）',
  `balance_after` decimal(18, 8) NOT NULL COMMENT '操作后账户余额快照（单位：quota，用于快速核对，无需重放流水）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注（如充值来源、操作人等）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '流水创建时间（不可修改，审计用）',
  PRIMARY KEY (`ledger_id`) USING BTREE,
  INDEX `idx_employee_time`(`employee_id` ASC, `create_time` ASC) USING BTREE COMMENT '按员工查询余额变动历史（账单页用）',
  INDEX `idx_request_id`(`request_id` ASC) USING BTREE COMMENT '按请求 ID 查询关联流水（排障用）',
  INDEX `idx_billing_session`(`billing_session_id` ASC) USING BTREE COMMENT '按计费会话查询关联流水（对账用）'
) ENGINE = InnoDB AUTO_INCREMENT = 2033128555361693699 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '额度流水（账户余额变动审计日志，只追加不修改）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_quota_ledger
-- ----------------------------
INSERT INTO `t_ai_quota_ledger` VALUES (2033128555361693698, 1, NULL, NULL, NULL, 1, 1000.00000000, 2000.00000000, NULL, '2026-03-15 10:30:01');
INSERT INTO `t_ai_quota_ledger` VALUES (2033717523306721282, 1, 1, NULL, '074550a5-402e-4a0f-8241-62e58fdc030a', 2, -3.00000000, 1982.00000000, NULL, '2026-03-17 01:30:19');
INSERT INTO `t_ai_quota_ledger` VALUES (2033720514629853186, 1, 1, NULL, '8ebf4c0a-e80a-4c14-9dd1-953c5d14633c', 2, -3.00000000, 1979.00000000, NULL, '2026-03-17 01:42:12');
INSERT INTO `t_ai_quota_ledger` VALUES (2033720620695412737, 1, 1, 2033720514323668993, '8ebf4c0a-e80a-4c14-9dd1-953c5d14633c', 4, 1.00000000, 1980.00000000, NULL, '2026-03-17 01:42:37');
INSERT INTO `t_ai_quota_ledger` VALUES (2033752021083799554, 1, 1, NULL, '780a5471-c8ce-4c61-b76c-1ec2ca7feaa5', 2, -2.00000000, 1978.00000000, NULL, '2026-03-17 03:47:24');
INSERT INTO `t_ai_quota_ledger` VALUES (2033752128239878146, 1, 1, 2033752020702117889, '780a5471-c8ce-4c61-b76c-1ec2ca7feaa5', 4, 0.00000000, 1978.00000000, NULL, '2026-03-17 03:47:50');
INSERT INTO `t_ai_quota_ledger` VALUES (2033793672716156930, 1, 1, NULL, '2e7f2526-13c5-43a7-a513-2ea87156c6d9', 2, -2.00000000, 1976.00000000, NULL, '2026-03-17 06:32:52');
INSERT INTO `t_ai_quota_ledger` VALUES (2033793952757252098, 1, 1, 2033793671126515714, '2e7f2526-13c5-43a7-a513-2ea87156c6d9', 4, 0.00000000, 1976.00000000, NULL, '2026-03-17 06:33:59');
INSERT INTO `t_ai_quota_ledger` VALUES (2033796436728938498, 1, 1, NULL, '6a8d9393-e6b9-4de2-a85c-e40895c5fce6', 2, -2.10000000, 1973.90000000, NULL, '2026-03-17 06:43:51');
INSERT INTO `t_ai_quota_ledger` VALUES (2033796518027132929, 1, 1, 2033796436414365698, '6a8d9393-e6b9-4de2-a85c-e40895c5fce6', 4, 1.90400000, 1975.80400000, NULL, '2026-03-17 06:44:10');
INSERT INTO `t_ai_quota_ledger` VALUES (2033797371047903233, 1, 1, NULL, 'd4275df7-b4f0-459c-906d-da0a4f094b65', 2, -2.10000000, 1973.70400000, NULL, '2026-03-17 06:47:34');
INSERT INTO `t_ai_quota_ledger` VALUES (2033797674702929922, 1, 1, 2033797370662027266, 'd4275df7-b4f0-459c-906d-da0a4f094b65', 4, 1.71800000, 1975.42200000, NULL, '2026-03-17 06:48:46');
INSERT INTO `t_ai_quota_ledger` VALUES (2033798314124574721, 1, 1, NULL, '3e4b2c10-b040-4a00-ac18-d75547b9b19e', 2, -2.10000000, 1973.32200000, NULL, '2026-03-17 06:51:19');
INSERT INTO `t_ai_quota_ledger` VALUES (2033798816455413762, 1, 1, NULL, '1e38f9dc-7675-4ca1-91ab-9256eb1a6d84', 2, -2.10000000, 1973.08700000, NULL, '2026-03-17 06:53:18');
INSERT INTO `t_ai_quota_ledger` VALUES (2033798972101840898, 1, 1, 2033798815524278274, '1e38f9dc-7675-4ca1-91ab-9256eb1a6d84', 4, 2.06100000, 1975.14800000, NULL, '2026-03-17 06:53:56');
INSERT INTO `t_ai_quota_ledger` VALUES (2033805846582874114, 1, 1, NULL, 'b9853308-1815-47ce-988c-7489eb9a2c09', 2, -0.00620000, 1975.14180000, NULL, '2026-03-17 07:21:15');
INSERT INTO `t_ai_quota_ledger` VALUES (2033805882611945474, 1, 1, 2033805846322827265, 'b9853308-1815-47ce-988c-7489eb9a2c09', 4, 0.00559200, 1975.14739200, NULL, '2026-03-17 07:21:23');

-- ----------------------------
-- Table structure for t_ai_token
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_token`;
CREATE TABLE `t_ai_token`  (
  `token_id` bigint NOT NULL COMMENT '自增主键',
  `employee_id` bigint NOT NULL COMMENT '归属员工 ID，关联 t_employee.employee_id',
  `channel_group_id` bigint NOT NULL COMMENT '渠道分组id',
  `token_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Token 名称（同一员工下唯一，便于区分不同用途）',
  `token_prefix` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Token 展示前缀（明文，仅用于 UI 展示，不参与鉴权）',
  `token_hash` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始 Token 的 SHA-256 哈希值，用于鉴权比对，明文不落库',
  `expires_time` datetime NULL DEFAULT NULL COMMENT 'Token 过期时间；NULL 表示永不过期',
  `last_used_time` datetime NULL DEFAULT NULL COMMENT '最近一次鉴权成功时间（用于活跃度统计与安全审计）',
  `request_count` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计使用次数（鉴权通过即计数）',
  `disabled_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '禁用标记：0=启用，1=已禁用',
  `deleted_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '软删除标记：0=正常，1=已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注（用途说明等）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`token_id`) USING BTREE,
  UNIQUE INDEX `uk_token_hash`(`token_hash` ASC) USING BTREE COMMENT '哈希全局唯一，防止碰撞及重复录入',
  UNIQUE INDEX `uk_employee_token_name`(`employee_id` ASC, `token_name` ASC) USING BTREE COMMENT '同一员工下 Token 名称唯一',
  INDEX `idx_employee_status`(`employee_id` ASC, `disabled_flag` ASC, `deleted_flag` ASC) USING BTREE COMMENT '按员工查询有效 Token 列表',
  INDEX `idx_expires_time`(`expires_time` ASC) USING BTREE COMMENT '定时清理/过期扫描用'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI访问Token（客户端调用 /v1/* 接口的鉴权凭证）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_token
-- ----------------------------
INSERT INTO `t_ai_token` VALUES (1, 1, 0, '测试', 'sk-sa-_teV', '9bfc45d8c97def955062ab1c38c17ac5c624bc828489663a8b37662172ba193d', NULL, NULL, 0, 0, 0, NULL, '2026-03-16 07:51:10', '2026-03-16 07:51:10');

-- ----------------------------
-- Table structure for t_ai_usage_log
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_usage_log`;
CREATE TABLE `t_ai_usage_log`  (
  `usage_log_id` bigint NOT NULL COMMENT '主键',
  `request_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求唯一标识（UUID v4），与 t_ai_billing_session.request_id 一致',
  `billing_session_id` bigint NULL DEFAULT NULL COMMENT '关联的计费会话 ID，关联 t_ai_billing_session.billing_session_id',
  `employee_id` bigint NOT NULL COMMENT '发起请求的员工 ID，关联 t_employee.employee_id',
  `token_id` bigint NOT NULL COMMENT '鉴权使用的 Token ID，关联 t_ai_token.token_id',
  `channel_id` bigint NOT NULL COMMENT '路由选取的上游渠道 ID，关联 t_ai_channel.channel_id',
  `model_id` bigint NOT NULL COMMENT '请求使用的模型 ID，关联 t_ai_model.model_id',
  `endpoint_type` tinyint UNSIGNED NOT NULL COMMENT '端点类型：1=Chat，2=Embedding',
  `stream_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否为流式响应：0=非流式，1=SSE 流式',
  `prompt_tokens` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '输入（Prompt）Token 数量（上游实际返回值）',
  `completion_tokens` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '输出（Completion）Token 数量（上游实际返回值）',
  `total_tokens` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '总 Token 数量（= prompt_tokens + completion_tokens）',
  `input_cost` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '输入 Token 费用（元）= prompt_tokens / 1000 * input_price_per_1k',
  `output_cost` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '输出 Token 费用（元）= completion_tokens / 1000 * output_price_per_1k',
  `total_cost` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '本次请求总费用（元）= input_cost + output_cost',
  `preconsume_quota` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '预扣额度（冗余自计费会话，便于单表统计，单位：quota）',
  `settled_quota` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '结算额度（实际消耗，单位：quota）',
  `settle_delta_quota` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '结算差额（= settled_quota - preconsume_quota，正值补扣，负值返还）',
  `upstream_status_code` int NULL DEFAULT NULL COMMENT '上游 AI 服务返回的 HTTP 状态码',
  `gateway_status_code` int NULL DEFAULT NULL COMMENT '网关向客户端返回的 HTTP 状态码',
  `latency_ms` int UNSIGNED NULL DEFAULT NULL COMMENT '端到端请求耗时（毫秒）',
  `error_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误码（成功时为空，如 rate_limit_exceeded、context_length_exceeded 等）',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误详情描述（成功时为空）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志写入时间（不可修改，审计用）',
  PRIMARY KEY (`usage_log_id`) USING BTREE,
  UNIQUE INDEX `uk_usage_request_id`(`request_id` ASC) USING BTREE COMMENT 'request_id 全局唯一，防止重复写入',
  UNIQUE INDEX `uk_usage_billing_session`(`billing_session_id` ASC) USING BTREE COMMENT '与计费会话一一对应',
  INDEX `idx_employee_time`(`employee_id` ASC, `create_time` ASC) USING BTREE COMMENT '按员工查询用量历史（个人用量报表用）',
  INDEX `idx_token_time`(`token_id` ASC, `create_time` ASC) USING BTREE COMMENT '按 Token 查询调用记录（Token 详情页用）',
  INDEX `idx_model_time`(`model_id` ASC, `create_time` ASC) USING BTREE COMMENT '按模型统计用量趋势（模型分析报表用）'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用量日志（每次请求的完整用量与计费快照，只追加不修改）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_usage_log
-- ----------------------------
INSERT INTO `t_ai_usage_log` VALUES (2033720621022568449, '8ebf4c0a-e80a-4c14-9dd1-953c5d14633c', 2033720514323668993, 1, 1, 1, 1, 1, 1, 50, 1485, 1535, 0.05000000, 1.48500000, 1.53500000, 3.00000000, 2.00000000, 0.00000000, 200, 200, 24809, NULL, NULL, '2026-03-17 01:42:38');
INSERT INTO `t_ai_usage_log` VALUES (2033752128562839553, '780a5471-c8ce-4c61-b76c-1ec2ca7feaa5', 2033752020702117889, 1, 1, 1, 1, 1, 1, 50, 1530, 1580, 0.05000000, 1.53000000, 1.58000000, 2.00000000, 2.00000000, 0.00000000, 200, 200, 25087, NULL, NULL, '2026-03-17 03:47:50');
INSERT INTO `t_ai_usage_log` VALUES (2033793953285734401, '2e7f2526-13c5-43a7-a513-2ea87156c6d9', 2033793671126515714, 1, 1, 1, 1, 1, 1, 50, 1512, 1562, 0.05000000, 1.51200000, 1.56200000, 2.00000000, 2.00000000, 0.00000000, 200, 200, 25890, NULL, NULL, '2026-03-17 06:33:59');
INSERT INTO `t_ai_usage_log` VALUES (2033796518358482945, '6a8d9393-e6b9-4de2-a85c-e40895c5fce6', 2033796436414365698, 1, 1, 1, 1, 1, 1, 33, 163, 196, 0.03300000, 0.16300000, 0.19600000, 2.10000000, 0.19600000, 0.00000000, 200, 200, 3386, NULL, NULL, '2026-03-17 06:44:10');
INSERT INTO `t_ai_usage_log` VALUES (2033797675013308418, 'd4275df7-b4f0-459c-906d-da0a4f094b65', 2033797370662027266, 1, 1, 1, 1, 1, 1, 215, 167, 382, 0.21500000, 0.16700000, 0.38200000, 2.10000000, 0.38200000, 0.00000000, 200, 200, 2975, NULL, NULL, '2026-03-17 06:48:46');
INSERT INTO `t_ai_usage_log` VALUES (2033798972676460546, '1e38f9dc-7675-4ca1-91ab-9256eb1a6d84', 2033798815524278274, 1, 1, 1, 1, 1, 1, 7, 32, 39, 0.00700000, 0.03200000, 0.03900000, 2.10000000, 0.03900000, 0.00000000, 200, 200, 11684, NULL, NULL, '2026-03-17 06:53:56');
INSERT INTO `t_ai_usage_log` VALUES (2033805882867798018, 'b9853308-1815-47ce-988c-7489eb9a2c09', 2033805846322827265, 1, 1, 1, 1, 1, 1, 46, 172, 218, 0.00009200, 0.00051600, 0.00060800, 0.00620000, 0.00060800, 0.00000000, 200, 200, 5161, NULL, NULL, '2026-03-17 07:21:23');

-- ----------------------------
-- Table structure for t_ai_user_account
-- ----------------------------
DROP TABLE IF EXISTS `t_ai_user_account`;
CREATE TABLE `t_ai_user_account`  (
  `employee_id` bigint NOT NULL COMMENT '关联 t_employee.employee_id，一人一户，业务主键',
  `quota_remain` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '当前剩余可用额度（单位：quota）',
  `quota_used` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '历史累计已消耗额度（单位：quota）',
  `disabled_flag` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '账户冻结标记：0=正常，1=已冻结',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间（由 MySQL 自动维护）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
  PRIMARY KEY (`employee_id`) USING BTREE,
  INDEX `idx_quota_remain`(`quota_remain` ASC) USING BTREE COMMENT '辅助查询低余额/零余额账户（预警/排查用）'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AI用户额度账户（一人一户）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ai_user_account
-- ----------------------------
INSERT INTO `t_ai_user_account` VALUES (1, 1975.14739200, 24.85260800, 0, '2026-03-17 07:21:23', '2026-03-15 10:29:53');
INSERT INTO `t_ai_user_account` VALUES (2, 1000.00000000, 0.00000000, 0, '2026-03-16 07:50:14', '2026-03-16 07:50:14');

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `category_type` smallint NOT NULL COMMENT '分类类型',
  `parent_id` int NOT NULL COMMENT '父级id',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `disabled_flag` tinyint NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `deleted_flag` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 381 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表，主要用于商品分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (1, '手机', 1, 0, 0, 0, 0, NULL, '2022-10-10 22:27:24', '2022-07-14 20:55:15');
INSERT INTO `t_category` VALUES (2, '键盘', 1, 0, 0, 0, 0, NULL, '2022-09-14 21:39:00', '2022-07-14 20:55:48');
INSERT INTO `t_category` VALUES (3, '自定义1', 2, 0, 0, 0, 0, NULL, '2022-09-14 22:01:06', '2022-07-14 20:56:03');
INSERT INTO `t_category` VALUES (4, '自定义2', 2, 0, 0, 0, 0, NULL, '2022-09-14 22:01:10', '2022-07-14 20:56:09');
INSERT INTO `t_category` VALUES (351, '鼠标', 1, 0, 0, 0, 0, NULL, '2022-09-14 21:39:06', '2022-09-14 21:39:06');
INSERT INTO `t_category` VALUES (352, '苹果', 1, 1, 0, 0, 0, NULL, '2022-09-14 21:39:25', '2022-09-14 21:39:25');
INSERT INTO `t_category` VALUES (353, '华为', 1, 1, 0, 0, 0, NULL, '2022-09-14 21:39:32', '2022-09-14 21:39:32');
INSERT INTO `t_category` VALUES (354, 'IKBC', 1, 2, 0, 0, 0, NULL, '2022-09-14 21:39:38', '2022-09-14 21:39:38');
INSERT INTO `t_category` VALUES (355, '双飞燕', 1, 2, 0, 0, 0, NULL, '2022-09-14 21:39:47', '2022-09-14 21:39:47');
INSERT INTO `t_category` VALUES (356, '罗技', 1, 351, 0, 0, 0, NULL, '2022-09-14 21:39:57', '2022-09-14 21:39:57');
INSERT INTO `t_category` VALUES (357, '小米', 1, 1, 0, 0, 0, NULL, '2022-10-10 22:27:39', '2022-10-10 22:27:39');
INSERT INTO `t_category` VALUES (360, 'iphone', 1, 352, 0, 0, 0, NULL, '2023-12-04 21:26:55', '2023-12-01 19:54:22');

-- ----------------------------
-- Table structure for t_change_log
-- ----------------------------
DROP TABLE IF EXISTS `t_change_log`;
CREATE TABLE `t_change_log`  (
  `change_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '更新日志id',
  `update_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本',
  `type` int NOT NULL COMMENT '更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]',
  `publish_author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布人',
  `public_date` date NOT NULL COMMENT '发布日期',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新内容',
  `link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '跳转链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`change_log_id`) USING BTREE,
  UNIQUE INDEX `version_unique`(`update_version` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统更新日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_change_log
-- ----------------------------
INSERT INTO `t_change_log` VALUES (2, 'v1.1.0', 2, '卓大', '2020-05-09', 'SmartAdmin中后台系统 v1.1.0 版本（20200422）正式更新上线，更新内容如下：\n\n1.【新增】增加员工姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', 'http://smartadmin.1024lab.net/views/1.x/base/About.html', '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (8, 'v1.0.0', 1, '卓大', '2019-11-01', 'SmartAdmin中后台系统 v1.0.0 版本（20191101）正式更新上线，更新内容如下：\n\n1.【新增】人员管理\n\n2.【新增】系统设置\n\n3.【新增】心跳服务\n\n4.【新增】动态加载\n\n5.【新增】缓存策略\n\n6.【新增】定时任务', 'http://smartadmin.1024lab.net/views/1.x/base/About.html', '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (9, 'v1.2.0', 2, '卓大', '2020-05-23', 'SmartAdmin中后台系统 v1.2.0 版本（20200515）正式更新上线，更新内容如下：\n\n1.【新增】增加数据权限\n\n2.【新增】帮助文档', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (10, 'v1.2.1', 3, '卓大', '2020-05-24', 'SmartAdmin中后台系统 v1.2.1 版本（20200524）正式更新上线，更新内容如下：\n\n1.【修复】四级菜单权限bug\n\n2.【修复】缓存keepalive的Bug\n\n', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (11, 'v1.3.0', 2, '卓大', '2020-06-01', 'SmartAdmin中后台系统 v1.3.0 版本（20200601）正式更新上线，更新内容如下：\n\n1.【新增】工作台看板功能\n\n2.【新增】天气预报功能\n\n3.【新增】记录上次登录IP功能', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (12, 'v1.4.0', 2, '卓大', '2020-06-06', 'SmartAdmin中后台系统 v1.4.0 版本（20200606）正式更新上线，更新内容如下：\n\n1.【新增】联系客服功能\n\n2.【新增】意见反馈功能', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (13, 'v1.5.0', 2, '卓大', '2020-06-14', 'SmartAdmin中后台系统 v1.5.0 版本（20200614）正式更新上线，更新内容如下：\n\n1.【新增】OA系统\n\n2.【新增】通知公告', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (14, 'v1.6.0', 2, '卓大', '2020-06-17', 'SmartAdmin中后台系统 v1.6.0 版本（20200617）正式更新上线，更新内容如下：\n\n1.【新增】代码生成\n\n2.【新增】通知公告', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (15, 'v2.0.0', 1, '卓大', '2022-10-22', 'SmartAdmin中后台系统 v2.0.0 版本（20191101）正式更新上线，更新内容如下：\n\n1.【新增】人员管理\n\n2.【新增】系统设置\n\n3.【新增】心跳服务\n\n4.【新增】动态加载\n\n5.【新增】缓存策略\n\n6.【新增】定时任务', 'http://smartadmin.1024lab.net/views/1.x/base/About.html', '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (16, 'v1.7.0', 2, '卓大', '2022-10-22', 'SmartAdmin中后台系统 v1.7.0 版本（20200624）正式更新上线，更新内容如下：\n\n1.【新增】商品管理\n\n2.【新增】商品分类', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` VALUES (18, 'v3.0.0', 1, '卓大', '2024-01-01', 'SmartAdmin中后台系统 v3.0.0 版本（20240101）正式更新上线，更新内容如下：\n\n\n1、【新增】权限从SpringSecurity 转成 Sa-Token\n\n2、【新增】增加接口 加密、解密功能\n\n3、【新增】增加网络安全相关功能：登录限制、密码复杂度、最大在线时长等\n\n4、【新增】ant desgin vue 为 4.x 最新版本\n\n5、【新增】升级 vite5\n\n6、【新增】swagger增加knife4j接口文档\n\n7、【优化】后端sa-common 改名为 sa-base\n\n8、【优化】优化官网文档说明\n', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');

-- ----------------------------
-- Table structure for t_code_generator_config
-- ----------------------------
DROP TABLE IF EXISTS `t_code_generator_config`;
CREATE TABLE `t_code_generator_config`  (
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名',
  `basic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '基础命名信息',
  `fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '字段列表',
  `insert_and_update` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '新建、修改',
  `delete_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '删除',
  `query_fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '查询',
  `table_fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '列表',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详情',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成器的每个表的配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_code_generator_config
-- ----------------------------
INSERT INTO `t_code_generator_config` VALUES ('t_ai_app', '{\"backendAuthor\":\"Mission\",\"backendDate\":1766304044000,\"copyright\":\"Mission\",\"description\":\"ai应用管理表\",\"frontAuthor\":\"Mission\",\"frontDate\":1766304044000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.ai\",\"moduleName\":\"AiApp\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"应用名称\",\"columnName\":\"name\",\"fieldName\":\"name\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"应用名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"应用描述\",\"columnName\":\"description\",\"fieldName\":\"description\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"应用描述\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"应用提供商id\",\"columnName\":\"app_provider_id\",\"fieldName\":\"appProviderId\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"应用提供商id\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_at\",\"fieldName\":\"createAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_at\",\"fieldName\":\"updateAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"name\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"description\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"app_provider_id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":true,\"pageType\":\"drawer\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":true,\"isSupportDelete\":true}', '[{\"columnNameList\":[\"name\",\"description\"],\"fieldName\":\"keywords\",\"label\":\"关键字查询\",\"queryTypeEnum\":\"Like\",\"width\":\"200px\"}]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"name\",\"ellipsisFlag\":true,\"fieldName\":\"name\",\"label\":\"应用名称\",\"showFlag\":true},{\"columnName\":\"description\",\"ellipsisFlag\":true,\"fieldName\":\"description\",\"label\":\"应用描述\",\"showFlag\":true},{\"columnName\":\"app_provider_id\",\"ellipsisFlag\":true,\"fieldName\":\"appProviderId\",\"label\":\"应用提供商id\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":false},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人\",\"showFlag\":true},{\"columnName\":\"create_at\",\"ellipsisFlag\":true,\"fieldName\":\"createAt\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":false},{\"columnName\":\"update_at\",\"ellipsisFlag\":true,\"fieldName\":\"updateAt\",\"label\":\"更新时间\",\"showFlag\":true},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人\",\"showFlag\":true}]', NULL, '2025-12-21 08:06:15', '2025-12-21 08:06:15');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_app_provider', '{\"backendAuthor\":\"Mission\",\"backendDate\":1766304379000,\"copyright\":\"Mission\",\"description\":\"应用提供商\",\"frontAuthor\":\"Mission\",\"frontDate\":1766304379000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.ai\",\"moduleName\":\"AiAppProvider\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"名称\",\"columnName\":\"name\",\"fieldName\":\"name\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"baseUrl\",\"columnName\":\"base_url\",\"fieldName\":\"baseUrl\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"baseUrl\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"key\",\"columnName\":\"key\",\"fieldName\":\"key\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"key\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_at\",\"fieldName\":\"createAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人名称\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_at\",\"fieldName\":\"updateAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人名称\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人名称\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"name\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"base_url\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"key\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"create_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":true,\"pageType\":\"drawer\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":true,\"isSupportDelete\":true}', '[{\"columnNameList\":[\"name\"],\"fieldName\":\"name\",\"label\":\"名称\",\"queryTypeEnum\":\"Like\",\"width\":\"200px\"}]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"name\",\"ellipsisFlag\":true,\"fieldName\":\"name\",\"label\":\"名称\",\"showFlag\":true},{\"columnName\":\"base_url\",\"ellipsisFlag\":true,\"fieldName\":\"baseUrl\",\"label\":\"baseUrl\",\"showFlag\":true},{\"columnName\":\"key\",\"ellipsisFlag\":true,\"fieldName\":\"key\",\"label\":\"key\",\"showFlag\":true},{\"columnName\":\"create_at\",\"ellipsisFlag\":true,\"fieldName\":\"createAt\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":false},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人\",\"showFlag\":true},{\"columnName\":\"update_at\",\"ellipsisFlag\":true,\"fieldName\":\"updateAt\",\"label\":\"更新时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":false},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人\",\"showFlag\":true}]', NULL, '2025-12-21 08:07:51', '2025-12-21 08:07:51');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_conversations', '{\"backendAuthor\":\"Mission\",\"backendDate\":1766304508000,\"copyright\":\"Mission\",\"description\":\"会话表\",\"frontAuthor\":\"Mission\",\"frontDate\":1766304508000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.ai\",\"moduleName\":\"AiConversations\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"应用id\",\"columnName\":\"app_id\",\"fieldName\":\"appId\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"应用id\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"名称\",\"columnName\":\"name\",\"fieldName\":\"name\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"入参\",\"columnName\":\"inputs\",\"fieldName\":\"inputs\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"入参\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"总结\",\"columnName\":\"summary\",\"fieldName\":\"summary\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"总结\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_at\",\"fieldName\":\"createAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_at\",\"fieldName\":\"updateAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"app_id\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"inputs\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"summary\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":false,\"pageType\":\"modal\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":true,\"isSupportDelete\":true}', '[]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"app_id\",\"ellipsisFlag\":true,\"fieldName\":\"appId\",\"label\":\"应用id\",\"showFlag\":true},{\"columnName\":\"name\",\"ellipsisFlag\":true,\"fieldName\":\"name\",\"label\":\"名称\",\"showFlag\":true},{\"columnName\":\"inputs\",\"ellipsisFlag\":true,\"fieldName\":\"inputs\",\"label\":\"入参\",\"showFlag\":true},{\"columnName\":\"summary\",\"ellipsisFlag\":true,\"fieldName\":\"summary\",\"label\":\"总结\",\"showFlag\":true},{\"columnName\":\"create_at\",\"ellipsisFlag\":true,\"fieldName\":\"createAt\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":true},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人姓名\",\"showFlag\":true},{\"columnName\":\"update_at\",\"ellipsisFlag\":true,\"fieldName\":\"updateAt\",\"label\":\"更新时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":true},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人姓名\",\"showFlag\":true}]', NULL, '2025-12-21 08:10:13', '2025-12-21 08:10:13');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_dataset', '{\"backendAuthor\":\"Mission\",\"backendDate\":1764768839000,\"copyright\":\"Mission\",\"description\":\"知识库\",\"frontAuthor\":\"Mission\",\"frontDate\":1764768839000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.aiDataset\",\"moduleName\":\"AiDataset\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"dataset_id\",\"fieldName\":\"datasetId\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库名称\",\"columnName\":\"name\",\"fieldName\":\"name\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识库名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库描述\",\"columnName\":\"description\",\"fieldName\":\"description\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识库描述\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库权限（private，public）\",\"columnName\":\"permission\",\"dict\":\"DATASET_PERMISSION\",\"fieldName\":\"permission\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识库权限\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库类型 dify\",\"columnName\":\"type\",\"dict\":\"DATASET_TYPE\",\"fieldName\":\"type\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识库类型\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"外部知识库id\",\"columnName\":\"ext_knowledge_id\",\"fieldName\":\"extKnowledgeId\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"外部知识库id\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"外部知识库密钥\",\"columnName\":\"ext_knowledge_key\",\"fieldName\":\"extKnowledgeKey\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"外部知识库密钥\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人名称\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_time\",\"fieldName\":\"createTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_time\",\"fieldName\":\"updateTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人名称\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"逻辑删除标志\",\"columnName\":\"deleted_flag\",\"fieldName\":\"deletedFlag\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"逻辑删除标志\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"dataset_id\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"name\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"description\",\"frontComponent\":\"Textarea\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"permission\",\"frontComponent\":\"DictSelect\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"type\",\"frontComponent\":\"DictSelect\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"ext_knowledge_id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":false,\"updateFlag\":true},{\"columnName\":\"ext_knowledge_key\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":false,\"updateFlag\":true},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"deleted_flag\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":true,\"pageType\":\"drawer\",\"width\":\"400\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":false,\"isSupportDelete\":true}', '[{\"columnNameList\":[\"dataset_id\",\"name\",\"description\"],\"fieldName\":\"keyword\",\"label\":\"关键字查询\",\"queryTypeEnum\":\"Like\",\"width\":\"200px\"},{\"columnNameList\":[\"type\"],\"fieldName\":\"type\",\"label\":\"知识库类型\",\"queryTypeEnum\":\"Equal\",\"width\":\"200px\"},{\"columnNameList\":[\"permission\"],\"fieldName\":\"permission\",\"label\":\"知识库权限\",\"queryTypeEnum\":\"Equal\",\"width\":\"200px\"}]', '[{\"columnName\":\"dataset_id\",\"ellipsisFlag\":true,\"fieldName\":\"datasetId\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"name\",\"ellipsisFlag\":true,\"fieldName\":\"name\",\"label\":\"知识库名称\",\"showFlag\":true},{\"columnName\":\"description\",\"ellipsisFlag\":true,\"fieldName\":\"description\",\"label\":\"知识库描述\",\"showFlag\":true},{\"columnName\":\"permission\",\"ellipsisFlag\":true,\"fieldName\":\"permission\",\"label\":\"知识库权限（private，public）\",\"showFlag\":true},{\"columnName\":\"type\",\"ellipsisFlag\":true,\"fieldName\":\"type\",\"label\":\"知识库类型 dify\",\"showFlag\":true},{\"columnName\":\"ext_knowledge_id\",\"ellipsisFlag\":true,\"fieldName\":\"extKnowledgeId\",\"label\":\"外部知识库id\",\"showFlag\":true},{\"columnName\":\"ext_knowledge_key\",\"ellipsisFlag\":true,\"fieldName\":\"extKnowledgeKey\",\"label\":\"外部知识库密钥\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":false},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人名称\",\"showFlag\":true},{\"columnName\":\"create_time\",\"ellipsisFlag\":true,\"fieldName\":\"createTime\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":false},{\"columnName\":\"update_time\",\"ellipsisFlag\":true,\"fieldName\":\"updateTime\",\"label\":\"更新时间\",\"showFlag\":true},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人名称\",\"showFlag\":true},{\"columnName\":\"deleted_flag\",\"ellipsisFlag\":true,\"fieldName\":\"deletedFlag\",\"label\":\"逻辑删除标志\",\"showFlag\":false}]', NULL, '2025-12-03 13:52:59', '2025-12-03 13:52:59');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_documents', '{\"backendAuthor\":\"Mission\",\"backendDate\":1769603781000,\"copyright\":\"Mission\",\"description\":\"文档列表\",\"frontAuthor\":\"Mission\",\"frontDate\":1769603781000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.knowledge\",\"moduleName\":\"AiDocuments\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"id\",\"enumName\":\"\",\"fieldName\":\"id\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库id\",\"columnName\":\"knowledge_id\",\"fieldName\":\"knowledgeId\",\"javaType\":\"Long\",\"jsType\":\"Number\",\"label\":\"知识库id\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"文档名称\",\"columnName\":\"name\",\"fieldName\":\"name\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"文档名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"文档字数\",\"columnName\":\"word_count\",\"fieldName\":\"wordCount\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"文档字数\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"状态 pending-待处理 success-成功 fail-失败\",\"columnName\":\"status\",\"dict\":\"PROCESS_STATUS\",\"fieldName\":\"status\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"状态 pending-待处理 success-成功 fail-失败\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"文件id\",\"columnName\":\"file_id\",\"fieldName\":\"fileId\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"文件id\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_time\",\"fieldName\":\"createTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_time\",\"fieldName\":\"updateTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"knowledge_id\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"word_count\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"status\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"file_id\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":false,\"pageType\":\"drawer\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":true,\"isSupportDelete\":true}', '[]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"knowledge_id\",\"ellipsisFlag\":true,\"fieldName\":\"knowledgeId\",\"label\":\"知识库id\",\"showFlag\":true},{\"columnName\":\"name\",\"ellipsisFlag\":true,\"fieldName\":\"name\",\"label\":\"文档名称\",\"showFlag\":true},{\"columnName\":\"word_count\",\"ellipsisFlag\":true,\"fieldName\":\"wordCount\",\"label\":\"文档字数\",\"showFlag\":true},{\"columnName\":\"status\",\"ellipsisFlag\":true,\"fieldName\":\"status\",\"label\":\"状态 pending-待处理 success-成功 fail-失败\",\"showFlag\":true},{\"columnName\":\"file_id\",\"ellipsisFlag\":true,\"fieldName\":\"fileId\",\"label\":\"文件id\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":true},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人姓名\",\"showFlag\":true},{\"columnName\":\"create_time\",\"ellipsisFlag\":true,\"fieldName\":\"createTime\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":true},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人姓名\",\"showFlag\":true},{\"columnName\":\"update_time\",\"ellipsisFlag\":true,\"fieldName\":\"updateTime\",\"label\":\"更新时间\",\"showFlag\":true}]', NULL, '2026-01-28 12:40:57', '2026-01-28 12:40:57');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_document_segments', '{\"backendAuthor\":\"Mission\",\"backendDate\":1769604356000,\"copyright\":\"Mission\",\"description\":\"文档分块表\",\"frontAuthor\":\"Mission\",\"frontDate\":1769604356000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.knowledge\",\"moduleName\":\"AiDocumentSegments\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"Long\",\"jsType\":\"Number\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库id\",\"columnName\":\"knowledge_id\",\"fieldName\":\"knowledgeId\",\"javaType\":\"Long\",\"jsType\":\"Number\",\"label\":\"知识库id\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"文档名称\",\"columnName\":\"document_id\",\"fieldName\":\"documentId\",\"javaType\":\"Long\",\"jsType\":\"Number\",\"label\":\"文档名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识类型: DOCUMENT-文档, QA-问答\",\"columnName\":\"type\",\"fieldName\":\"type\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识类型: DOCUMENT-文档, QA-问答\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"文档字数\",\"columnName\":\"word_count\",\"fieldName\":\"wordCount\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"文档字数\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"排序\",\"columnName\":\"position\",\"fieldName\":\"position\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"排序\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"分块内容\",\"columnName\":\"content\",\"fieldName\":\"content\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"分块内容\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"回答，QA类型才有\",\"columnName\":\"answer\",\"fieldName\":\"answer\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"回答，QA类型才有\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"状态 pending-待处理 processing处理中 completed成功 fail失败\",\"columnName\":\"status\",\"fieldName\":\"status\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"状态 pending-待处理 processing处理中 completed成功 fail失败\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_time\",\"fieldName\":\"createTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_time\",\"fieldName\":\"updateTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"InputNumber\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"knowledge_id\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"document_id\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"type\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"word_count\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"position\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"content\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"answer\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"status\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":false,\"pageType\":\"modal\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":true,\"isSupportDelete\":true}', '[]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"knowledge_id\",\"ellipsisFlag\":true,\"fieldName\":\"knowledgeId\",\"label\":\"知识库id\",\"showFlag\":true},{\"columnName\":\"document_id\",\"ellipsisFlag\":true,\"fieldName\":\"documentId\",\"label\":\"文档名称\",\"showFlag\":true},{\"columnName\":\"type\",\"ellipsisFlag\":true,\"fieldName\":\"type\",\"label\":\"知识类型: DOCUMENT-文档, QA-问答\",\"showFlag\":true},{\"columnName\":\"word_count\",\"ellipsisFlag\":true,\"fieldName\":\"wordCount\",\"label\":\"文档字数\",\"showFlag\":true},{\"columnName\":\"position\",\"ellipsisFlag\":true,\"fieldName\":\"position\",\"label\":\"排序\",\"showFlag\":true},{\"columnName\":\"content\",\"ellipsisFlag\":true,\"fieldName\":\"content\",\"label\":\"分块内容\",\"showFlag\":true},{\"columnName\":\"answer\",\"ellipsisFlag\":true,\"fieldName\":\"answer\",\"label\":\"回答，QA类型才有\",\"showFlag\":true},{\"columnName\":\"status\",\"ellipsisFlag\":true,\"fieldName\":\"status\",\"label\":\"状态 pending-待处理 processing处理中 completed成功 fail失败\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":true},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人姓名\",\"showFlag\":true},{\"columnName\":\"create_time\",\"ellipsisFlag\":true,\"fieldName\":\"createTime\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":true},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人姓名\",\"showFlag\":true},{\"columnName\":\"update_time\",\"ellipsisFlag\":true,\"fieldName\":\"updateTime\",\"label\":\"更新时间\",\"showFlag\":true}]', NULL, '2026-01-28 12:46:35', '2026-01-28 12:46:35');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_knowledge', '{\"backendAuthor\":\"Mission\",\"backendDate\":1769567047000,\"copyright\":\"Mission\",\"description\":\"知识库\",\"frontAuthor\":\"Mission\",\"frontDate\":1769567047000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.knowledge\",\"moduleName\":\"AiKnowledge\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库id\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识库id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"知识库名称\",\"columnName\":\"name\",\"fieldName\":\"name\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"知识库名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"knowledge-base\",\"columnName\":\"type\",\"dict\":\"DATASET_TYPE\",\"fieldName\":\"type\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"knowledge-base\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"描述\",\"columnName\":\"description\",\"fieldName\":\"description\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"描述\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"权限 private-私有 public-公开\",\"columnName\":\"permission\",\"dict\":\"DATASET_PERMISSION\",\"fieldName\":\"permission\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"权限 private-私有 public-公开\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"向量模型\",\"columnName\":\"embedding_model\",\"fieldName\":\"embeddingModel\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"向量模型\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"向量模型\",\"columnName\":\"enable_rerank\",\"dict\":\"YES_NO\",\"fieldName\":\"enableRerank\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"向量模型\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"reRank模型 当启动rerank时使用\",\"columnName\":\"rerank_model\",\"fieldName\":\"rerankModel\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"reRank模型 当启动rerank时使用\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_at\",\"fieldName\":\"createAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_at\",\"fieldName\":\"updateAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"删除标志\",\"columnName\":\"deleted_flag\",\"fieldName\":\"deletedFlag\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"删除标志\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"name\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"type\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"description\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":false,\"updateFlag\":true},{\"columnName\":\"permission\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"embedding_model\",\"frontComponent\":\"DictSelect\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"enable_rerank\",\"frontComponent\":\"InputNumber\",\"insertFlag\":true,\"requiredFlag\":false,\"updateFlag\":true},{\"columnName\":\"rerank_model\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":false,\"updateFlag\":true},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"deleted_flag\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":true,\"pageType\":\"drawer\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":false,\"isSupportDelete\":true}', '[{\"columnNameList\":[\"name\"],\"fieldName\":\"name\",\"label\":\"知识库名称\",\"queryTypeEnum\":\"Like\",\"width\":\"200px\"}]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"知识库id\",\"showFlag\":true},{\"columnName\":\"name\",\"ellipsisFlag\":true,\"fieldName\":\"name\",\"label\":\"知识库名称\",\"showFlag\":true},{\"columnName\":\"type\",\"ellipsisFlag\":true,\"fieldName\":\"type\",\"label\":\"knowledge-base\",\"showFlag\":true},{\"columnName\":\"description\",\"ellipsisFlag\":true,\"fieldName\":\"description\",\"label\":\"描述\",\"showFlag\":true},{\"columnName\":\"permission\",\"ellipsisFlag\":true,\"fieldName\":\"permission\",\"label\":\"权限 private-私有 public-公开\",\"showFlag\":true},{\"columnName\":\"embedding_model\",\"ellipsisFlag\":true,\"fieldName\":\"embeddingModel\",\"label\":\"向量模型\",\"showFlag\":true},{\"columnName\":\"enable_rerank\",\"ellipsisFlag\":true,\"fieldName\":\"enableRerank\",\"label\":\"向量模型\",\"showFlag\":true},{\"columnName\":\"rerank_model\",\"ellipsisFlag\":true,\"fieldName\":\"rerankModel\",\"label\":\"reRank模型 当启动rerank时使用\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":false},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人姓名\",\"showFlag\":true},{\"columnName\":\"create_at\",\"ellipsisFlag\":true,\"fieldName\":\"createAt\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":false},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人姓名\",\"showFlag\":true},{\"columnName\":\"update_at\",\"ellipsisFlag\":true,\"fieldName\":\"updateAt\",\"label\":\"更新时间\",\"showFlag\":true},{\"columnName\":\"deleted_flag\",\"ellipsisFlag\":true,\"fieldName\":\"deletedFlag\",\"label\":\"删除标志\",\"showFlag\":false}]', NULL, '2026-01-28 02:26:51', '2026-01-28 02:26:51');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_messages', '{\"backendAuthor\":\"Mission\",\"backendDate\":1766304616000,\"copyright\":\"Mission\",\"description\":\"消息表\",\"frontAuthor\":\"Mission\",\"frontDate\":1766304616000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.ai\",\"moduleName\":\"AiMessages\"}', '[{\"autoIncreaseFlag\":false,\"columnComment\":\"id\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"会话ID\",\"columnName\":\"conversaton_id\",\"fieldName\":\"conversatonId\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"会话ID\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"查询\",\"columnName\":\"query\",\"fieldName\":\"query\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"查询\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"消息\",\"columnName\":\"message\",\"fieldName\":\"message\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"消息\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"入参\",\"columnName\":\"inputs\",\"fieldName\":\"inputs\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"入参\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"回答\",\"columnName\":\"answer\",\"fieldName\":\"answer\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"回答\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_at\",\"fieldName\":\"createAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_at\",\"fieldName\":\"updateAt\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":false},{\"columnName\":\"conversaton_id\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"query\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"message\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"inputs\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"answer\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_at\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":false,\"pageType\":\"modal\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":true,\"isSupportDelete\":true}', '[]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"conversaton_id\",\"ellipsisFlag\":true,\"fieldName\":\"conversatonId\",\"label\":\"会话ID\",\"showFlag\":true},{\"columnName\":\"query\",\"ellipsisFlag\":true,\"fieldName\":\"query\",\"label\":\"查询\",\"showFlag\":true},{\"columnName\":\"message\",\"ellipsisFlag\":true,\"fieldName\":\"message\",\"label\":\"消息\",\"showFlag\":true},{\"columnName\":\"inputs\",\"ellipsisFlag\":true,\"fieldName\":\"inputs\",\"label\":\"入参\",\"showFlag\":true},{\"columnName\":\"answer\",\"ellipsisFlag\":true,\"fieldName\":\"answer\",\"label\":\"回答\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":true},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人姓名\",\"showFlag\":true},{\"columnName\":\"create_at\",\"ellipsisFlag\":true,\"fieldName\":\"createAt\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":true},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人姓名\",\"showFlag\":true},{\"columnName\":\"update_at\",\"ellipsisFlag\":true,\"fieldName\":\"updateAt\",\"label\":\"更新时间\",\"showFlag\":true}]', NULL, '2025-12-21 08:11:48', '2025-12-21 08:11:48');
INSERT INTO `t_code_generator_config` VALUES ('t_ai_model_config', '{\"backendAuthor\":\"Mission\",\"backendDate\":1769565578000,\"copyright\":\"Mission\",\"description\":\"模型配置表\",\"frontAuthor\":\"Mission\",\"frontDate\":1769565578000,\"javaPackageName\":\"net.lab1024.sa.admin.module.business.ai.model\",\"moduleName\":\"AiModelConfig\"}', '[{\"autoIncreaseFlag\":true,\"columnComment\":\"\",\"columnName\":\"id\",\"fieldName\":\"id\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"id\",\"primaryKeyFlag\":true},{\"autoIncreaseFlag\":false,\"columnComment\":\"厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)\",\"columnName\":\"provider\",\"dict\":\"MODEL_PROVIDER\",\"fieldName\":\"provider\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"BaseUrl\",\"columnName\":\"base_url\",\"fieldName\":\"baseUrl\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"关键配置\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"API密钥\",\"columnName\":\"api_key\",\"fieldName\":\"apiKey\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"API密钥\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"模型名称\",\"columnName\":\"model_name\",\"fieldName\":\"modelName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"模型名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"是否启用\",\"columnName\":\"is_active\",\"dict\":\"YES_NO\",\"fieldName\":\"isActive\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"是否激活\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"请求端点中模型名称\",\"columnName\":\"end_point_mode_name\",\"fieldName\":\"endPointModeName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"请求端点中模型名称\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"输出响应最大令牌数\",\"columnName\":\"max_tokens\",\"fieldName\":\"maxTokens\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"输出响应最大令牌数\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"模型类型 (CHAT/EMBEDDING)\",\"columnName\":\"model_type\",\"dict\":\"MODEL_TYPE\",\"fieldName\":\"modelType\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"模型类型 (CHAT/EMBEDDING)\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions\",\"columnName\":\"path\",\"fieldName\":\"path\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人\",\"columnName\":\"create_by\",\"fieldName\":\"createBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建人姓名\",\"columnName\":\"create_by_name\",\"fieldName\":\"createByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"创建人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"创建时间\",\"columnName\":\"create_time\",\"fieldName\":\"createTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"创建时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人\",\"columnName\":\"update_by\",\"fieldName\":\"updateBy\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新人姓名\",\"columnName\":\"update_by_name\",\"fieldName\":\"updateByName\",\"javaType\":\"String\",\"jsType\":\"String\",\"label\":\"更新人姓名\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"更新时间\",\"columnName\":\"update_time\",\"fieldName\":\"updateTime\",\"javaType\":\"LocalDateTime\",\"jsType\":\"Date\",\"label\":\"更新时间\",\"primaryKeyFlag\":false},{\"autoIncreaseFlag\":false,\"columnComment\":\"0=未删除, 1=已删除\",\"columnName\":\"deleted_flag\",\"fieldName\":\"deletedFlag\",\"javaType\":\"Integer\",\"jsType\":\"Number\",\"label\":\"0=未删除, 1=已删除\",\"primaryKeyFlag\":false}]', '{\"countPerLine\":1,\"fieldList\":[{\"columnName\":\"id\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"provider\",\"frontComponent\":\"DictSelect\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"base_url\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"api_key\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"model_name\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"is_active\",\"frontComponent\":\"DictSelect\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"end_point_mode_name\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"max_tokens\",\"frontComponent\":\"InputNumber\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"model_type\",\"frontComponent\":\"DictSelect\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"path\",\"frontComponent\":\"Input\",\"insertFlag\":true,\"requiredFlag\":true,\"updateFlag\":true},{\"columnName\":\"create_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"create_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_by_name\",\"frontComponent\":\"Input\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"update_time\",\"frontComponent\":\"DateTime\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false},{\"columnName\":\"deleted_flag\",\"frontComponent\":\"InputNumber\",\"insertFlag\":false,\"requiredFlag\":false,\"updateFlag\":false}],\"isSupportInsertAndUpdate\":true,\"pageType\":\"modal\",\"width\":\"500\"}', '{\"deleteEnum\":\"SingleAndBatch\",\"isPhysicallyDeleted\":false,\"isSupportDelete\":true}', '[{\"columnNameList\":[\"model_name\"],\"fieldName\":\"modelName\",\"label\":\"模型名称\",\"queryTypeEnum\":\"Like\",\"width\":\"200px\"}]', '[{\"columnName\":\"id\",\"ellipsisFlag\":true,\"fieldName\":\"id\",\"label\":\"id\",\"showFlag\":true},{\"columnName\":\"provider\",\"ellipsisFlag\":true,\"fieldName\":\"provider\",\"label\":\"厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)\",\"showFlag\":true},{\"columnName\":\"base_url\",\"ellipsisFlag\":true,\"fieldName\":\"baseUrl\",\"label\":\"关键配置\",\"showFlag\":true},{\"columnName\":\"api_key\",\"ellipsisFlag\":true,\"fieldName\":\"apiKey\",\"label\":\"API密钥\",\"showFlag\":true},{\"columnName\":\"model_name\",\"ellipsisFlag\":true,\"fieldName\":\"modelName\",\"label\":\"模型名称\",\"showFlag\":true},{\"columnName\":\"is_active\",\"ellipsisFlag\":true,\"fieldName\":\"isActive\",\"label\":\"是否激活\",\"showFlag\":true},{\"columnName\":\"end_point_mode_name\",\"ellipsisFlag\":true,\"fieldName\":\"endPointModeName\",\"label\":\"请求端点中模型名称\",\"showFlag\":true},{\"columnName\":\"max_tokens\",\"ellipsisFlag\":true,\"fieldName\":\"maxTokens\",\"label\":\"输出响应最大令牌数\",\"showFlag\":true},{\"columnName\":\"model_type\",\"ellipsisFlag\":true,\"fieldName\":\"modelType\",\"label\":\"模型类型 (CHAT/EMBEDDING)\",\"showFlag\":true},{\"columnName\":\"path\",\"ellipsisFlag\":true,\"fieldName\":\"path\",\"label\":\"Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions\",\"showFlag\":true},{\"columnName\":\"create_by\",\"ellipsisFlag\":true,\"fieldName\":\"createBy\",\"label\":\"创建人\",\"showFlag\":false},{\"columnName\":\"create_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"createByName\",\"label\":\"创建人姓名\",\"showFlag\":true},{\"columnName\":\"create_time\",\"ellipsisFlag\":true,\"fieldName\":\"createTime\",\"label\":\"创建时间\",\"showFlag\":true},{\"columnName\":\"update_by\",\"ellipsisFlag\":true,\"fieldName\":\"updateBy\",\"label\":\"更新人\",\"showFlag\":false},{\"columnName\":\"update_by_name\",\"ellipsisFlag\":true,\"fieldName\":\"updateByName\",\"label\":\"更新人姓名\",\"showFlag\":true},{\"columnName\":\"update_time\",\"ellipsisFlag\":true,\"fieldName\":\"updateTime\",\"label\":\"更新时间\",\"showFlag\":true},{\"columnName\":\"deleted_flag\",\"ellipsisFlag\":true,\"fieldName\":\"deletedFlag\",\"label\":\"0=未删除, 1=已删除\",\"showFlag\":false}]', NULL, '2026-01-28 02:05:22', '2026-01-28 02:05:22');

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config`  (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名字',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数key',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次修改时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_config
-- ----------------------------
INSERT INTO `t_config` VALUES (1, '万能密码', 'super_password', '1024ok', '执行示例任务2', '2026-03-17 12:14:13', '2021-12-16 23:32:46');
INSERT INTO `t_config` VALUES (2, '三级等保', 'level3_protect_config', '{\n	\"fileDetectFlag\":true,\n	\"loginActiveTimeoutMinutes\":30,\n	\"loginFailLockMinutes\":30,\n	\"loginFailMaxTimes\":3,\n	\"maxUploadFileSizeMb\":30,\n	\"passwordComplexityEnabled\":true,\n	\"regularChangePasswordMonths\":3,\n	\"regularChangePasswordNotAllowRepeatTimes\":3,\n	\"twoFactorLoginEnabled\":false\n}', 'SmartJob Sample2 update', '2024-09-03 21:49:23', '2024-08-13 11:44:49');

-- ----------------------------
-- Table structure for t_data_tracer
-- ----------------------------
DROP TABLE IF EXISTS `t_data_tracer`;
CREATE TABLE `t_data_tracer`  (
  `data_tracer_id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` bigint NOT NULL COMMENT '各种单据的id',
  `type` int NOT NULL COMMENT '单据类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作内容',
  `diff_old` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '差异：旧的数据',
  `diff_new` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '差异：新的数据',
  `extra_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '额外信息',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型：1 后管用户 ',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地区',
  `user_agent` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ua',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`data_tracer_id`) USING BTREE,
  INDEX `order_id_order_type`(`data_id` ASC, `type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '各种单据操作记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_data_tracer
-- ----------------------------
INSERT INTO `t_data_tracer` VALUES (35, 10, 1, '新增', NULL, NULL, NULL, 47, 1, '善逸', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.61', '2023-10-07 19:02:24', '2023-10-07 19:02:24');
INSERT INTO `t_data_tracer` VALUES (36, 11, 1, '新增', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 19:55:53', '2023-12-01 19:55:53');
INSERT INTO `t_data_tracer` VALUES (37, 12, 1, '新增', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 19:57:26', '2023-12-01 19:57:26');
INSERT INTO `t_data_tracer` VALUES (38, 11, 1, '', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 19:58:09', '2023-12-01 19:58:09');
INSERT INTO `t_data_tracer` VALUES (39, 2, 3, '修改企业信息', '统一社会信用代码:\"1024lab\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:有限企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大\"<br/>省份名称:\"河南省\"<br/>企业logo:\"public/common/fb827d63dda74a60ab8b4f70cc7c7d0a_20221022145641_jpg\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室\"<br/>邮箱:\"lab1024@163.com\"', '营业执照:\"public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg\"<br/>统一社会信用代码:\"1024lab1\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:外资企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大1\"<br/>省份名称:\"河南省\"<br/>企业logo:\"\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室1\"<br/>邮箱:\"lab1024@163.com\"', NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:05:05', '2023-12-01 20:05:05');
INSERT INTO `t_data_tracer` VALUES (40, 2, 3, '修改企业信息', '营业执照:\"public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg\"<br/>统一社会信用代码:\"1024lab1\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:外资企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大1\"<br/>省份名称:\"河南省\"<br/>企业logo:\"\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室1\"<br/>邮箱:\"lab1024@163.com\"', '营业执照:\"public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg\"<br/>统一社会信用代码:\"1024lab\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:外资企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大\"<br/>省份名称:\"河南省\"<br/>企业logo:\"\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室\"<br/>邮箱:\"lab1024@163.com\"', NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:05:54', '2023-12-01 20:05:54');
INSERT INTO `t_data_tracer` VALUES (41, 2, 3, '更新银行:<br/>', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:09:17', '2023-12-01 20:09:17');
INSERT INTO `t_data_tracer` VALUES (42, 2, 3, '更新发票：<br/>删除状态:由【false】变更为【】', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:09:20', '2023-12-01 20:09:20');
INSERT INTO `t_data_tracer` VALUES (49, 1, 3, '修改企业信息', '营业执照:\"public/common/852b7e19bef94af39c1a6156edf47cfb_20221022170332_jpg\"<br/>统一社会信用代码:\"1024lab_block\"<br/>详细地址:\"区块链大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:有限企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"开云\"<br/>省份名称:\"河南省\"<br/>企业logo:\"public/common/f4a76fa720814949a610f05f6f9545bf_20221022170256_jpg\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新区块链实验室\"', '营业执照:\"public/common/1d89055e5680426280446aff1e7e627c_20240306112451.jpeg\"<br/>统一社会信用代码:\"1024lab_block\"<br/>详细地址:\"区块链大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:有限企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"开云\"<br/>省份名称:\"河南省\"<br/>企业logo:\"public/common/34f5ac0fc097402294aea75352c128f0_20240306112435.png\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新区块链实验室\"', NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '2024-03-06 11:24:55', '2024-03-06 11:24:55');
INSERT INTO `t_data_tracer` VALUES (99, 12, 1, '', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36', '2024-09-03 21:06:32', '2024-09-03 21:06:32');

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`  (
  `department_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门主键id',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '部门负责人id',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '部门的父级id',
  `sort` int NOT NULL COMMENT '部门排序',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`department_id`) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES (1, '1024创新实验室', 1, 0, 1, '2022-10-19 20:17:09', '2022-10-19 20:17:09');
INSERT INTO `t_department` VALUES (2, '开发部', 44, 1, 1000, '2022-10-19 20:22:23', '2022-10-19 20:22:23');
INSERT INTO `t_department` VALUES (3, '产品部', 2, 1, 99, '2022-10-21 10:25:30', '2022-10-21 10:25:30');
INSERT INTO `t_department` VALUES (4, '销售部', 64, 1, 9, '2022-10-21 10:25:47', '2022-10-21 10:25:47');
INSERT INTO `t_department` VALUES (5, '测试部', 48, 1, 0, '2022-11-05 10:54:18', '2022-11-05 10:54:18');
INSERT INTO `t_department` VALUES (7, '直播组', 44, 1, 1111, '2024-07-02 19:38:15', '2024-07-02 19:38:15');
INSERT INTO `t_department` VALUES (8, '抖音组', 47, 7, 0, '2024-07-02 19:39:11', '2024-07-02 19:39:11');

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `dict_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名字',
  `dict_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典编码',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典备注',
  `disabled_flag` tinyint NOT NULL DEFAULT 0 COMMENT '禁用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `unique_code`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict` VALUES (1, '商品地区', 'GOODS_PLACE', '用于商品管理中的商品地区1', 0, '2025-03-27 14:42:26', '2025-03-31 11:23:03');
INSERT INTO `t_dict` VALUES (4, '知识库权限', 'DATASET_PERMISSION', '', 0, '2025-12-03 13:29:49', '2025-12-03 13:29:49');
INSERT INTO `t_dict` VALUES (5, '知识库类型', 'DATASET_TYPE', '', 0, '2025-12-03 13:50:30', '2025-12-03 13:50:30');
INSERT INTO `t_dict` VALUES (6, '模型类型', 'MODEL_TYPE', '模型类型', 0, '2026-01-28 01:51:42', '2026-01-28 01:51:42');
INSERT INTO `t_dict` VALUES (7, '是否', 'YES_NO', '', 0, '2026-01-28 01:56:45', '2026-01-28 01:56:45');
INSERT INTO `t_dict` VALUES (8, '模型供应商', 'MODEL_PROVIDER', '', 0, '2026-01-28 02:01:13', '2026-01-28 02:01:13');
INSERT INTO `t_dict` VALUES (9, '处理状态', 'PROCESS_STATUS', '', 0, '2026-01-28 12:38:58', '2026-01-28 12:38:58');

-- ----------------------------
-- Table structure for t_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_data`;
CREATE TABLE `t_dict_data`  (
  `dict_data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典数据id',
  `dict_id` bigint NOT NULL COMMENT '字典id',
  `data_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项值',
  `data_label` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项显示名称',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort_order` int NOT NULL COMMENT '排序（越大越靠前）',
  `disabled_flag` tinyint NOT NULL DEFAULT 0 COMMENT '禁用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict_data
-- ----------------------------
INSERT INTO `t_dict_data` VALUES (2, 1, 'LUO_YANG', '洛阳', 'sad', 2, 0, '2025-03-27 15:52:39', '2025-03-27 20:53:21');
INSERT INTO `t_dict_data` VALUES (3, 1, 'ZHENG_ZHOU', '郑州', '', 0, 0, '2025-03-27 18:58:16', '2025-03-27 20:53:32');
INSERT INTO `t_dict_data` VALUES (7, 1, 'BEI_JING', '北京', '', 0, 0, '2025-03-27 20:53:45', '2025-03-27 20:53:45');
INSERT INTO `t_dict_data` VALUES (8, 1, 'SHANG_HAI', '上海', '', 0, 0, '2025-03-27 20:53:45', '2025-03-27 20:53:45');
INSERT INTO `t_dict_data` VALUES (9, 4, 'public', '公开', '', 0, 0, '2025-12-03 13:30:05', '2025-12-03 13:30:05');
INSERT INTO `t_dict_data` VALUES (10, 4, 'private', '私有', '', 0, 0, '2025-12-03 13:30:17', '2025-12-03 13:30:17');
INSERT INTO `t_dict_data` VALUES (11, 5, 'dify', 'dify', '', 0, 0, '2025-12-03 13:50:42', '2025-12-03 13:50:42');
INSERT INTO `t_dict_data` VALUES (12, 6, 'chat', '文本模型', '', 0, 0, '2026-01-28 01:52:07', '2026-01-28 01:52:07');
INSERT INTO `t_dict_data` VALUES (13, 6, 'embedding', '向量模型', '', 0, 0, '2026-01-28 01:52:27', '2026-01-28 01:52:27');
INSERT INTO `t_dict_data` VALUES (14, 7, '0', '否', '', 0, 0, '2026-01-28 01:57:05', '2026-01-28 01:57:05');
INSERT INTO `t_dict_data` VALUES (15, 7, '1', '是', '', 0, 0, '2026-01-28 01:59:25', '2026-01-28 01:59:25');
INSERT INTO `t_dict_data` VALUES (16, 8, 'openai', 'openai', '', 0, 0, '2026-01-28 02:01:27', '2026-01-28 02:01:27');
INSERT INTO `t_dict_data` VALUES (17, 9, 'pending', '待处理', '状态 pending-待处理 success-成功 fail-失败', 0, 0, '2026-01-28 12:39:22', '2026-01-28 12:39:22');
INSERT INTO `t_dict_data` VALUES (18, 9, 'success', '成功', '状态 pending-待处理 success-成功 fail-失败', 0, 0, '2026-01-28 12:39:33', '2026-01-28 12:39:33');
INSERT INTO `t_dict_data` VALUES (19, 9, 'fail', '失败', '状态 pending-待处理 success-成功 fail-失败', 0, 0, '2026-01-28 12:39:44', '2026-01-28 12:39:44');

-- ----------------------------
-- Table structure for t_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee`  (
  `employee_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `employee_uid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工uuid',
  `login_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录帐号',
  `login_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `actual_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT 0 COMMENT '性别',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `department_id` bigint NOT NULL COMMENT '部门id',
  `position_id` bigint NULL DEFAULT NULL COMMENT '职务ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `disabled_flag` tinyint UNSIGNED NOT NULL COMMENT '是否被禁用 0否1是',
  `deleted_flag` tinyint UNSIGNED NOT NULL COMMENT '是否删除0否 1是',
  `administrator_flag` tinyint NOT NULL DEFAULT 0 COMMENT '是否为超级管理员: 0 不是，1是',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`employee_id`) USING BTREE,
  UNIQUE INDEX `employee_uid_index`(`employee_uid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_employee
-- ----------------------------
INSERT INTO `t_employee` VALUES (1, 'cf1e361fd46741f5b2a09335cef50db8', 'admin', '$argon2id$v=19$m=16384,t=2,p=1$d9yQEAhck+haKxP2ZtXocg$NEnw3D2Ly8UbYpy2odATLA4ZflZ1FKJjWCuOGrVE4PM', '管理员', 'public/common/d99e85cb41094c98a11e7274c1b66780_20260315172541.png', 0, '13500000000', 1, 3, NULL, 0, 0, 1, NULL, '2026-03-15 09:25:46', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (2, '3c253628b4cb4302a7bb83008a82a415', 'huke', '$argon2id$v=19$m=16384,t=2,p=1$3N9HxhPdydtmqXmTmBUxcw$Yh2jMqQ5qmCC1cgezKtFd5vuH8WirHZh6FPnFS0clEY', '胡克', NULL, 0, '13123123121', 1, 4, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (44, '5e2a57cd8eff4346be03dc2acfed0d7c', 'zhuoda', '$argon2id$v=19$m=16384,t=2,p=1$Mt02VdlsDNrteY/sBOs2uw$0gI5gfb/D4iLGi6RRlEq/4Qo71cseuz5YZrwiCj3VQI', '卓大', NULL, 1, '18637925892', 1, 6, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (47, 'b031a061076a4732aa0d63989adb1fbc', 'shanyi', '$argon2id$v=19$m=16384,t=2,p=1$lsqZF68KCPkPaF2ShNhtNQ$Zpsv0GLBeau3x0hL0JzpWtnIlNf0hh3+P6Zu5fM6gJw', '善逸', 'public/common/f823b00873684f0a9d31f0d62316cc8e_20240630015141.jpg', 1, '17630506613', 2, 5, NULL, 0, 0, 0, '这个是备注', '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (48, 'e29327485b784211aa9677a9436d2e00', 'qinjiu', '$argon2id$v=19$m=16384,t=2,p=1$ga8Ww+zlLShAzC8o54qftg$3Ete1M8/zzepZqiEV1yNu/U7svMI0EuDWVKZ9X5M1uQ', '琴酒', NULL, 2, '14112343212', 2, 6, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (63, 'cab6922aeeb949a997c93c043b909b05', 'kaiyun', '$argon2id$v=19$m=16384,t=2,p=1$5TZB3BWsbv0FXrgA60+7ag$pnDVVvjE/J0kOet3xLq19fyv1+a/KGqN6B+xsvDluYc', '开云', NULL, 0, '13112312346', 2, 5, 'ss@qq.com', 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (64, '02ce19c1c707448a81159834a60bbd94', 'qingye', '$argon2id$v=19$m=16384,t=2,p=1$X+M3CF1557PGfLavpWXCPQ$2LsEiOgLFP+VbGA/7TPAbLnkyiLollova6iETB9S/ds', '清野', NULL, 1, '13123123111', 2, 4, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (65, 'e791135b86c34435873f4c9068c0e9ba', 'feiye', '$argon2id$v=19$m=16384,t=2,p=1$cPMw0Xu3dgu4lFX1x+qUvQ$Ol6NktMqi2fGn4Djv+m5ha/DyARWkXA/y784hFVa0rQ', '飞叶', NULL, 1, '13123123112', 4, 3, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (66, '2954e985557745df844e4c88532cd8a6', 'luoyi', '$argon2id$v=19$m=16384,t=2,p=1$D0lXN4LyhLhtHaKFbS3DRw$0FK9A8F1oT38xqIZvNcu1eWsB5C5vXkwULXhvLxYmK8', '罗伊', NULL, 1, '13123123142', 4, 2, NULL, 1, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (67, '39cb2c7de94141d6824e9a167912c23d', 'chuxiao', '$argon2id$v=19$m=16384,t=2,p=1$/BdtVk/U5utWvple9bfCQw$eK8JjH+cei7gNQwPDDdP5ACQT3qkYvz5Qk4k016jRpU', '初晓', NULL, 1, '13123123123', 1, 2, NULL, 1, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee` VALUES (68, '2aaf8c8c393c46b080aca86179388d7e', 'xuanpeng', '$argon2id$v=19$m=16384,t=2,p=1$ldHEjEwCWur/RnSy0JmFJQ$nlhVYiFMELToZ9nXI5QxG4maTV/L7pyPU0GRv3+s+tg', '玄朋', NULL, 1, '13123123124', 1, 3, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');

-- ----------------------------
-- Table structure for t_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback`  (
  `feedback_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `feedback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '反馈内容',
  `feedback_attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '反馈图片',
  `user_id` bigint NOT NULL COMMENT '创建人id',
  `user_type` int NOT NULL COMMENT '创建人用户类型',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '意见反馈' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_feedback
-- ----------------------------

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`  (
  `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `folder_type` tinyint UNSIGNED NOT NULL COMMENT '文件夹类型',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_size` int NULL DEFAULT NULL COMMENT '文件大小',
  `file_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件key，用于文件下载',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建人，即上传人',
  `creator_user_type` int NULL DEFAULT NULL COMMENT '创建人用户类型',
  `creator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上次更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE INDEX `uk_file_key`(`file_key` ASC) USING BTREE,
  INDEX `module_id_module_type`(`folder_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_file
-- ----------------------------
INSERT INTO `t_file` VALUES (108, 1, 'DeepSeek.docx', 22330, 'public/common/7a5ad16c79934726b1f2fa4fa46de5d3_20260131194251.docx', 'docx', 1, 1, '管理员', '2026-01-31 11:42:51', '2026-01-31 11:42:51');
INSERT INTO `t_file` VALUES (109, 1, '员工年度考核表(陈铭雄).docx', 18268, 'public/common/4fb40c1f37734639815e880051fb2493_20260226201717.docx', 'docx', 1, 1, '管理员', '2026-02-26 12:17:17', '2026-02-26 12:17:17');
INSERT INTO `t_file` VALUES (110, 1, '设计ms小店图标.png', 2985598, 'public/common/d99e85cb41094c98a11e7274c1b66780_20260315172541.png', 'png', 1, 1, '管理员', '2026-03-15 09:25:46', '2026-03-15 09:25:46');

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods`  (
  `goods_id` int NOT NULL AUTO_INCREMENT,
  `goods_status` int NULL DEFAULT NULL COMMENT '商品状态:[1:预约中,2:售卖中,3:售罄]',
  `category_id` int NOT NULL COMMENT '商品类目',
  `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产地',
  `price` decimal(10, 2) UNSIGNED NOT NULL COMMENT '价格',
  `shelves_flag` tinyint UNSIGNED NOT NULL COMMENT '上架状态',
  `deleted_flag` tinyint UNSIGNED NOT NULL COMMENT '删除状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_goods
-- ----------------------------
INSERT INTO `t_goods` VALUES (1, 1, 353, 'Mote60', 'BEI_JING', 9999.00, 1, 0, NULL, '2022-10-21 19:57:49', '2021-09-01 22:25:30');
INSERT INTO `t_goods` VALUES (7, 1, 352, 'iphone15 pro', 'LUO_YANG', 50000.00, 1, 0, '备注', '2024-06-16 09:34:08', '2022-10-21 19:58:07');
INSERT INTO `t_goods` VALUES (8, 1, 352, 'iphone14', 'ZHENG_ZHOU', 150.00, 0, 0, '', '2022-10-21 19:12:49', '2022-10-21 19:00:11');
INSERT INTO `t_goods` VALUES (10, 1, 357, '小米15', 'LUO_YANG', 7999.00, 1, 0, '', '2023-10-07 19:02:24', '2023-10-07 19:02:24');
INSERT INTO `t_goods` VALUES (11, 1, 354, '青轴键盘', 'ZHENG_ZHOU', 199.00, 1, 0, '支持usb', '2023-12-01 19:58:09', '2023-12-01 19:55:53');
INSERT INTO `t_goods` VALUES (12, 1, 356, '罗技双模鼠标', 'BEI_JING,ZHENG_ZHOU', 99.00, 0, 0, '支持蓝牙', '2024-09-03 21:06:32', '2023-12-01 19:57:25');

-- ----------------------------
-- Table structure for t_heart_beat_record
-- ----------------------------
DROP TABLE IF EXISTS `t_heart_beat_record`;
CREATE TABLE `t_heart_beat_record`  (
  `heart_beat_record_id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `project_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
  `server_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器ip',
  `process_no` int NOT NULL COMMENT '进程号',
  `process_start_time` datetime NOT NULL COMMENT '进程开启时间',
  `heart_beat_time` datetime NOT NULL COMMENT '心跳时间',
  PRIMARY KEY (`heart_beat_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 245 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公用服务 - 服务心跳' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_heart_beat_record
-- ----------------------------
INSERT INTO `t_heart_beat_record` VALUES (188, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.103;192.168.15.1;172.18.48.1', 20448, '2025-12-03 21:02:15', '2025-12-03 21:59:43');
INSERT INTO `t_heart_beat_record` VALUES (189, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.103;192.168.15.1;172.18.48.1', 45948, '2025-12-03 22:04:33', '2025-12-03 22:10:43');
INSERT INTO `t_heart_beat_record` VALUES (190, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.163.58;192.168.15.1;172.18.48.1', 49064, '2025-12-03 22:25:21', '2025-12-03 22:26:38');
INSERT INTO `t_heart_beat_record` VALUES (191, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.163.58;192.168.15.1;172.18.48.1', 34796, '2025-12-03 22:27:03', '2025-12-03 22:33:16');
INSERT INTO `t_heart_beat_record` VALUES (192, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.163.58;192.168.15.1;172.18.48.1', 22864, '2025-12-03 22:33:28', '2025-12-03 22:34:45');
INSERT INTO `t_heart_beat_record` VALUES (193, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.163.58;192.168.15.1;172.18.48.1', 47264, '2025-12-03 22:36:36', '2025-12-04 21:54:10');
INSERT INTO `t_heart_beat_record` VALUES (194, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.102;192.168.15.1;172.24.64.1;172.17.112.1;198.18.0.1', 8132, '2025-12-14 13:52:23', '2025-12-14 15:48:22');
INSERT INTO `t_heart_beat_record` VALUES (195, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.103;192.168.15.1;172.30.240.1', 104816, '2025-12-21 15:58:05', '2025-12-21 20:52:42');
INSERT INTO `t_heart_beat_record` VALUES (196, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 31520, '2025-12-24 21:17:59', '2025-12-24 21:34:11');
INSERT INTO `t_heart_beat_record` VALUES (197, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 13856, '2025-12-24 21:36:47', '2025-12-24 22:34:52');
INSERT INTO `t_heart_beat_record` VALUES (198, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 30772, '2025-12-24 22:38:14', '2025-12-24 22:44:26');
INSERT INTO `t_heart_beat_record` VALUES (199, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 36216, '2025-12-24 22:48:54', '2025-12-24 22:50:04');
INSERT INTO `t_heart_beat_record` VALUES (200, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 43288, '2025-12-24 22:50:22', '2025-12-24 22:51:36');
INSERT INTO `t_heart_beat_record` VALUES (201, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 35588, '2025-12-24 22:52:59', '2025-12-24 22:54:09');
INSERT INTO `t_heart_beat_record` VALUES (202, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.26.48.1', 28488, '2025-12-24 22:55:17', '2025-12-24 22:56:26');
INSERT INTO `t_heart_beat_record` VALUES (203, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.102;192.168.15.1;172.20.112.1;172.17.112.1', 78204, '2026-01-17 18:07:24', '2026-01-17 20:57:21');
INSERT INTO `t_heart_beat_record` VALUES (204, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.102;192.168.15.1;172.20.112.1;172.17.112.1', 95920, '2026-01-17 21:33:16', '2026-01-17 22:58:59');
INSERT INTO `t_heart_beat_record` VALUES (205, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;10.17.131.58;192.168.15.1;172.31.192.1', 12424, '2026-01-26 21:16:07', '2026-01-26 21:27:18');
INSERT INTO `t_heart_beat_record` VALUES (206, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;2.0.0.1;192.168.248.124', 399128, '2026-01-28 09:20:06', '2026-01-28 10:06:32');
INSERT INTO `t_heart_beat_record` VALUES (207, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;2.0.0.1;192.168.248.124', 132780, '2026-01-28 10:10:53', '2026-01-28 10:22:17');
INSERT INTO `t_heart_beat_record` VALUES (208, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;2.0.0.1;192.168.248.124', 184036, '2026-01-28 10:22:35', '2026-01-28 14:39:13');
INSERT INTO `t_heart_beat_record` VALUES (209, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 36564, '2026-01-28 20:20:54', '2026-01-28 20:27:11');
INSERT INTO `t_heart_beat_record` VALUES (210, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 12536, '2026-01-28 20:28:41', '2026-01-28 20:44:57');
INSERT INTO `t_heart_beat_record` VALUES (211, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 44524, '2026-01-28 20:48:38', '2026-01-28 21:21:04');
INSERT INTO `t_heart_beat_record` VALUES (212, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 28808, '2026-01-28 21:25:38', '2026-01-29 20:43:19');
INSERT INTO `t_heart_beat_record` VALUES (213, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 21100, '2026-01-29 20:49:08', '2026-01-29 20:50:27');
INSERT INTO `t_heart_beat_record` VALUES (214, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 42764, '2026-01-29 20:52:47', '2026-01-29 21:08:57');
INSERT INTO `t_heart_beat_record` VALUES (215, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.31.192.1', 52840, '2026-01-29 21:14:07', '2026-01-29 22:11:53');
INSERT INTO `t_heart_beat_record` VALUES (216, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.31.192.1', 107972, '2026-01-31 19:37:22', '2026-01-31 19:43:46');
INSERT INTO `t_heart_beat_record` VALUES (217, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.101;192.168.15.1;172.31.192.1', 121396, '2026-01-31 19:48:00', '2026-02-01 15:13:15');
INSERT INTO `t_heart_beat_record` VALUES (218, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.21.0.1', 10212, '2026-02-26 20:15:09', '2026-02-26 20:51:31');
INSERT INTO `t_heart_beat_record` VALUES (219, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.21.0.1', 34380, '2026-02-26 21:00:13', '2026-02-26 21:01:30');
INSERT INTO `t_heart_beat_record` VALUES (220, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.21.0.1', 32576, '2026-02-26 21:01:39', '2026-02-26 21:03:36');
INSERT INTO `t_heart_beat_record` VALUES (221, 'C:\\Users\\Mission\\Desktop\\demo\\smart-admin', '127.0.0.1;192.168.23.1;192.168.0.100;192.168.15.1;172.21.0.1', 36192, '2026-02-26 21:07:56', '2026-02-27 18:29:52');
INSERT INTO `t_heart_beat_record` VALUES (222, 'F:\\demo\\smart-admin', '127.0.0.1;10.65.115.58;172.28.176.1', 26308, '2026-03-04 20:19:43', '2026-03-04 20:41:07');
INSERT INTO `t_heart_beat_record` VALUES (223, 'F:\\demo\\smart-admin', '127.0.0.1;10.65.115.58;172.28.176.1', 29412, '2026-03-04 21:08:14', '2026-03-04 21:09:32');
INSERT INTO `t_heart_beat_record` VALUES (224, 'F:\\demo\\smart-admin', '127.0.0.1;10.65.115.58;172.28.176.1', 19704, '2026-03-04 21:13:53', '2026-03-04 21:45:12');
INSERT INTO `t_heart_beat_record` VALUES (225, 'F:\\demo\\smart-admin', '127.0.0.1;10.65.115.58;172.28.176.1', 31848, '2026-03-04 21:45:29', '2026-03-04 21:47:04');
INSERT INTO `t_heart_beat_record` VALUES (226, 'F:\\demo\\smart-admin', '127.0.0.1;10.65.115.58;172.28.176.1', 1924, '2026-03-04 21:50:20', '2026-03-04 21:57:01');
INSERT INTO `t_heart_beat_record` VALUES (227, 'F:\\demo\\smart-admin', '127.0.0.1;10.65.115.58;172.28.176.1', 29948, '2026-03-04 21:57:22', '2026-03-04 21:58:52');
INSERT INTO `t_heart_beat_record` VALUES (228, 'F:\\project\\smart-admin', '127.0.0.1;192.168.0.103;172.29.176.1;192.168.74.1;192.168.70.1', 30432, '2026-03-15 16:32:20', '2026-03-15 17:04:18');
INSERT INTO `t_heart_beat_record` VALUES (229, 'F:\\project\\smart-admin', '127.0.0.1;192.168.0.103;172.29.176.1;192.168.74.1;192.168.70.1', 29076, '2026-03-15 17:15:16', '2026-03-15 17:16:34');
INSERT INTO `t_heart_beat_record` VALUES (230, 'F:\\project\\smart-admin', '127.0.0.1;192.168.0.103;172.29.176.1;192.168.74.1;192.168.70.1', 39636, '2026-03-15 17:19:17', '2026-03-15 17:40:35');
INSERT INTO `t_heart_beat_record` VALUES (231, 'F:\\project\\smart-admin', '127.0.0.1;192.168.0.103;172.29.176.1;192.168.74.1;192.168.70.1', 40596, '2026-03-15 17:41:40', '2026-03-15 18:13:19');
INSERT INTO `t_heart_beat_record` VALUES (232, 'F:\\project\\smart-admin', '127.0.0.1;192.168.0.103;172.29.176.1;192.168.74.1;192.168.70.1', 10104, '2026-03-15 18:16:51', '2026-03-15 18:28:11');
INSERT INTO `t_heart_beat_record` VALUES (233, 'F:\\project\\smart-admin', '127.0.0.1;192.168.0.103;172.29.176.1;192.168.74.1;192.168.70.1', 7428, '2026-03-15 18:29:14', '2026-03-16 11:25:09');
INSERT INTO `t_heart_beat_record` VALUES (234, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;2.0.0.1;10.239.211.18', 12032, '2026-03-16 15:04:46', '2026-03-16 15:06:34');
INSERT INTO `t_heart_beat_record` VALUES (235, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;2.0.0.1;10.239.211.18', 34536, '2026-03-16 15:07:04', '2026-03-16 15:18:51');
INSERT INTO `t_heart_beat_record` VALUES (236, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;2.0.0.1;10.239.211.18', 2488, '2026-03-16 15:57:21', '2026-03-16 15:59:13');
INSERT INTO `t_heart_beat_record` VALUES (237, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;10.239.211.18', 20648, '2026-03-17 08:31:42', '2026-03-17 09:28:39');
INSERT INTO `t_heart_beat_record` VALUES (238, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;10.239.211.18', 13556, '2026-03-17 09:28:52', '2026-03-17 09:30:41');
INSERT INTO `t_heart_beat_record` VALUES (239, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;10.239.211.18', 8500, '2026-03-17 09:37:44', '2026-03-17 10:24:36');
INSERT INTO `t_heart_beat_record` VALUES (240, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;10.239.211.18', 34020, '2026-03-17 11:45:19', '2026-03-17 11:47:10');
INSERT INTO `t_heart_beat_record` VALUES (241, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;10.239.211.18', 53076, '2026-03-17 14:31:20', '2026-03-17 14:48:11');
INSERT INTO `t_heart_beat_record` VALUES (242, 'C:\\Users\\Mission\\Desktop\\myProject\\smart-admin', '127.0.0.1;192.168.91.116;10.239.211.18', 54104, '2026-03-17 14:52:25', '2026-03-17 15:49:15');
INSERT INTO `t_heart_beat_record` VALUES (243, 'F:\\project\\smart-admin', '127.0.0.1;10.239.211.58;172.29.176.1;192.168.74.1;192.168.70.1', 24336, '2026-03-17 20:13:37', '2026-03-17 20:14:55');
INSERT INTO `t_heart_beat_record` VALUES (244, 'F:\\project\\smart-admin', '127.0.0.1;10.239.211.58;172.29.176.1;192.168.74.1;192.168.70.1', 53716, '2026-03-17 20:17:36', '2026-03-17 20:23:55');

-- ----------------------------
-- Table structure for t_help_doc
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc`;
CREATE TABLE `t_help_doc`  (
  `help_doc_id` bigint NOT NULL AUTO_INCREMENT,
  `help_doc_catalog_id` bigint NOT NULL COMMENT '类型1公告 2动态',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文本内容',
  `content_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'html内容',
  `attachment` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `page_view_count` int NOT NULL DEFAULT 0 COMMENT '页面浏览量，传说中的pv',
  `user_view_count` int NOT NULL DEFAULT 0 COMMENT '用户浏览量，传说中的uv',
  `author` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`help_doc_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帮助文档' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_help_doc
-- ----------------------------
INSERT INTO `t_help_doc` VALUES (32, 6, '企业名称该写什么？', '需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 57, 2, '卓大', '2026-01-29 13:22:47', '2022-11-22 10:41:48');
INSERT INTO `t_help_doc` VALUES (33, 6, '谁有权限查看企业信息', '需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 15, 2, '卓大', '2026-01-29 13:22:49', '2022-11-22 10:42:19');

-- ----------------------------
-- Table structure for t_help_doc_catalog
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc_catalog`;
CREATE TABLE `t_help_doc_catalog`  (
  `help_doc_catalog_id` bigint NOT NULL AUTO_INCREMENT COMMENT '帮助文档目录',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序字段',
  `parent_id` bigint NOT NULL COMMENT '父级id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`help_doc_catalog_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帮助文档-目录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_help_doc_catalog
-- ----------------------------
INSERT INTO `t_help_doc_catalog` VALUES (6, '企业信息', 0, 0, '2022-11-05 10:52:40', '2022-11-22 10:37:38');
INSERT INTO `t_help_doc_catalog` VALUES (9, '企业信用', 0, 6, '2023-12-01 20:16:54', '2023-12-01 20:16:54');
INSERT INTO `t_help_doc_catalog` VALUES (10, '采购文档', 0, 11, '2023-12-01 20:17:08', '2023-12-01 20:17:29');
INSERT INTO `t_help_doc_catalog` VALUES (11, '进销存', 0, 0, '2023-12-01 20:17:23', '2023-12-01 20:17:23');

-- ----------------------------
-- Table structure for t_help_doc_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc_relation`;
CREATE TABLE `t_help_doc_relation`  (
  `relation_id` bigint NOT NULL COMMENT '关联id',
  `relation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联名称',
  `help_doc_id` bigint NOT NULL COMMENT '文档id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`relation_id`, `help_doc_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帮助文档-关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_help_doc_relation
-- ----------------------------
INSERT INTO `t_help_doc_relation` VALUES (0, '首页', 32, '2023-12-04 13:34:17', '2023-12-04 13:34:17');
INSERT INTO `t_help_doc_relation` VALUES (0, '首页', 33, '2023-12-04 13:34:21', '2023-12-04 13:34:21');

-- ----------------------------
-- Table structure for t_help_doc_view_record
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc_view_record`;
CREATE TABLE `t_help_doc_view_record`  (
  `help_doc_id` bigint NOT NULL COMMENT '通知公告id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `page_view_count` int NULL DEFAULT 0 COMMENT '查看次数',
  `first_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首次ip',
  `first_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首次用户设备等标识',
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次ip',
  `last_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次用户设备等标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`help_doc_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帮助文档-查看记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_help_doc_view_record
-- ----------------------------
INSERT INTO `t_help_doc_view_record` VALUES (32, 1, '管理员', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '2026-01-29 13:22:40', '2026-01-29 13:22:47');
INSERT INTO `t_help_doc_view_record` VALUES (33, 1, '管理员', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '2026-01-29 13:22:46', '2026-01-29 13:22:49');

-- ----------------------------
-- Table structure for t_login_fail
-- ----------------------------
DROP TABLE IF EXISTS `t_login_fail`;
CREATE TABLE `t_login_fail`  (
  `login_fail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `login_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录名',
  `login_fail_count` int NULL DEFAULT NULL COMMENT '连续登录失败次数',
  `lock_flag` tinyint NULL DEFAULT 0 COMMENT '锁定状态:1锁定，0未锁定',
  `login_lock_begin_time` datetime NULL DEFAULT NULL COMMENT '连续登录失败锁定开始时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`login_fail_id`) USING BTREE,
  UNIQUE INDEX `uid_and_utype`(`user_id` ASC, `user_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录失败次数记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_login_fail
-- ----------------------------

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log`  (
  `login_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `user_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `login_ip` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ip',
  `login_ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ip地区',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'user-agent信息',
  `login_device` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录设备',
  `login_result` int NOT NULL COMMENT '登录结果：0成功 1失败 2 退出',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`login_log_id`) USING BTREE,
  INDEX `customer_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1927 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_login_log
-- ----------------------------
INSERT INTO `t_login_log` VALUES (1905, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', '电脑端', 0, '', '2025-12-03 13:02:41', '2025-12-03 21:02:41');
INSERT INTO `t_login_log` VALUES (1906, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', '电脑端', 0, '', '2025-12-04 11:59:44', '2025-12-04 19:59:44');
INSERT INTO `t_login_log` VALUES (1907, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '电脑端', 0, '', '2025-12-21 08:00:30', '2025-12-21 16:00:30');
INSERT INTO `t_login_log` VALUES (1908, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '电脑端', 0, '', '2025-12-24 13:20:21', '2025-12-24 21:20:22');
INSERT INTO `t_login_log` VALUES (1909, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '电脑端', 0, '', '2025-12-24 14:39:34', '2025-12-24 22:39:34');
INSERT INTO `t_login_log` VALUES (1910, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-01-28 01:30:20', '2026-01-28 09:30:19');
INSERT INTO `t_login_log` VALUES (1911, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-01-28 12:21:32', '2026-01-28 20:21:32');
INSERT INTO `t_login_log` VALUES (1912, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-01-28 13:21:59', '2026-01-28 21:21:59');
INSERT INTO `t_login_log` VALUES (1913, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-01-29 12:43:35', '2026-01-29 20:43:35');
INSERT INTO `t_login_log` VALUES (1914, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-01-31 11:39:24', '2026-01-31 19:39:24');
INSERT INTO `t_login_log` VALUES (1915, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-02-26 12:16:04', '2026-02-26 20:16:04');
INSERT INTO `t_login_log` VALUES (1916, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-02-26 13:00:55', '2026-02-26 21:00:55');
INSERT INTO `t_login_log` VALUES (1917, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-04 12:37:09', '2026-03-04 20:37:08');
INSERT INTO `t_login_log` VALUES (1918, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-15 08:39:22', '2026-03-15 16:39:18');
INSERT INTO `t_login_log` VALUES (1919, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-15 09:17:32', '2026-03-15 17:17:28');
INSERT INTO `t_login_log` VALUES (1920, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-16 07:06:03', '2026-03-16 15:06:02');
INSERT INTO `t_login_log` VALUES (1921, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-17 00:33:01', '2026-03-17 08:32:59');
INSERT INTO `t_login_log` VALUES (1922, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-17 01:45:46', '2026-03-17 09:45:43');
INSERT INTO `t_login_log` VALUES (1923, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-17 03:46:39', '2026-03-17 11:46:36');
INSERT INTO `t_login_log` VALUES (1924, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-17 06:34:14', '2026-03-17 14:34:15');
INSERT INTO `t_login_log` VALUES (1925, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-17 06:34:14', '2026-03-17 14:34:15');
INSERT INTO `t_login_log` VALUES (1926, 1, 1, '管理员', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', '电脑端', 0, '', '2026-03-17 12:06:18', '2026-03-17 20:06:10');

-- ----------------------------
-- Table structure for t_mail_template
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_template`;
CREATE TABLE `t_mail_template`  (
  `template_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `template_subject` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `template_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板内容',
  `template_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '解析类型 string，freemarker',
  `disable_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`template_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_mail_template
-- ----------------------------
INSERT INTO `t_mail_template` VALUES ('login_verification_code', '登录验证码', '<!DOCTYPE HTML>\r\n<html>\r\n<head>\r\n  <title>登录提醒</title>\r\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n  <style>\r\n      * {\r\n          font-family: SimSun;\r\n          /* 4号字体 */\r\n          font-size: 18px;\r\n          /* 22磅行间距 */\r\n          line-height: 29px;\r\n      }\r\n\r\n      .main_font_size {\r\n          font-size: 12.0pt;\r\n      }\r\n\r\n      .mainContent {\r\n          line-height: 28px;\r\n      }\r\n\r\n      p {\r\n          margin: 0 auto;\r\n          text-align: justify;\r\n      }\r\n  </style>\r\n\r\n</head>\r\n<body>\r\n<div>\r\n  <div style=\"margin: 0px auto;width: 690px;\">\r\n    <div class=\"mainContent\">\r\n      <h1>验证码</h1>\r\n      <p>请在验证页面输入此验证码</p>\r\n      <p><b>${code}</b></p>\r\n      <p>验证码将于此电子邮件发出 5 分钟后过期。</p>\r\n      <p>如果你未曾提出此请求，可以忽略这封电子邮件。</p>\r\n    </div>\r\n\r\n  </div>\r\n</div>\r\n</body>\r\n</html>', 'freemarker', 0, '2024-08-06 09:13:08', '2024-07-28 13:56:06');

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_type` int NOT NULL COMMENT '类型',
  `parent_id` bigint NOT NULL COMMENT '父菜单ID',
  `sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `perms_type` int NULL DEFAULT NULL COMMENT '权限类型',
  `api_perms` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端权限字符串',
  `web_perms` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端权限字符串',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `context_menu_id` bigint NULL DEFAULT NULL COMMENT '功能点关联菜单ID',
  `frame_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为外链',
  `frame_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '外链地址',
  `cache_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否缓存',
  `visible_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '显示状态',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 341 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (26, '菜单管理', 2, 50, 1, '/menu/list', '/system/menu/menu-list.vue', NULL, NULL, NULL, 'CopyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, '2021-08-09 15:04:35', 1, '2023-12-01 19:39:03');
INSERT INTO `t_menu` VALUES (40, '删除', 3, 26, NULL, NULL, NULL, 1, 'system:menu:batchDelete', 'system:menu:batchDelete', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 09:45:56', 1, '2023-10-07 18:15:50');
INSERT INTO `t_menu` VALUES (45, '组织架构', 1, 0, 3, '/organization', NULL, NULL, NULL, NULL, 'UserSwitchOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 16:13:27', 1, '2024-07-02 19:27:44');
INSERT INTO `t_menu` VALUES (46, '员工管理', 2, 45, 3, '/organization/employee', '/system/employee/index.vue', NULL, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 16:21:50', 1, '2024-07-02 20:15:23');
INSERT INTO `t_menu` VALUES (47, '商品管理', 2, 48, 1, '/erp/goods/list', '/business/erp/goods/goods-list.vue', NULL, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2021-08-12 17:58:39', 1, '2023-12-01 19:33:08');
INSERT INTO `t_menu` VALUES (48, '商品管理', 1, 138, 3, '/goods', NULL, NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 18:02:59', 1, '2024-07-08 13:58:46');
INSERT INTO `t_menu` VALUES (50, '系统设置', 1, 0, 6, '/setting', NULL, NULL, NULL, NULL, 'SettingOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-13 16:41:33', 1, '2023-12-01 19:38:03');
INSERT INTO `t_menu` VALUES (76, '角色管理', 2, 45, 4, '/organization/role', '/system/role/index.vue', NULL, NULL, NULL, 'SlidersOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-26 10:31:00', 1, '2024-07-02 20:15:28');
INSERT INTO `t_menu` VALUES (78, '商品分类', 2, 48, 2, '/erp/catalog/goods', '/business/erp/catalog/goods-catalog.vue', NULL, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2022-05-18 23:34:14', 1, '2023-12-01 19:33:13');
INSERT INTO `t_menu` VALUES (79, '自定义分组', 2, 48, 3, '/erp/catalog/custom', '/business/erp/catalog/custom-catalog.vue', NULL, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-18 23:37:53', 1, '2023-12-01 19:33:16');
INSERT INTO `t_menu` VALUES (81, '用户操作记录', 2, 213, 6, '/support/operate-log/operate-log-list', '/support/operate-log/operate-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-20 12:37:24', 44, '2024-08-13 14:34:10');
INSERT INTO `t_menu` VALUES (85, '组件演示', 2, 84, NULL, '/demonstration/index', '/support/demonstration/index.vue', NULL, NULL, NULL, 'ClearOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-20 23:16:46', NULL, '2022-05-20 23:16:46');
INSERT INTO `t_menu` VALUES (86, '添加部门', 3, 46, 1, NULL, NULL, 1, 'system:department:add', 'system:department:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:33:37', 1, '2023-10-07 18:26:35');
INSERT INTO `t_menu` VALUES (87, '修改部门', 3, 46, 2, NULL, NULL, 1, 'system:department:update', 'system:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:34:11', 1, '2023-10-07 18:26:44');
INSERT INTO `t_menu` VALUES (88, '删除部门', 3, 46, 3, NULL, NULL, 1, 'system:department:delete', 'system:department:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:34:49', 1, '2023-10-07 18:26:49');
INSERT INTO `t_menu` VALUES (91, '添加员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:add', 'system:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:11:38', 1, '2023-10-07 18:27:46');
INSERT INTO `t_menu` VALUES (92, '编辑员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:update', 'system:employee:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:10', 1, '2023-10-07 18:27:49');
INSERT INTO `t_menu` VALUES (93, '禁用启用员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:disabled', 'system:employee:disabled', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:37', 1, '2023-10-07 18:27:53');
INSERT INTO `t_menu` VALUES (94, '调整员工部门', 3, 46, NULL, NULL, NULL, 1, 'system:employee:department:update', 'system:employee:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:59', 1, '2023-10-07 18:27:34');
INSERT INTO `t_menu` VALUES (95, '重置密码', 3, 46, NULL, NULL, NULL, 1, 'system:employee:password:reset', 'system:employee:password:reset', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:13:30', 1, '2023-10-07 18:27:57');
INSERT INTO `t_menu` VALUES (96, '删除员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:delete', 'system:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:14:08', 1, '2023-10-07 18:28:01');
INSERT INTO `t_menu` VALUES (97, '添加角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:add', 'system:role:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:00', 1, '2023-10-07 18:42:31');
INSERT INTO `t_menu` VALUES (98, '删除角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:delete', 'system:role:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:19', 1, '2023-10-07 18:42:35');
INSERT INTO `t_menu` VALUES (99, '编辑角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:update', 'system:role:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:55', 1, '2023-10-07 18:42:44');
INSERT INTO `t_menu` VALUES (100, '更新数据范围', 3, 76, NULL, NULL, NULL, 1, 'system:role:dataScope:update', 'system:role:dataScope:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:37:03', 1, '2023-10-07 18:41:49');
INSERT INTO `t_menu` VALUES (101, '批量移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:batch:delete', 'system:role:employee:batch:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:05', 1, '2023-10-07 18:43:32');
INSERT INTO `t_menu` VALUES (102, '移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:delete', 'system:role:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:21', 1, '2023-10-07 18:43:37');
INSERT INTO `t_menu` VALUES (103, '添加员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:add', 'system:role:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:38', 1, '2023-10-07 18:44:05');
INSERT INTO `t_menu` VALUES (104, '修改权限', 3, 76, NULL, NULL, NULL, 1, 'system:role:menu:update', 'system:role:menu:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:41:55', 1, '2023-10-07 18:44:11');
INSERT INTO `t_menu` VALUES (105, '添加', 3, 26, NULL, NULL, NULL, 1, 'system:menu:add', 'system:menu:add', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:44:37', 1, '2023-10-07 17:35:35');
INSERT INTO `t_menu` VALUES (106, '编辑', 3, 26, NULL, NULL, NULL, 1, 'system:menu:update', 'system:menu:update', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:44:59', 1, '2023-10-07 17:35:48');
INSERT INTO `t_menu` VALUES (109, '参数配置', 2, 50, 3, '/config/config-list', '/support/config/config-list.vue', NULL, NULL, NULL, 'AntDesignOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 13:34:41', 1, '2022-06-23 16:24:16');
INSERT INTO `t_menu` VALUES (110, '数据字典', 2, 50, 4, '/setting/dict', '/support/dict/index.vue', NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 17:53:00', 1, '2022-05-27 18:09:14');
INSERT INTO `t_menu` VALUES (111, '监控服务', 1, 0, 100, '/monitor', NULL, NULL, NULL, NULL, 'BarChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-17 11:13:23', 1, '2023-11-28 17:43:56');
INSERT INTO `t_menu` VALUES (113, '查询', 3, 112, NULL, NULL, NULL, NULL, NULL, 'ad', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-17 11:31:36', NULL, '2022-06-17 11:31:36');
INSERT INTO `t_menu` VALUES (114, '运维工具', 1, 0, 200, NULL, NULL, NULL, NULL, NULL, 'NodeCollapseOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2022-06-20 10:09:16', 1, '2023-12-01 19:36:18');
INSERT INTO `t_menu` VALUES (117, 'Reload', 2, 50, 12, '/hook', '/support/reload/reload-list.vue', NULL, NULL, NULL, 'ReloadOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-20 10:16:49', 1, '2023-12-01 19:39:17');
INSERT INTO `t_menu` VALUES (122, '数据库监控', 2, 111, 4, '/support/druid/index', NULL, NULL, NULL, NULL, 'ConsoleSqlOutlined', NULL, 1, 'http://localhost:1024/druid', 1, 1, 0, 0, 1, '2022-06-20 14:49:33', 1, '2023-02-16 19:15:58');
INSERT INTO `t_menu` VALUES (130, '单号管理', 2, 50, 6, '/support/serial-number/serial-number-list', '/support/serial-number/serial-number-list.vue', NULL, NULL, NULL, 'NumberOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 14:45:22', 1, '2022-06-28 16:23:41');
INSERT INTO `t_menu` VALUES (132, '公告管理', 2, 138, 2, '/oa/notice/notice-list', '/business/oa/notice/notice-list.vue', NULL, NULL, NULL, 'SoundOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2022-06-24 18:23:09', 1, '2024-07-08 13:58:51');
INSERT INTO `t_menu` VALUES (133, '缓存管理', 2, 50, 11, '/support/cache/cache-list', '/support/cache/cache-list.vue', NULL, NULL, NULL, 'BorderInnerOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 18:52:25', 1, '2023-12-01 19:39:13');
INSERT INTO `t_menu` VALUES (138, '功能Demo', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 0, 0, 1, 0, 1, '2022-06-24 20:09:18', 1, '2026-03-15 09:33:30');
INSERT INTO `t_menu` VALUES (142, '公告详情', 2, 132, NULL, '/oa/notice/notice-detail', '/business/oa/notice/notice-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-06-25 16:38:47', 1, '2022-09-14 19:46:17');
INSERT INTO `t_menu` VALUES (143, '登录登出记录', 2, 213, 5, '/support/login-log/login-log-list', '/support/login-log/login-log-list.vue', NULL, NULL, NULL, 'LoginOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-28 15:01:38', 44, '2024-08-13 14:33:49');
INSERT INTO `t_menu` VALUES (144, '企业管理', 2, 138, 1, '/oa/enterprise/enterprise-list', '/business/oa/enterprise/enterprise-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 17:00:07', 1, '2024-07-08 13:48:24');
INSERT INTO `t_menu` VALUES (145, '企业详情', 2, 138, NULL, '/oa/enterprise/enterprise-detail', '/business/oa/enterprise/enterprise-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 18:52:52', 1, '2022-11-22 10:39:07');
INSERT INTO `t_menu` VALUES (147, '帮助文档', 2, 218, 1, '/help-doc/help-doc-manage-list', '/support/help-doc/management/help-doc-manage-list.vue', NULL, NULL, NULL, 'FolderViewOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 19:59:01', 1, '2023-12-01 19:38:23');
INSERT INTO `t_menu` VALUES (148, '意见反馈', 2, 218, 2, '/feedback/feedback-list', '/support/feedback/feedback-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 19:59:52', 1, '2023-12-01 19:38:40');
INSERT INTO `t_menu` VALUES (149, '我的通知', 2, 132, NULL, '/oa/notice/notice-employee-list', '/business/oa/notice/notice-employee-list.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 20:29:41', 1, '2022-09-14 20:31:23');
INSERT INTO `t_menu` VALUES (150, '我的通知公告详情', 2, 132, NULL, '/oa/notice/notice-employee-detail', '/business/oa/notice/notice-employee-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 20:30:25', 1, '2022-09-14 20:31:38');
INSERT INTO `t_menu` VALUES (151, '代码生成', 2, 0, 600, '/support/code-generator', '/support/code-generator/code-generator-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-21 18:25:05', 1, '2022-10-22 11:27:58');
INSERT INTO `t_menu` VALUES (152, '更新日志', 2, 218, 3, '/support/change-log/change-log-list', '/support/change-log/change-log-list.vue', NULL, NULL, NULL, 'HeartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 44, '2022-10-10 10:31:20', 1, '2023-12-01 19:38:51');
INSERT INTO `t_menu` VALUES (153, '清除缓存', 3, 133, NULL, NULL, NULL, 1, 'support:cache:delete', 'support:cache:delete', NULL, 133, 0, NULL, 0, 1, 1, 0, 1, '2022-10-15 22:45:13', 1, '2023-10-07 16:22:29');
INSERT INTO `t_menu` VALUES (154, '获取缓存key', 3, 133, NULL, NULL, NULL, 1, 'support:cache:keys', 'support:cache:keys', NULL, 133, 0, NULL, 0, 1, 1, 0, 1, '2022-10-15 22:45:48', 1, '2023-10-07 16:22:35');
INSERT INTO `t_menu` VALUES (156, '查看结果', 3, 117, NULL, NULL, NULL, 1, 'support:reload:result', 'support:reload:result', NULL, 117, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:17:23', 1, '2023-10-07 14:31:47');
INSERT INTO `t_menu` VALUES (157, '单号生成', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:generate', 'support:serialNumber:generate', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:21:06', 1, '2023-10-07 18:22:46');
INSERT INTO `t_menu` VALUES (158, '生成记录', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:record', 'support:serialNumber:record', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:21:34', 1, '2023-10-07 18:22:55');
INSERT INTO `t_menu` VALUES (159, '查询', 3, 110, NULL, NULL, NULL, 1, 'support:dict:query', 'support:dict:query', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:23:51', 1, '2025-04-08 19:42:25');
INSERT INTO `t_menu` VALUES (160, '添加', 3, 110, NULL, NULL, NULL, 1, 'support:dict:add', 'support:dict:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:05', 1, '2025-04-08 19:43:02');
INSERT INTO `t_menu` VALUES (161, '更新', 3, 110, NULL, NULL, NULL, 1, 'support:dict:update', 'support:dict:update', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:34', 1, '2025-04-08 19:43:34');
INSERT INTO `t_menu` VALUES (162, '删除', 3, 110, NULL, NULL, NULL, 1, 'support:dict:delete', 'support:dict:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:55', 1, '2025-04-08 19:43:52');
INSERT INTO `t_menu` VALUES (163, '新建', 3, 109, NULL, NULL, NULL, 1, 'support:config:add', 'support:config:add', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:26:56', 1, '2023-10-07 18:16:17');
INSERT INTO `t_menu` VALUES (164, '编辑', 3, 109, NULL, NULL, NULL, 1, 'support:config:update', 'support:config:update', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:27:07', 1, '2023-10-07 18:16:24');
INSERT INTO `t_menu` VALUES (165, '查询', 3, 47, NULL, NULL, NULL, 1, 'goods:query', 'goods:query', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:55:39', 1, '2023-10-07 13:58:28');
INSERT INTO `t_menu` VALUES (166, '新建', 3, 47, NULL, NULL, NULL, 1, 'goods:add', 'goods:add', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:56:00', 1, '2023-10-07 13:58:32');
INSERT INTO `t_menu` VALUES (167, '批量删除', 3, 47, NULL, NULL, NULL, 1, 'goods:batchDelete', 'goods:batchDelete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:56:15', 1, '2023-10-07 13:58:35');
INSERT INTO `t_menu` VALUES (168, '查询', 3, 147, 11, NULL, NULL, 1, 'support:helpDoc:query', 'support:helpDoc:query', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:13', 1, '2023-10-07 14:05:49');
INSERT INTO `t_menu` VALUES (169, '新建', 3, 147, 12, NULL, NULL, 1, 'support:helpDoc:add', 'support:helpDoc:add', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:37', 1, '2023-10-07 14:05:56');
INSERT INTO `t_menu` VALUES (170, '新建目录', 3, 147, 1, NULL, NULL, 1, 'support:helpDocCatalog:addCategory', 'support:helpDocCatalog:addCategory', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:57', 1, '2023-10-07 14:06:38');
INSERT INTO `t_menu` VALUES (171, '修改目录', 3, 147, 2, NULL, NULL, 1, 'support:helpDocCatalog:update', 'support:helpDocCatalog:update', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:13:46', 1, '2023-10-07 14:06:49');
INSERT INTO `t_menu` VALUES (173, '新建', 3, 78, NULL, NULL, NULL, 1, 'category:add', 'category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:02', 1, '2023-10-07 13:54:01');
INSERT INTO `t_menu` VALUES (174, '查询', 3, 78, NULL, NULL, NULL, 1, 'category:tree', 'category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:22', 1, '2023-10-07 13:54:33');
INSERT INTO `t_menu` VALUES (175, '编辑', 3, 78, NULL, NULL, NULL, 1, 'category:update', 'category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:38', 1, '2023-10-07 13:54:18');
INSERT INTO `t_menu` VALUES (176, '删除', 3, 78, NULL, NULL, NULL, 1, 'category:delete', 'category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:50', 1, '2023-10-07 13:54:27');
INSERT INTO `t_menu` VALUES (177, '新建', 3, 79, NULL, NULL, NULL, 1, 'category:add', 'custom:category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:02', 1, '2023-10-07 13:57:32');
INSERT INTO `t_menu` VALUES (178, '查询', 3, 79, NULL, NULL, NULL, 1, 'category:tree', 'custom:category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:22', 1, '2023-10-07 13:57:50');
INSERT INTO `t_menu` VALUES (179, '编辑', 3, 79, NULL, NULL, NULL, 1, 'category:update', 'custom:category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:38', 1, '2023-10-07 13:58:02');
INSERT INTO `t_menu` VALUES (180, '删除', 3, 79, NULL, NULL, NULL, 1, 'category:delete', 'custom:category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:50', 1, '2023-10-07 13:58:12');
INSERT INTO `t_menu` VALUES (181, '查询', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:query', 'oa:enterprise:query', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:14', 1, '2023-10-07 12:00:09');
INSERT INTO `t_menu` VALUES (182, '新建', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:add', 'oa:enterprise:add', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:25', 1, '2023-10-07 12:00:17');
INSERT INTO `t_menu` VALUES (183, '编辑', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:update', 'oa:enterprise:update', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:36', 1, '2023-10-07 12:00:38');
INSERT INTO `t_menu` VALUES (184, '删除', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:delete', 'oa:enterprise:delete', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:53', 1, '2023-10-07 12:00:46');
INSERT INTO `t_menu` VALUES (185, '查询', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:query', 'oa:notice:query', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:26:38', 1, '2023-10-07 11:43:01');
INSERT INTO `t_menu` VALUES (186, '新建', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:add', 'oa:notice:add', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:04', 1, '2023-10-07 11:43:07');
INSERT INTO `t_menu` VALUES (187, '编辑', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:update', 'oa:notice:update', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:15', 1, '2023-10-07 11:43:12');
INSERT INTO `t_menu` VALUES (188, '删除', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:delete', 'oa:notice:delete', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:23', 1, '2023-10-07 11:43:18');
INSERT INTO `t_menu` VALUES (190, '查询', 3, 152, NULL, NULL, NULL, 1, '', 'support:changeLog:query', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:28:33', 1, '2023-10-07 14:25:05');
INSERT INTO `t_menu` VALUES (191, '新建', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:add', 'support:changeLog:add', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:28:46', 1, '2023-10-07 14:24:15');
INSERT INTO `t_menu` VALUES (192, '批量删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:batchDelete', 'support:changeLog:batchDelete', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:29:10', 1, '2023-10-07 14:24:22');
INSERT INTO `t_menu` VALUES (193, '文件管理', 2, 50, 20, '/support/file/file-list', '/support/file/file-list.vue', NULL, NULL, NULL, 'FolderOpenOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 11:26:11', 1, '2022-10-22 11:29:22');
INSERT INTO `t_menu` VALUES (194, '删除', 3, 47, NULL, NULL, NULL, 1, 'goods:delete', 'goods:delete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:00:12', 1, '2023-10-07 13:58:39');
INSERT INTO `t_menu` VALUES (195, '修改', 3, 47, NULL, NULL, NULL, 1, 'goods:update', 'goods:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:05:23', 1, '2023-10-07 13:58:42');
INSERT INTO `t_menu` VALUES (196, '查看详情', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:detail', 'oa:enterprise:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:16:47', 1, '2023-10-07 11:48:59');
INSERT INTO `t_menu` VALUES (198, '删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:delete', 'support:changeLog:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:42:34', 1, '2023-10-07 14:24:32');
INSERT INTO `t_menu` VALUES (199, '查询', 3, 109, NULL, NULL, NULL, 1, 'support:config:query', 'support:config:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:45:14', 1, '2023-10-07 18:16:27');
INSERT INTO `t_menu` VALUES (200, '查询', 3, 193, NULL, NULL, NULL, 1, 'support:file:query', 'support:file:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:47:23', 1, '2023-10-07 18:24:43');
INSERT INTO `t_menu` VALUES (201, '删除', 3, 147, 14, NULL, NULL, 1, 'support:helpDoc:delete', 'support:helpDoc:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:03:20', 1, '2023-10-07 14:07:02');
INSERT INTO `t_menu` VALUES (202, '更新', 3, 147, 13, NULL, NULL, 1, 'support:helpDoc:update', 'support:helpDoc:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:03:32', 1, '2023-10-07 14:06:56');
INSERT INTO `t_menu` VALUES (203, '查询', 3, 143, NULL, NULL, NULL, 1, 'support:loginLog:query', 'support:loginLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:05:11', 1, '2023-10-07 14:27:23');
INSERT INTO `t_menu` VALUES (204, '查询', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:query', 'support:operateLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:33:31', 1, '2023-10-07 14:27:56');
INSERT INTO `t_menu` VALUES (205, '详情', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:detail', 'support:operateLog:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:33:49', 1, '2023-10-07 14:28:04');
INSERT INTO `t_menu` VALUES (206, '心跳监控', 2, 111, 1, '/support/heart-beat/heart-beat-list', '/support/heart-beat/heart-beat-list.vue', 1, NULL, NULL, 'FallOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:47:03', 1, '2022-10-22 18:32:52');
INSERT INTO `t_menu` VALUES (207, '更新', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:update', 'support:changeLog:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 11:51:32', 1, '2023-10-07 14:24:39');
INSERT INTO `t_menu` VALUES (212, '查询', 3, 117, NULL, NULL, NULL, 1, 'support:reload:query', 'support:reload:query', NULL, NULL, 0, NULL, 1, 1, 1, 0, 1, '2023-10-07 14:31:36', NULL, '2023-10-07 14:31:36');
INSERT INTO `t_menu` VALUES (213, '网络安全', 1, 0, 5, NULL, NULL, 1, NULL, NULL, 'SafetyCertificateOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-17 19:03:08', 1, '2023-12-01 19:38:00');
INSERT INTO `t_menu` VALUES (214, '登录失败锁定', 2, 213, 4, '/support/login-fail', '/support/login-fail/login-fail-list.vue', 1, NULL, NULL, 'LockOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-17 19:04:24', 44, '2024-08-13 14:16:26');
INSERT INTO `t_menu` VALUES (215, '接口加解密', 2, 213, 2, '/support/api-encrypt', '/support/api-encrypt/api-encrypt-index.vue', 1, NULL, NULL, 'CodepenCircleOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-24 11:49:28', 44, '2024-08-13 12:00:14');
INSERT INTO `t_menu` VALUES (216, '导出', 3, 47, NULL, NULL, NULL, 1, 'goods:exportGoods', 'goods:exportGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:34:03', NULL, '2023-12-01 19:34:03');
INSERT INTO `t_menu` VALUES (217, '导入', 3, 47, 3, NULL, NULL, 1, 'goods:importGoods', 'goods:importGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:34:22', NULL, '2023-12-01 19:34:22');
INSERT INTO `t_menu` VALUES (218, '文档中心', 1, 0, 4, NULL, NULL, 1, NULL, NULL, 'FileSearchOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:37:28', 1, '2023-12-01 19:37:51');
INSERT INTO `t_menu` VALUES (219, '部门管理', 2, 45, 1, '/organization/department', '/system/department/department-list.vue', 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-22 16:40:21', 1, '2024-07-02 20:15:17');
INSERT INTO `t_menu` VALUES (221, '定时任务', 2, 50, 25, '/job/list', '/support/job/job-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, '2024-06-25 17:57:40', 2, '2024-06-25 19:49:21');
INSERT INTO `t_menu` VALUES (228, '职务管理', 2, 45, 2, '/organization/position', '/system/position/position-list.vue', 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-29 11:11:09', 1, '2024-07-02 20:15:11');
INSERT INTO `t_menu` VALUES (229, '查询任务', 3, 221, NULL, NULL, NULL, 1, 'support:job:query', 'support:job:query', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:14:15', 2, '2024-06-29 11:15:00');
INSERT INTO `t_menu` VALUES (230, '更新任务', 3, 221, NULL, NULL, NULL, 1, 'support:job:update', 'support:job:update', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:15:40', NULL, '2024-06-29 11:15:40');
INSERT INTO `t_menu` VALUES (231, '执行任务', 3, 221, NULL, NULL, NULL, 1, 'support:job:execute', 'support:job:execute', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:16:03', NULL, '2024-06-29 11:16:03');
INSERT INTO `t_menu` VALUES (232, '查询记录', 3, 221, NULL, NULL, NULL, 1, 'support:job:log:query', 'support:job:log:query', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:16:37', NULL, '2024-06-29 11:16:37');
INSERT INTO `t_menu` VALUES (233, 'knife4j文档', 2, 218, 4, '/knife4j', NULL, 1, NULL, NULL, 'FileWordOutlined', NULL, 1, 'http://localhost:1024/doc.html', 1, 1, 0, 0, 1, '2024-07-02 20:23:50', 1, '2024-07-08 13:49:15');
INSERT INTO `t_menu` VALUES (234, 'swagger文档', 2, 218, 5, '/swagger', 'http://localhost:1024/swagger-ui/index.html', 1, NULL, NULL, 'ApiOutlined', NULL, 1, 'http://localhost:1024/swagger-ui/index.html', 1, 1, 0, 0, 1, '2024-07-02 20:35:43', 1, '2024-07-08 13:49:26');
INSERT INTO `t_menu` VALUES (250, '三级等保设置', 2, 213, 1, '/support/level3protect/level3-protect-config-index', '/support/level3protect/level3-protect-config-index.vue', 1, NULL, NULL, 'SafetyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 44, '2024-08-13 11:41:02', 44, '2024-08-13 11:58:12');
INSERT INTO `t_menu` VALUES (251, '敏感数据脱敏', 2, 213, 3, '/support/level3protect/data-masking-list', '/support/level3protect/data-masking-list.vue', 1, NULL, NULL, 'FileProtectOutlined', NULL, 0, NULL, 1, 1, 0, 0, 44, '2024-08-13 11:58:00', 44, '2024-08-13 11:59:49');
INSERT INTO `t_menu` VALUES (252, '启用/禁用', 3, 110, NULL, NULL, NULL, 1, 'support:dict:updateDisabled', 'support:dict:updateDisabled', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:44:12', 1, '2025-04-08 19:46:03');
INSERT INTO `t_menu` VALUES (253, '查询字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:query', 'support:dictData:query', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:46:47', NULL, '2025-04-08 19:46:47');
INSERT INTO `t_menu` VALUES (254, '添加字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:add', 'support:dictData:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:00', NULL, '2025-04-08 19:48:00');
INSERT INTO `t_menu` VALUES (255, '更新字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:update', 'support:dictData:update', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:19', NULL, '2025-04-08 19:48:19');
INSERT INTO `t_menu` VALUES (256, '删除字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:delete', 'support:dictData:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:38', NULL, '2025-04-08 19:48:38');
INSERT INTO `t_menu` VALUES (257, '启用/禁用字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:updateDisabled', 'support:dictData:updateDisabled', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:57', NULL, '2025-04-08 19:48:57');
INSERT INTO `t_menu` VALUES (258, '查询企业员工', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:queryEmployee', 'oa:enterprise:queryEmployee', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:11:46', 75, '2025-04-08 21:12:24');
INSERT INTO `t_menu` VALUES (259, '查询银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:query', 'oa:bank:query', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:12:40', NULL, '2025-04-08 21:12:40');
INSERT INTO `t_menu` VALUES (260, '查询发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:query', 'oa:invoice:query', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:12:56', NULL, '2025-04-08 21:12:56');
INSERT INTO `t_menu` VALUES (261, '添加企业员工', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:addEmployee', 'oa:enterprise:addEmployee', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:35:34', NULL, '2025-04-08 21:35:34');
INSERT INTO `t_menu` VALUES (262, '删除企业员工', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:deleteEmployee', 'oa:enterprise:deleteEmployee', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:40:17', NULL, '2025-04-08 21:40:17');
INSERT INTO `t_menu` VALUES (263, '添加银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:add', 'oa:bank:add', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:45:44', NULL, '2025-04-08 21:45:44');
INSERT INTO `t_menu` VALUES (264, '更新银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:update', 'oa:bank:update', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:02', NULL, '2025-04-08 21:46:02');
INSERT INTO `t_menu` VALUES (265, '删除银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:delete', 'oa:bank:delete', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:12', NULL, '2025-04-08 21:46:12');
INSERT INTO `t_menu` VALUES (266, '添加发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:add', 'oa:invoice:add', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:30', NULL, '2025-04-08 21:46:30');
INSERT INTO `t_menu` VALUES (267, '更新发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:update', 'oa:invoice:update', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:47', NULL, '2025-04-08 21:46:47');
INSERT INTO `t_menu` VALUES (268, '删除发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:delete', 'oa:invoice:delete', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:59', NULL, '2025-04-08 21:46:59');
INSERT INTO `t_menu` VALUES (300, '消息管理', 2, 50, 30, '/message', '/support/message/message-list.vue', 1, NULL, NULL, 'MailOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2025-04-09 14:30:04', 1, '2025-04-10 20:19:36');
INSERT INTO `t_menu` VALUES (301, '知识库', 2, 309, NULL, '/ai-dataset/list', '/business/ai-dataset/ai-dataset-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:02:06', 1, '2026-01-28 01:31:04');
INSERT INTO `t_menu` VALUES (302, '查询', 3, 301, NULL, NULL, NULL, 1, 'aiDataset:query', 'aiDataset:query', NULL, 301, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:02:06', 1, '2026-01-28 01:31:04');
INSERT INTO `t_menu` VALUES (303, '添加', 3, 301, NULL, NULL, NULL, 1, 'aiDataset:add', 'aiDataset:add', NULL, 301, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:02:07', 1, '2026-01-28 01:31:04');
INSERT INTO `t_menu` VALUES (304, '更新', 3, 301, NULL, NULL, NULL, 1, 'aiDataset:update', 'aiDataset:update', NULL, 301, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:02:07', 1, '2026-01-28 01:31:04');
INSERT INTO `t_menu` VALUES (305, '删除', 3, 301, NULL, NULL, NULL, 1, 'aiDataset:delete', 'aiDataset:delete', NULL, 301, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:02:07', 1, '2026-01-28 01:31:04');
INSERT INTO `t_menu` VALUES (306, '知识库', 2, 0, NULL, '/ai-dataset/list', '/business/ai-dataset/ai-dataset-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:12:21', 1, '2025-12-03 14:15:59');
INSERT INTO `t_menu` VALUES (307, '知识库', 2, 0, NULL, '/ai-dataset/list', '/business/ai-dataset/ai-dataset-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:12:44', 1, '2025-12-03 14:16:01');
INSERT INTO `t_menu` VALUES (308, '知识库', 2, 0, NULL, '/ai-dataset/list', '/business/ai-dataset/ai-dataset-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:12:55', 1, '2025-12-03 14:16:04');
INSERT INTO `t_menu` VALUES (309, '知识库', 1, 0, 7, NULL, NULL, 1, NULL, NULL, 'FileDoneOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-03 14:17:44', 1, '2026-01-28 01:31:04');
INSERT INTO `t_menu` VALUES (310, 'ai应用管理表', 2, 320, NULL, '/ai/app/list', '/business/ai/app/ai-app-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:07', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (311, '查询', 3, 310, NULL, NULL, NULL, 1, 'aiApp:query', 'aiApp:query', NULL, 310, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:07', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (312, '添加', 3, 310, NULL, NULL, NULL, 1, 'aiApp:add', 'aiApp:add', NULL, 310, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:07', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (313, '更新', 3, 310, NULL, NULL, NULL, 1, 'aiApp:update', 'aiApp:update', NULL, 310, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:07', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (314, '删除', 3, 310, NULL, NULL, NULL, 1, 'aiApp:delete', 'aiApp:delete', NULL, 310, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:07', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (315, '应用提供商', 2, 320, NULL, '/ai-app-provider/list', '/business/ai/app-provider/app-provider-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:45', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (316, '查询', 3, 315, NULL, NULL, NULL, 1, 'aiAppProvider:query', 'aiAppProvider:query', NULL, 315, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:45', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (317, '添加', 3, 315, NULL, NULL, NULL, 1, 'aiAppProvider:add', 'aiAppProvider:add', NULL, 315, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:45', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (318, '更新', 3, 315, NULL, NULL, NULL, 1, 'aiAppProvider:update', 'aiAppProvider:update', NULL, 315, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:45', 1, '2026-03-04 13:16:41');
INSERT INTO `t_menu` VALUES (319, '删除', 3, 315, NULL, NULL, NULL, 1, 'aiAppProvider:delete', 'aiAppProvider:delete', NULL, 315, 0, NULL, 0, 1, 0, 1, 1, '2025-12-21 08:18:45', 1, '2026-03-04 13:16:41');
INSERT INTO `t_menu` VALUES (320, 'AI管理', 1, 0, 9, NULL, NULL, 1, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2025-12-24 13:24:31', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (322, '查询', 3, 321, NULL, NULL, NULL, 1, 'aiModelConfig:query', 'aiModelConfig:query', NULL, 321, 0, NULL, 0, 1, 0, 1, 1, '2026-01-28 02:08:04', 1, '2026-03-04 13:16:41');
INSERT INTO `t_menu` VALUES (323, '添加', 3, 321, NULL, NULL, NULL, 1, 'aiModelConfig:add', 'aiModelConfig:add', NULL, 321, 0, NULL, 0, 1, 0, 1, 1, '2026-01-28 02:08:04', 1, '2026-03-04 13:16:41');
INSERT INTO `t_menu` VALUES (324, '更新', 3, 321, NULL, NULL, NULL, 1, 'aiModelConfig:update', 'aiModelConfig:update', NULL, 321, 0, NULL, 0, 1, 0, 1, 1, '2026-01-28 02:08:04', 1, '2026-03-04 13:16:41');
INSERT INTO `t_menu` VALUES (325, '删除', 3, 321, NULL, NULL, NULL, 1, 'aiModelConfig:delete', 'aiModelConfig:delete', NULL, 321, 0, NULL, 0, 1, 0, 1, 1, '2026-01-28 02:08:04', 1, '2026-03-04 13:16:41');
INSERT INTO `t_menu` VALUES (326, '知识库管理', 1, 0, 10, NULL, NULL, 1, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2026-01-28 12:34:01', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (327, '知识库', 2, 326, NULL, '/ai-knowledge/list', '/business/ai/knowledge/ai-knowledge-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2026-01-29 12:58:10', 1, '2026-03-04 13:16:40');
INSERT INTO `t_menu` VALUES (328, '模型配置表', 2, 333, NULL, '/ai/model/ai-model-config/list', '/business/ai/model/ai-model-config-list.vue', 1, NULL, NULL, 'AlignLeftOutlined', NULL, 0, NULL, 0, 0, 1, 0, 1, '2026-03-04 13:43:26', 1, '2026-03-17 07:26:45');
INSERT INTO `t_menu` VALUES (329, '查询', 3, 328, NULL, NULL, NULL, 1, 'aiModelConfig:query', 'aiModelConfig:query', NULL, 328, 0, NULL, 0, 1, 0, 0, 1, '2026-03-04 13:45:11', NULL, '2026-03-04 13:45:11');
INSERT INTO `t_menu` VALUES (330, '添加', 3, 328, NULL, NULL, NULL, 1, 'aiModelConfig:add', 'aiModelConfig:add', NULL, 328, 0, NULL, 0, 1, 0, 0, 1, '2026-03-04 13:45:12', NULL, '2026-03-04 13:45:12');
INSERT INTO `t_menu` VALUES (331, '更新', 3, 328, NULL, NULL, NULL, 1, 'aiModelConfig:update', 'aiModelConfig:update', NULL, 328, 0, NULL, 0, 1, 0, 0, 1, '2026-03-04 13:45:12', NULL, '2026-03-04 13:45:12');
INSERT INTO `t_menu` VALUES (332, '删除', 3, 328, NULL, NULL, NULL, 1, 'aiModelConfig:delete', 'aiModelConfig:delete', NULL, 328, 0, NULL, 0, 1, 0, 0, 1, '2026-03-04 13:45:12', NULL, '2026-03-04 13:45:12');
INSERT INTO `t_menu` VALUES (333, 'API中转管理', 1, 0, 7, NULL, NULL, 1, NULL, NULL, 'AimOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-04 13:47:10', 1, '2026-03-15 09:33:18');
INSERT INTO `t_menu` VALUES (334, '模型管理', 2, 333, NULL, '/ai/relay-model/list', '/business/ai/relay/model/relay-model-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-15 08:43:38', 1, '2026-03-15 08:44:23');
INSERT INTO `t_menu` VALUES (335, '渠道管理', 2, 333, NULL, '/ai/relay-channel/list', '/business/ai/relay/channel/relay-channel-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-15 09:29:16', NULL, '2026-03-15 09:29:16');
INSERT INTO `t_menu` VALUES (336, '用户账户', 2, 333, NULL, '/ai/relay-account/list', '/business/ai/relay/account/relay-account-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-15 09:29:56', NULL, '2026-03-15 09:29:56');
INSERT INTO `t_menu` VALUES (337, '计费列表', 2, 333, NULL, '/ai/relay-billing/list', '/business/ai/relay/billing/relay-billing-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-15 09:30:30', NULL, '2026-03-15 09:30:30');
INSERT INTO `t_menu` VALUES (338, 'Token管理', 2, 333, NULL, '/ai/relay-token/list', '/business/ai/relay/token/relay-token-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-15 09:31:03', NULL, '2026-03-15 09:31:03');
INSERT INTO `t_menu` VALUES (339, '用量日志', 2, 333, NULL, '/ai/relay-usage-log/list', '/business/ai/relay/usage-log/relay-usage-log-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-15 09:31:35', NULL, '2026-03-15 09:31:35');
INSERT INTO `t_menu` VALUES (340, '渠道分组', 2, 333, NULL, '/ai/relay-channel-group/list', '/business/ai/relay/channel-group/relay-channel-group-list.vue', 1, NULL, NULL, NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2026-03-17 00:33:53', NULL, '2026-03-17 00:33:53');

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `message_type` smallint NOT NULL COMMENT '消息类型',
  `receiver_user_type` int NOT NULL COMMENT '接收者用户类型',
  `receiver_user_id` bigint NOT NULL COMMENT '接收者用户id',
  `data_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '相关数据id',
  `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `idx_msg`(`message_type` ASC, `receiver_user_type` ASC, `receiver_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES (1, 1, 1, 1, 'null', '张三的对公付款单 【3000元】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 1, '2026-03-04 13:11:01', '2024-06-27 01:14:07', '2026-03-04 13:11:01');
INSERT INTO `t_message` VALUES (2, 2, 1, 1, '234', '刘备的请假单【本周四】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 1, '2026-03-04 13:11:00', '2024-07-04 16:09:49', '2026-03-04 13:11:00');
INSERT INTO `t_message` VALUES (3, 1, 1, 1, '23', '武松的物资采购单【Macbook Pro】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 1, '2026-03-04 13:10:58', '2024-07-07 22:03:14', '2026-03-04 13:10:58');
INSERT INTO `t_message` VALUES (4, 1, 1, 1, '23', '孙悟空的出差申请【出差洛阳】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 1, '2026-03-04 13:10:56', '2024-07-07 22:03:14', '2026-03-04 13:10:56');

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`  (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `notice_type_id` bigint NOT NULL COMMENT '类型1公告 2动态',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `all_visible_flag` tinyint(1) NOT NULL COMMENT '是否全部可见',
  `scheduled_publish_flag` tinyint(1) NOT NULL COMMENT '是否定时发布',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `content_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文本内容',
  `content_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'html内容',
  `attachment` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `page_view_count` int NOT NULL DEFAULT 0 COMMENT '页面浏览量，传说中的pv',
  `user_view_count` int NOT NULL DEFAULT 0 COMMENT '用户浏览量，传说中的uv',
  `source` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源',
  `author` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `document_number` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文号，如：1024创新实验室发〔2022〕字第36号',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0,
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES (49, 1, 'Spring Boot 3.0.0 首个 RC 发布', 1, 0, '2024-01-01 20:22:23', 'Spring Boot 3.0.0 首个 RC 发布\nSpring Boot 3.0 首个 RC 已发布，此外还为两个分支发布了更新：2.7.5 & 2.6.13。\n3.0.0-RC1\n发布公告写道，此版本包含 135 项功能增强、文档改进、依赖升级和 Bugfix。\nSpring Boot 3.0 的开发工作始于实验性的 Spring Native，旨在为 GraalVM 原生镜像提供支持。在该版本中，开发者现在可以使用标准 Spring Boot Maven 或 Gradle 插件将 Spring Boot 应用程序转换为原生可执行文件，而无需任何特殊配置。\n此版本还在参考文档中添加新内容来解释 AOT 处理背后的概念以及如何开始生成第一个 GraalVM 原生镜像。\n除此之外，Spring Boot 3.0 还完成了迁移到 JakartaEE 9 的工作，以及将使用的 Java 版本升级到 Java 17。\n其他新特性：\n为 Spring Data JDBC 提供更灵活的自动配置为 Prometheus 示例提供自动配置增强 Log4j2 功能，包括配置文件支持和环境属性查找\n详情查看 Release Note。\nSpring Boot 2.7.5 和 2.6.13 的更新内容主要是修复错误，优化文档和升级依赖，详情查看 Release Note (2.7.5、2.6.13)。\n相关链接\nSpring Boot 的详细介绍：点击查看Spring Boot 的下载地址：点击下载', '<h1 style=\"text-indent: 0px; text-align: start;\"><a href=\"https://www.oschina.net/news/214401/spring-boot-3-0-0-rc1-released\" target=\"_blank\">Spring&nbsp;Boot&nbsp;3.0.0&nbsp;首个&nbsp;RC&nbsp;发布</a></h1><p>Spring&nbsp;Boot&nbsp;3.0 首个&nbsp;RC 已发布，此外还为两个分支发布了更新：2.7.5 & 2.6.13。</p><p>3.0.0-RC1</p><p>发布公告写道，此版本包含 135&nbsp;项功能增强、文档改进、依赖升级和&nbsp;Bugfix。</p><p>Spring&nbsp;Boot&nbsp;3.0&nbsp;的开发工作始于实验性的&nbsp;Spring&nbsp;Native，旨在为&nbsp;GraalVM&nbsp;原生镜像提供支持。在该版本中，开发者现在可以使用标准&nbsp;Spring&nbsp;Boot&nbsp;Maven&nbsp;或&nbsp;Gradle&nbsp;插件将&nbsp;Spring&nbsp;Boot&nbsp;应用程序转换为原生可执行文件，而无需任何特殊配置。</p><p>此版本还在参考文档中添加新内容来解释 AOT&nbsp;处理背后的概念以及如何开始生成第一个&nbsp;GraalVM&nbsp;原生镜像。</p><p>除此之外，Spring&nbsp;Boot&nbsp;3.0&nbsp;还完成了迁移到 JakartaEE&nbsp;9&nbsp;的工作，以及将使用的&nbsp;Java&nbsp;版本升级到&nbsp;Java&nbsp;17。</p><p>其他新特性：</p><p>为&nbsp;Spring&nbsp;Data&nbsp;JDBC&nbsp;提供更灵活的自动配置为&nbsp;Prometheus&nbsp;示例提供自动配置增强&nbsp;Log4j2&nbsp;功能，包括配置文件支持和环境属性查找</p><p>详情查看&nbsp;Release&nbsp;Note。</p><p>Spring&nbsp;Boot&nbsp;2.7.5&nbsp;和&nbsp;2.6.13&nbsp;的更新内容主要是修复错误，优化文档和升级依赖，详情查看&nbsp;Release&nbsp;Note&nbsp;(2.7.5、2.6.13)。</p><p>相关链接</p><p>Spring&nbsp;Boot&nbsp;的详细介绍：点击查看Spring&nbsp;Boot&nbsp;的下载地址：点击下载</p>', '', 0, 0, '开源中国', '卓大', NULL, 0, 1, '2024-03-02 18:53:26', '2022-10-22 14:27:33');
INSERT INTO `t_notice` VALUES (50, 1, 'Oracle 推出 JDK 8 的直接替代品', 1, 0, '2024-01-01 20:22:23', 'Oracle 推出 JDK 8 的直接替代品\n来源: OSCHINA\n编辑: 白开水不加糖\n2022-10-20 08:14:29\n 0\n为了向传统的 Java 8 服务器工作负载提供 Java 17 级别的性能，Oracle 宣布推出 Java SE Subscription Enterprise Performance Pack (Enterprise Performance Pack)。并声称这是 JDK 8 的直接替代品，现已在 MyOracleSupport 上面向所有 Java SE 订阅客户和 Oracle 云基础设施 (OCI) 用户免费提供。\n“Enterprise Performance Pack 为 JDK 8 用户提供了在 JDK 8 和 JDK 17 发布之间的 7 年时间里，为 Java 带来的重大内存管理和性能改进。这些改进包括：现代垃圾回收算法、紧凑字符串、增强的可观察性和数十种其他优化。”\nJava 8 发布于 2014 年，和 Java 17 一样都是长期支持 (LTS) 版本；尽管发布距今已有近九年的历史，但仍被很多开发人员和组织所广泛应用。New Relic 发布的一份 “2022 年 Java 生态系统状况报告” 数据表明，Java 8 仍被 46.45% 的 Java 应用程序在生产中使用。\n根据介绍，Enterprise Performance Pack 在 Intel 和基于 Arm 的系统（如 Ampere Altra）上支持 headless Linux 64 位工作负载。\nOracle 方面称，使用 Enterprise Performance Pack 的客户将可以立即看到以或接近内存或 CPU 容量运行的 JDK 8 工作负载的好处。在 Oracle 自己的产品和云服务进行的测试表明，高负载应用程序的内存和性能都提高了大约 40%。即使没有接近容量运行的 JDK 8 应用程序，也可以会看到高达 5% 的性能提升。\n虽然 Enterprise Performance Pack 中包含的许多改进可以通过默认选项获得，但 Oracle 建议用户还是自己研究文档，以最大限度地提高性能并最大限度地降低内存使用率。例如，通过启用可扩展的低延迟 ZGC 垃圾收集器来提高应用程序响应能力，需要通过 -XX:+UseZGC 选项。', '<h3>Oracle&nbsp;推出&nbsp;JDK&nbsp;8&nbsp;的直接替代品</h3><p>来源:&nbsp;OSCHINA</p><p>编辑: 白开水不加糖</p><p>2022-10-20&nbsp;08:14:29</p><p> 0</p><p>为了向传统的&nbsp;Java&nbsp;8&nbsp;服务器工作负载提供&nbsp;Java&nbsp;17&nbsp;级别的性能，Oracle 宣布推出&nbsp;Java&nbsp;SE&nbsp;Subscription&nbsp;Enterprise&nbsp;Performance&nbsp;Pack&nbsp;(Enterprise&nbsp;Performance&nbsp;Pack)。并声称这是 JDK&nbsp;8&nbsp;的直接替代品，现已在 MyOracleSupport 上面向所有&nbsp;Java&nbsp;SE&nbsp;订阅客户和&nbsp;Oracle&nbsp;云基础设施&nbsp;(OCI)&nbsp;用户免费提供。</p><p>“Enterprise&nbsp;Performance&nbsp;Pack&nbsp;为&nbsp;JDK&nbsp;8&nbsp;用户提供了在&nbsp;JDK&nbsp;8&nbsp;和&nbsp;JDK&nbsp;17&nbsp;发布之间的&nbsp;7&nbsp;年时间里，为&nbsp;Java&nbsp;带来的重大内存管理和性能改进。这些改进包括：现代垃圾回收算法、紧凑字符串、增强的可观察性和数十种其他优化。”</p><p>Java&nbsp;8&nbsp;发布于&nbsp;2014&nbsp;年，和&nbsp;Java&nbsp;17&nbsp;一样都是长期支持&nbsp;(LTS)&nbsp;版本；尽管发布距今已有近九年的历史，但仍被很多开发人员和组织所广泛应用。New&nbsp;Relic&nbsp;发布的一份 “2022&nbsp;年&nbsp;Java&nbsp;生态系统状况报告”&nbsp;数据表明，Java&nbsp;8&nbsp;仍被&nbsp;46.45%&nbsp;的&nbsp;Java&nbsp;应用程序在生产中使用。</p><p>根据介绍，Enterprise&nbsp;Performance&nbsp;Pack&nbsp;在&nbsp;Intel&nbsp;和基于&nbsp;Arm&nbsp;的系统（如&nbsp;Ampere&nbsp;Altra）上支持 headless&nbsp;Linux&nbsp;64&nbsp;位工作负载。</p><p>Oracle 方面称，使用&nbsp;Enterprise&nbsp;Performance&nbsp;Pack&nbsp;的客户将可以立即看到以或接近内存或&nbsp;CPU&nbsp;容量运行的&nbsp;JDK&nbsp;8&nbsp;工作负载的好处。在&nbsp;Oracle&nbsp;自己的产品和云服务进行的测试表明，高负载应用程序的内存和性能都提高了大约&nbsp;40%。即使没有接近容量运行的&nbsp;JDK&nbsp;8&nbsp;应用程序，也可以会看到高达&nbsp;5%&nbsp;的性能提升。</p><p>虽然&nbsp;Enterprise&nbsp;Performance&nbsp;Pack&nbsp;中包含的许多改进可以通过默认选项获得，但 Oracle 建议用户还是自己研究文档，以最大限度地提高性能并最大限度地降低内存使用率。例如，通过启用可扩展的低延迟&nbsp;ZGC&nbsp;垃圾收集器来提高应用程序响应能力，需要通过&nbsp;-XX:+UseZGC&nbsp;选项。</p>', '', 0, 0, 'OSChina', '卓大', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:29:56');
INSERT INTO `t_notice` VALUES (51, 1, 'Spring Framework 6.0.0 RC2 发布', 1, 0, '2024-01-01 20:22:23', 'Spring Framework 6.0.0 RC2 发布\nSpring Framework 6.0.0 发布了第二个 RC 版本。\n新特性\n确保可以在构建时评估 classpath 检查 #29352为 JPA 持久化回调引入 Register 反射提示 #29348检查 @RegisterReflectionForBinding 是否至少指定一个类 #29346为 AOT 引擎设置引入 builder API #29341支持检测正在进行的 AOT 处理 #29340重新组织 HTTP Observation 类型 #29334支持在没有 java.beans.Introspector 的前提下，执行基本属性判断 #29320为BindingReflectionHintsRegistrar 添加 Kotlin 数据类组件支持 #29316将 HttpServiceFactory 和 RSocketServiceProxyFactory 切换到 builder 模型，以便优先进行可编程配置 #29296引入基于 GraalVM FieldValueTransformer API 的 PreComputeFieldFeature#29081在 TestContext 框架中引入 SPI 来处理 ApplicationContext 故障 #28826SimpleEvaluationContext 支持禁用 array 分配 #28808DateTimeFormatterRegistrar 支持默认回退到 ISO 解析 #26985\nSpring Framework 6.0 作为重大更新，要求使用 Java 17 或更高版本，并且已迁移到 Jakarta EE 9+（在 jakarta 命名空间中取代了以前基于 javax 的 EE API），以及对其他基础设施的修改。基于这些变化，Spring Framework 6.0 支持最新 Web 容器，如 Tomcat 10 / Jetty 11，以及最新的持久性框架 Hibernate ORM 6.1。这些特性仅可用于 Servlet API 和 JPA 的 jakarta 命名空间变体。\n值得一提的是，开发者可通过此版本在基于 Spring 的应用中体验 “虚拟线程”（JDK 19 中的预览版 “Project Loom”），查看此文章了解更多细节。现在提供了自定义选项来插入基于虚拟线程的 Executor 实现，目标是在 Project Loom 正式可用时提供 “一等公民” 的配置选项。\n除了上述的变化，Spring Framework 6.0 还包含许多其他改进和特性，例如：\n提供基于 @HttpExchange 服务接口的 HTTP 接口客户端对 RFC 7807 问题详细信息的支持Spring HTTP 客户端提供基于 Micrometer 的可观察性……\n详情查看 Release Note。\n按照发布计划，Spring Framework 6.0 将于 11 月正式 GA。', '<h1 style=\"text-indent: 0px; text-align: start;\"><a href=\"https://www.oschina.net/news/214472/spring-framework-6-0-0-rc2-released\" target=\"_blank\">Spring&nbsp;Framework&nbsp;6.0.0&nbsp;RC2&nbsp;发布</a></h1><p style=\"text-indent: 0px; text-align: left;\">Spring&nbsp;Framework&nbsp;6.0.0&nbsp;发布了<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fspring.io%2Fblog%2F2022%2F10%2F20%2Fspring-framework-6-0-0-rc2-available-now\" target=\"_blank\">第二个&nbsp;RC&nbsp;版本</a>。</p><p style=\"text-indent: 0px; text-align: left;\"><strong>新特性</strong></p><ul style=\"text-indent: 0px; text-align: left;\"><li>确保可以在构建时评估&nbsp;classpath&nbsp;检查&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29352\" target=\"_blank\">#29352</a></li><li>为&nbsp;JPA&nbsp;持久化回调引入&nbsp;Register&nbsp;反射提示&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29348\" target=\"_blank\">#29348</a></li><li>检查&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>@RegisterReflectionForBinding</code></span>&nbsp;是否至少指定一个类&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29346\" target=\"_blank\">#29346</a></li><li>为&nbsp;AOT&nbsp;引擎设置引入&nbsp;builder&nbsp;API&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29341\" target=\"_blank\">#29341</a></li><li>支持检测正在进行的&nbsp;AOT&nbsp;处理&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29340\" target=\"_blank\">#29340</a></li><li>重新组织&nbsp;HTTP&nbsp;Observation&nbsp;类型&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29334\" target=\"_blank\">#29334</a></li><li>支持在没有&nbsp;java.beans.Introspector&nbsp;的前提下，执行基本属性判断&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29320\" target=\"_blank\">#29320</a></li><li>为<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>BindingReflectionHintsRegistrar</code></span>&nbsp;添加&nbsp;Kotlin&nbsp;数据类组件支持&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29316\" target=\"_blank\">#29316</a></li><li>将&nbsp;HttpServiceFactory&nbsp;和&nbsp;RSocketServiceProxyFactory&nbsp;切换到&nbsp;builder&nbsp;模型，以便优先进行可编程配置&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29296\" target=\"_blank\">#29296</a></li><li>引入基于&nbsp;GraalVM&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>FieldValueTransformer</code></span>&nbsp;API&nbsp;的&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>PreComputeFieldFeature</code></span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29081\" target=\"_blank\">#29081</a></li><li>在&nbsp;TestContext&nbsp;框架中引入&nbsp;SPI&nbsp;来处理&nbsp;ApplicationContext&nbsp;故障&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F28826\" target=\"_blank\">#28826</a></li><li>SimpleEvaluationContext&nbsp;支持禁用&nbsp;array&nbsp;分配&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F28808\" target=\"_blank\">#28808</a></li><li>DateTimeFormatterRegistrar&nbsp;支持默认回退到&nbsp;ISO&nbsp;解析&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F26985\" target=\"_blank\">#26985</a></li></ul><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">Spring&nbsp;Framework&nbsp;6.0&nbsp;作为重大更新，要求</span><span style=\"color: rgb(51, 51, 51);\"><strong>使用&nbsp;Java&nbsp;17&nbsp;或更高版本</strong></span><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">，并且已迁移到&nbsp;Jakarta&nbsp;EE&nbsp;9+（在&nbsp;</span><span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>jakarta</code></span><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">&nbsp;命名空间中取代了以前基于&nbsp;</span><span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>javax</code></span><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">&nbsp;的&nbsp;EE&nbsp;API），以及对其他基础设施的修改。基于这些变化，Spring&nbsp;Framework&nbsp;6.0&nbsp;支持最新&nbsp;Web&nbsp;容器，如&nbsp;</span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Ftomcat.apache.org%2Fwhichversion.html\" target=\"_blank\">Tomcat&nbsp;10</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">&nbsp;/&nbsp;</span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fwww.eclipse.org%2Fjetty%2Fdownload.php\" target=\"_blank\">Jetty&nbsp;11</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">，以及最新的持久性框架&nbsp;</span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fhibernate.org%2Form%2Freleases%2F6.1%2F\" target=\"_blank\">Hibernate&nbsp;ORM&nbsp;6.1</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">。这些特性仅可用于&nbsp;Servlet&nbsp;API&nbsp;和&nbsp;JPA&nbsp;的&nbsp;jakarta&nbsp;命名空间变体。</span></p><p style=\"text-indent: 0px; text-align: left;\">值得一提的是，开发者可通过此版本在基于&nbsp;Spring&nbsp;的应用中体验&nbsp;“虚拟线程”（JDK&nbsp;19&nbsp;中的预览版&nbsp;“Project&nbsp;Loom”），<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fspring.io%2Fblog%2F2022%2F10%2F11%2Fembracing-virtual-threads\" target=\"_blank\">查看此文章</a>了解更多细节。现在提供了自定义选项来插入基于虚拟线程的&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>Executor</code></span>&nbsp;实现，目标是在&nbsp;Project&nbsp;Loom&nbsp;正式可用时提供&nbsp;“一等公民”&nbsp;的配置选项。</p><p style=\"text-indent: 0px; text-align: left;\">除了上述的变化，Spring&nbsp;Framework&nbsp;6.0&nbsp;还包含许多其他改进和特性，例如：</p><ul style=\"text-indent: 0px; text-align: left;\"><li>提供基于&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>@HttpExchange</code></span>&nbsp;服务接口的&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdocs.spring.io%2Fspring-framework%2Fdocs%2F6.0.0-RC1%2Freference%2Fhtml%2Fintegration.html%23rest-http-interface\" target=\"_blank\">HTTP&nbsp;接口客户端</a></li><li>对&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdocs.spring.io%2Fspring-framework%2Fdocs%2F6.0.0-RC1%2Freference%2Fhtml%2Fweb.html%23mvc-ann-rest-exceptions\" target=\"_blank\">RFC&nbsp;7807&nbsp;问题详细信息</a>的支持</li><li>Spring&nbsp;HTTP&nbsp;客户端提供基于&nbsp;Micrometer&nbsp;的可观察性</li><li>……</li></ul><p style=\"text-indent: 0px; text-align: left;\"><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Freleases%2Ftag%2Fv6.0.0-RC2\" target=\"_blank\">详情查看&nbsp;Release&nbsp;Note</a>。</p><p style=\"text-indent: 0px; text-align: left;\">按照发布计划，Spring&nbsp;Framework&nbsp;6.0&nbsp;将于&nbsp;11&nbsp;月正式&nbsp;GA。</p>', '', 0, 0, 'CSDN', '罗伊', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:30:45');
INSERT INTO `t_notice` VALUES (52, 1, 'Windows Terminal 正式成为 Windows 11 默认终端', 1, 0, '2024-01-01 20:22:23', '今年 7 月 ，微软在 Windows 11 的 Beta 版本测试了将系统默认终端设置为 Windows Terminal 。如今该设置已登录稳定版本，从 Windows 11 22H2 版本开始，Windows Terminal 将正式成为 Windows 11 的默认设置。\n默认终端是在打开命令行应用程序时默认启动的终端模拟器。从 Windows 诞生之日起，其默认终端一直是 Windows 控制台主机 conhost.exe。此次更新则意味着，以后 Windows 11 的所有命令行应用程序都将在 Windows Terminal 中自动打开。\nWindows Terminal 拥有非常多现代化的功能，毕竟它很新（ 2019 年 5 月在 Microsoft Build 上首次发布），吸取了很多现代终端的灵感。它支持多选项卡和窗格、命令面板等现代化的 UI 和操作方式，以及大量的自定义选项，比如目录、配置文件图标、自定义背景图像、配色方案、字体和透明度。\n当然，如果不想用 Windows Terminal，用户也可以在 Windows 设置中的 隐私和安全 > 开发人员页面和 Windows 终端设置 中调整默认终端设置，（此更新使用 “让 Windows 决定” 作为默认选择，即默认采用 Windows Terminal） 。\n此外，如果在更新之前就已设置其他默认终端，此次更新不会覆盖你的偏好。\n关于 Windows 11 默认终端的更多详情可查看微软博客。', '<p style=\"text-indent: 0px; text-align: left;\">今年&nbsp;7&nbsp;月&nbsp;，微软在&nbsp;Windows&nbsp;11&nbsp;的&nbsp;Beta&nbsp;版本<a href=\"https://www.oschina.net/news/204429/wt-default-terminal-in-win11-beta-channel\" target=\"\">测试</a>了将系统默认终端设置为&nbsp;Windows&nbsp;Terminal&nbsp;。如今该设置已登录稳定版本，从&nbsp;Windows&nbsp;11&nbsp;22H2&nbsp;版本开始，Windows&nbsp;Terminal&nbsp;将<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdevblogs.microsoft.com%2Fcommandline%2Fwindows-terminal-is-now-the-default-in-windows-11%2F\" target=\"_blank\">正式成为</a>&nbsp;Windows&nbsp;11&nbsp;的默认设置。</p><p style=\"text-indent: 0px; text-align: left;\">默认终端是在打开命令行应用程序时默认启动的终端模拟器。从&nbsp;Windows&nbsp;诞生之日起，其默认终端一直是&nbsp;Windows&nbsp;控制台主机&nbsp;conhost.exe。此次更新则意味着，以后&nbsp;Windows&nbsp;11&nbsp;的所有命令行应用程序都将在&nbsp;Windows&nbsp;Terminal&nbsp;中自动打开。</p><p style=\"text-indent: 0px; text-align: left;\">Windows&nbsp;Terminal&nbsp;拥有非常多现代化的功能，毕竟它<span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">很新（&nbsp;2019&nbsp;年&nbsp;5&nbsp;月在&nbsp;Microsoft&nbsp;Build&nbsp;上首次发布），吸取了很多现代终端的灵感。它支持多</span>选项卡和窗格、命令面板等现代化的&nbsp;UI&nbsp;和操作方式，以及大量的自定义选项，比如目录、配置文件图标、自定义背景图像、配色方案、字体和透明度。</p><p style=\"text-indent: 0px; text-align: left;\">当然，如果不想用&nbsp;Windows&nbsp;Terminal，用户也可以在&nbsp;Windows&nbsp;设置中的&nbsp;<em>隐私和安全&nbsp;&gt;&nbsp;开发人员页面和&nbsp;Windows&nbsp;终端设置&nbsp;</em>中调整默认终端设置，（此更新使用&nbsp;“让&nbsp;Windows&nbsp;决定”&nbsp;作为默认选择，即默认采用&nbsp;Windows&nbsp;Terminal）&nbsp;。</p><p style=\"text-indent: 0px; text-align: left;\">此外，如果在更新之前就已设置其他默认终端，此次更新<strong>不会覆盖</strong>你的偏好。</p><p style=\"text-indent: 0px; text-align: left;\">关于&nbsp;Windows&nbsp;11&nbsp;默认终端的更多详情可查看<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdevblogs.microsoft.com%2Fcommandline%2Fwindows-terminal-is-now-the-default-in-windows-11%2F\" target=\"_blank\">微软博客</a>。</p>', '', 0, 0, '开源中国', '善逸', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:33:03');
INSERT INTO `t_notice` VALUES (53, 1, 'TypeScript 诞生 10 周年', 1, 0, '2024-01-01 20:22:23', 'TypeScript 已经诞生 10 年了。10 年前 ——2012 年 10 月 1 日，TypeScript 首次公开亮相。当时主导 TypeScript 开发的 Anders Hejlsberg 这样描述 TypeScript：\n它是 JavaScript 的类型化超集，可被编译成常用的 JavaScript。TypeScript 还可以通过启用丰富的工具体验来极大地帮助提升生产力，与此同时开发者保持不变维护现有的代码，并继续使用喜爱的 JavaScript 库。TypeScript is a typed superset of JavaScript that compiles to idiomatic (normal) JavaScript, can dramatically improve your productivity by enabling rich tooling experiences, all while maintaining your existing code and continuing to use the same JavaScript libraries you already love.\n微软在博客中回顾了 TypeScript 刚亮相时受到的评价，大多数人对它都是持怀疑态度，毕竟这对于许多 JavaScript 开发者来说，试图将静态类型引入 JavaScript 是一个笑话 —— 或是邪恶的阴谋。反对者则直言这是十分愚蠢的想法，他们认为当时已存在可以编译为 JavaScript 的强类型语言，例如 C#、Java 和 C++。他们还吐槽主导 TypeScript 开发的 Anders Hejlsberg 对静态类型有 “迷之执着”。\n当时微软意识到 JavaScript 未来将会被应用到无数场景，而且他们公司内部团队在处理复杂的 JavaScript 代码库时面临着巨大的挑战，所以他们觉得有必要创造强大的工具来帮助编写 JavaScript—— 尤其是针对大型 JavaScript 项目。基于此需求，TypeScript 也确定了自己的定位和特性，它是 JavaScript 的超集，将类型检查和静态分析、显式接口和最佳实践结合到单一语言和编译器中。通过在 JavaScript 上构建，TypeScript 能够更接近目标运行时，同时仅添加支持大型应用程序和大型团队所需的语法糖。\n团队还坚持 TypeScript 要能够与现有的 JavaScript 无缝交互，与 JavaScript 共同进化，并且看上去也和 JavaScript 类似。\nTypeScript 诞生之初的部分设计目标：\n不会对已有的程序增加运行时开销与当前和未来的 ECMAScript 提案保持一致保留所有 JavaScript 代码的运行时行为避免添加表达式类型的语法 (expression-level syntax)使用一致、完全可擦除的结构化类型系统……\n这些目标指导着 TypeScript 的发展方向：关注类型系统，成为 JavaScript 的类型检查器，只添加类型检查所需的语法，避免添加新的运行时语法和行为。\n微软提到，TypeScript 拥有如今的繁荣生态离不开一个重要属性：开源。TypeScript 一开始就是免费且开源 —— 语言规范和编译器都是开源项目，并且以真正开放的方式来运作。事实上，微软当时对外展现出的姿态并不是现在的 “拥抱开源”，所以他们内部并没真正认识到 TypeScript 的开源是如何帮助它走向成功。因此有人认为，TypeScript 在很大程度上引导微软开始更多地转向开源。\n现在，TypeScript 仍在积极发展和迭代改进，并被全球数百万开发者使用。在诸多编程语言排名、指数或开发者调查中，TypeScript 一直位居前列，也是最受欢迎和最常用的编程语言。', '<p style=\"text-indent: 0px; text-align: start;\">TypeScript&nbsp;已经诞生&nbsp;10&nbsp;年了。10&nbsp;年前&nbsp;——2012&nbsp;年&nbsp;10&nbsp;月&nbsp;1&nbsp;日，TypeScript&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fweb.archive.org%2Fweb%2F20121003001910%2Fhttps%3A%2F%2Fblogs.msdn.com%2Fb%2Fsomasegar%2Farchive%2F2012%2F10%2F01%2Ftypescript-javascript-development-at-application-scale.aspx\" target=\"_blank\"><strong>首次公开亮相</strong></a>。当时主导&nbsp;TypeScript&nbsp;开发的&nbsp;Anders&nbsp;Hejlsberg&nbsp;这样描述&nbsp;TypeScript：</p><blockquote style=\"text-indent: 0px; text-align: left;\">它是&nbsp;JavaScript&nbsp;的类型化超集，可被编译成常用的&nbsp;JavaScript。TypeScript&nbsp;还可以通过启用丰富的工具体验来极大地帮助提升生产力，与此同时开发者保持不变维护现有的代码，并继续使用喜爱的&nbsp;JavaScript&nbsp;库。TypeScript&nbsp;is&nbsp;a&nbsp;typed&nbsp;superset&nbsp;of&nbsp;JavaScript&nbsp;that&nbsp;compiles&nbsp;to&nbsp;idiomatic&nbsp;(normal)&nbsp;JavaScript,&nbsp;can&nbsp;dramatically&nbsp;improve&nbsp;your&nbsp;productivity&nbsp;by&nbsp;enabling&nbsp;rich&nbsp;tooling&nbsp;experiences,&nbsp;all&nbsp;while&nbsp;maintaining&nbsp;your&nbsp;existing&nbsp;code&nbsp;and&nbsp;continuing&nbsp;to&nbsp;use&nbsp;the&nbsp;same&nbsp;JavaScript&nbsp;libraries&nbsp;you&nbsp;already&nbsp;love.</blockquote><p style=\"text-indent: 0px; text-align: left;\">微软在博客中回顾了&nbsp;TypeScript&nbsp;刚亮相时受到的评价，大多数人对它都是持怀疑态度，毕竟这对于许多&nbsp;JavaScript&nbsp;开发者来说，试图将静态类型引入&nbsp;JavaScript&nbsp;是一个笑话&nbsp;——&nbsp;或是邪恶的阴谋。反对者则直言这是十分愚蠢的想法，他们认为当时已存在可以编译为&nbsp;JavaScript&nbsp;的强类型语言，例如&nbsp;C#、Java&nbsp;和&nbsp;C++。他们还吐槽主导&nbsp;TypeScript&nbsp;开发的&nbsp;Anders&nbsp;Hejlsberg&nbsp;对静态类型有&nbsp;“迷之执着”。</p><p style=\"text-indent: 0px; text-align: start;\">当时微软意识到&nbsp;JavaScript&nbsp;未来将会被应用到无数场景，而且他们公司内部团队在处理复杂的&nbsp;JavaScript&nbsp;代码库时面临着巨大的挑战，所以他们觉得有必要创造强大的工具来帮助编写&nbsp;JavaScript——&nbsp;尤其是针对大型&nbsp;JavaScript&nbsp;项目。基于此需求，TypeScript&nbsp;也确定了自己的定位和特性，它是&nbsp;JavaScript&nbsp;的超集，将类型检查和静态分析、显式接口和最佳实践结合到单一语言和编译器中。通过在&nbsp;JavaScript&nbsp;上构建，TypeScript&nbsp;能够更接近目标运行时，同时仅添加支持大型应用程序和大型团队所需的语法糖。</p><p style=\"text-indent: 0px; text-align: start;\">团队还坚持&nbsp;TypeScript&nbsp;要能够与现有的&nbsp;JavaScript&nbsp;无缝交互，与&nbsp;JavaScript&nbsp;共同进化，并且看上去也和&nbsp;JavaScript&nbsp;类似。</p><p style=\"text-indent: 0px; text-align: start;\">TypeScript&nbsp;诞生之初的部分<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fmicrosoft%2FTypeScript%2Fwiki%2FTypeScript-Design-Goals%2F53ffa9b1802cd8e18dfe4b2cd4e9ef5d4182df10\" target=\"_blank\"><strong>设计目标</strong></a>：</p><ul style=\"text-indent: 0px; text-align: left;\"><li>不会对已有的程序增加运行时开销</li><li>与当前和未来的&nbsp;ECMAScript&nbsp;提案保持一致</li><li>保留所有&nbsp;JavaScript&nbsp;代码的运行时行为</li><li>避免添加表达式类型的语法&nbsp;(expression-level&nbsp;syntax)</li><li>使用一致、完全可擦除的结构化类型系统</li><li>……</li></ul><p style=\"text-indent: 0px; text-align: start;\">这些目标指导着&nbsp;TypeScript&nbsp;的发展方向：关注类型系统，成为&nbsp;JavaScript&nbsp;的类型检查器，只添加类型检查所需的语法，避免添加新的运行时语法和行为。</p><p style=\"text-indent: 0px; text-align: start;\">微软提到，TypeScript&nbsp;拥有如今的繁荣生态离不开一个重要属性：<strong>开源</strong>。TypeScript&nbsp;一开始就是免费且开源&nbsp;——<span style=\"color: rgb(51, 51, 51);\">&nbsp;语言规范和编译器都是开源项目，</span>并且以真正开放的方式来运作。事实上，微软当时对外展现出的姿态并不是现在的&nbsp;“拥抱开源”，所以他们内部并没真正认识到&nbsp;TypeScript&nbsp;的开源是如何帮助它走向成功。因此有人认为，TypeScript&nbsp;在很大程度上引导微软开始更多地转向开源。</p><p style=\"text-indent: 0px; text-align: start;\">现在，TypeScript&nbsp;仍在积极发展和迭代改进，并被全球数百万开发者使用。在诸多编程语言排名、指数或开发者调查中，TypeScript&nbsp;一直位居前列，也是最受欢迎和最常用的编程语言。</p>', '', 0, 0, '开源中国', '开云', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:34:56');
INSERT INTO `t_notice` VALUES (54, 1, 'JetBrains Fleet 公测，下一代 IDE', 1, 0, '2024-01-01 20:22:23', 'JetBrains 宣布首次公共预览 Fleet，所有人都可以使用。Fleet 是由 JetBrains 打造的下一代 IDE，于 2021 年首次正式推出。它是一个新的分布式多语言编辑器和 IDE，基于 JetBrains 在后端的 IntelliJ 平台，采用了全新的用户界面和分布式架构从头开始构建。\n下载 Fleet：https://www.jetbrains.com.cn/fleet/download/\n\n公告表示，自从最初宣布 Fleet 以来，有超过 137,000 人报名参加私人预览；官方最初之所以决定从封闭式预览开始，是为了能够以渐进的方式处理反馈。现如今，JetBrains Fleet 仍处于起步阶段，还有大量的工作要做。其向公众开放预览的原因有两个方面：“首先，我们认为让所有注册者再等下去是不对的，但单独邀请这么多人对我们来说也缺乏意义。面向公众开放预览对我们来说更容易。第二，也是最重要的，我们一直是一家以开放态度打造产品的公司。我们不希望 Fleet 在这方面有任何不同。”\nJetBrains 方面提供了一个图表，以显示 Fleet 目前提供支持的语言和技术，以及每个技术的状态。但值得注意的是，Fleet 仍处于早期阶段，有些事情可能无法按预期工作；所以即使有些东西被列为受支持的，也有可能存在问题。\n同时 JetBrains 也强调称，他们并不打算取代其现有的 IDE。\n因此，请不要期望在 Fleet 中看到与我们的 IDE（如 IntelliJ IDEA）完全相同的功能。尽管我们会继续开发 Fleet，我们 IDE 的所有功能也不会出现在其中。Fleet 是我们为开发者提供不同用户体验的一个机会。话虽如此，我们确实希望听到你认为 Fleet 还缺少什么功能的反馈，例如特定的重构选项、工具集成等。我们现有的 IDE 将继续发展。我们对其有很多计划，包括性能改进、新的用户界面、远程开发等等。最后，Fleet 还在底层采用了我们现有工具的智慧，所以这些工具都不会消失。\nJetBrains 透露，在未来几个月他们将致力于稳定 Fleet，并尽可能地解决得到的反馈。同时，将在以下领域开展工作：\n为插件作者提供 API 支持和 SDK–鉴于 Fleet 有一个分布式架构，我们需要努力为插件作者简化工作。 虽然我们保证会为扩展 Fleet 提供一个平台，但也请求大家在这方面多一点耐心。 性能 – 我们希望 Fleet 不仅在内存占用方面，而且在响应时间方面都能表现出色。 有很多地方我们仍然可以提高性能，我们将在这些方面努力。 主题和键盘地图 – 我们知道许多开发者已经习惯了他们现有的编辑器和 IDE，当他们转移到新的 IDE 时，往往会想念他们以前的键盘绑定和主题。 我们将致力于增加对更多主题和键盘映射的支持。 我们当然也会致力于 Vim 的模拟。\n更多详情可查看官方博客。', '<p style=\"text-indent: 0px; text-align: left;\">JetBrains&nbsp;<a href=\"https://my.oschina.net/u/5494143/blog/5584325\" target=\"\">宣布</a>首次公共预览&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fwww.jetbrains.com.cn%2Ffleet%2F\" target=\"_blank\">Fleet</a>，所有人都可以使用。Fleet&nbsp;是由&nbsp;JetBrains&nbsp;打造的下一代&nbsp;IDE，于&nbsp;2021&nbsp;年首次正式<a href=\"https://my.oschina.net/u/5494143/blog/5332934\" target=\"\">推出</a>。它是一个新的分布式多语言编辑器和&nbsp;IDE，基于&nbsp;JetBrains&nbsp;在后端的&nbsp;IntelliJ&nbsp;平台，采用了全新的用户界面和分布式架构从头开始构建。</p><p style=\"text-indent: 0px; text-align: left;\"><strong>下载&nbsp;Fleet：</strong><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fwww.jetbrains.com.cn%2Ffleet%2Fdownload%2F\" target=\"_blank\">https://www.jetbrains.com.cn/fleet/download/</a></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">公告表示，自从最初宣布&nbsp;Fleet&nbsp;以来，有超过&nbsp;137,000&nbsp;人报名参加私人预览；官方最初之所以决定从封闭式预览开始，是为了能够以渐进的方式处理反馈。现如今，JetBrains&nbsp;Fleet&nbsp;仍处于起步阶段，还有大量的工作要做。其向公众开放预览的原因有两个方面：“首先，我们认为让所有注册者再等下去是不对的，但单独邀请这么多人对我们来说也缺乏意义。面向公众开放预览对我们来说更容易。第二，也是最重要的，我们一直是一家以开放态度打造产品的公司。我们不希望&nbsp;Fleet&nbsp;在这方面有任何不同。”</p><p style=\"text-indent: 0px; text-align: left;\">JetBrains&nbsp;方面提供了一个<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fjb.gg%2Ffleet-feature-matrix\" target=\"_blank\">图表</a>，以显示&nbsp;Fleet&nbsp;目前提供支持的语言和技术，以及每个技术的状态。但值得注意的是，Fleet&nbsp;仍处于早期阶段，有些事情可能无法按预期工作；所以即使有些东西被列为受支持的，也有可能存在问题。</p><p style=\"text-indent: 0px; text-align: left;\">同时&nbsp;JetBrains&nbsp;也强调称，他们并不打算取代其现有的&nbsp;IDE。</p><blockquote style=\"text-indent: 0px; text-align: left;\">因此，请不要期望在&nbsp;Fleet&nbsp;中看到与我们的&nbsp;IDE（如&nbsp;IntelliJ&nbsp;IDEA）完全相同的功能。尽管我们会继续开发&nbsp;Fleet，我们&nbsp;IDE&nbsp;的所有功能也不会出现在其中。Fleet&nbsp;是我们为开发者提供不同用户体验的一个机会。话虽如此，我们确实希望听到你认为&nbsp;Fleet&nbsp;还缺少什么功能的反馈，例如特定的重构选项、工具集成等。我们现有的&nbsp;IDE&nbsp;将继续发展。我们对其有很多计划，包括性能改进、新的用户界面、远程开发等等。最后，Fleet&nbsp;还在底层采用了我们现有工具的智慧，所以这些工具都不会消失。</blockquote><p style=\"text-indent: 0px; text-align: start;\">JetBrains&nbsp;透露，在未来几个月他们将致力于稳定&nbsp;Fleet，并尽可能地解决得到的反馈。同时，将在以下领域开展工作：</p><ul style=\"text-indent: 0px; text-align: left;\"><li><strong>为插件作者提供&nbsp;API&nbsp;支持和&nbsp;SDK</strong>–鉴于&nbsp;Fleet&nbsp;有一个<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fblog.jetbrains.com%2Fzh-hans%2Ffleet%2F2022%2F01%2Ffleet-below-deck-part-i-architecture-overview%2F\" target=\"_blank\">分布式架构</a>，我们需要努力为插件作者简化工作。&nbsp;虽然我们保证会为扩展&nbsp;Fleet&nbsp;提供一个平台，但也请求大家在这方面多一点耐心。&nbsp;</li><li><strong>性能</strong>&nbsp;–&nbsp;我们希望&nbsp;Fleet&nbsp;不仅在内存占用方面，而且在响应时间方面都能表现出色。&nbsp;有很多地方我们仍然可以提高性能，我们将在这些方面努力。&nbsp;</li><li><strong>主题和键盘地图</strong>&nbsp;–&nbsp;我们知道许多开发者已经习惯了他们现有的编辑器和&nbsp;IDE，当他们转移到新的&nbsp;IDE&nbsp;时，往往会想念他们以前的键盘绑定和主题。&nbsp;我们将致力于增加对更多主题和键盘映射的支持。&nbsp;我们当然也会致力于&nbsp;Vim&nbsp;的模拟。</li></ul><p style=\"text-indent: 0px; text-align: left;\">更多详情可<a href=\"https://my.oschina.net/u/5494143/blog/5584325\" target=\"\">查看官方博客</a>。</p>', '', 0, 0, 'CSDN', '开云', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:36:10');
INSERT INTO `t_notice` VALUES (55, 2, '1024创新实验室 十一放假通知', 1, 0, '2024-01-01 20:22:23', '国庆假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：10月1日至7日放假调休，共7天。\n衷心预祝\n国庆快乐，阖家幸福！', '<p style=\"text-indent: 0px; text-align: justify;\">国庆假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：<strong>10月1日至7日放假调休</strong>，共7天。</p><p style=\"text-indent: 0px; text-align: justify;\"><strong>衷心预祝</strong></p><p style=\"text-indent: 0px; text-align: justify;\"><strong>国庆快乐，阖家幸福！</strong></p>', '', 0, 0, '人力行政部', '卓大', '1024创新实验室发〔2022〕字第36号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:37:57');
INSERT INTO `t_notice` VALUES (56, 2, '十月份技术分享会议', 1, 0, '2024-01-01 20:22:23', '尊敬的各位技术大佬：\n1024创新实验室技术分享即将隆重举行\n现将有关会议事宜通知如下：\n一、会议内容\n1、研究探讨SmartAdmin的技术体系\n二、会议形式\n大会专题小会分组讨论;\n三、会议时间及地点\n会议报到时间：xxx1年6月14日\n会议报到地点：洛阳市', '<p style=\"text-indent: 0px; text-align: start;\">尊敬的各位技术大佬：</p><p style=\"text-indent: 0px; text-align: start;\">1024创新实验室技术分享即将隆重举行</p><p style=\"text-indent: 0px; text-align: start;\">现将有关会议事宜通知如下：</p><p style=\"text-indent: 0px; text-align: start;\"><strong>一、会议内容</strong></p><p style=\"text-indent: 0px; text-align: start;\">1、研究探讨SmartAdmin的技术体系</p><p style=\"text-indent: 0px; text-align: start;\"><strong>二、会议形式</strong></p><p style=\"text-indent: 0px; text-align: start;\">大会专题小会分组讨论;</p><p style=\"text-indent: 0px; text-align: start;\"><strong>三、会议时间及地点</strong></p><p style=\"text-indent: 0px; text-align: start;\">会议报到时间：xxx1年6月14日</p><p style=\"text-indent: 0px; text-align: start;\">会议报到地点：洛阳市</p>', '', 0, 0, '技术部', '开云', '1024创新实验室发〔2022〕字第33号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:40:45');
INSERT INTO `t_notice` VALUES (57, 2, '关于疫情防控上班通知', 1, 0, '2024-01-01 20:22:23', '近期，国内部分地区疫情频发，多地疫情出现外溢，为有效降低我市疫情输入和传播风险，洛阳市疾病预防控制中心发布疫情防控公众提示：\n一、所有入（返）洛阳人员均需提前3天向目的地社区（村居）、酒店宾馆、接待单位等所属网格进行报备，或通过“洛阳即时通系统”进行自主报备，配合做好健康码和行程码查验、核酸检测、隔离观察和健康监测等相关疫情防控措施。\n二、倡导广大群众减少跨地市出行，避免人群大范围流动引发的疫情传播扩散风险。\n三、对7天内有高风险区旅居史的人员，采取7天集中隔离医学观察；对7天内有中风险区旅居史的人员，采取7天居家隔离医学观察，如不具备居家隔离医学观察条件的，采取集中隔离医学观察。\n四、对疫情发生地出现一定范围社区传播或已实施大范围社区管控措施，基于对疫情输入风险研判结果，对近7天内来自疫情发生地所在县（市、区）的流入人员，参照中风险区旅居史人员的防控要求采取相应措施。\n五、对所有省外入（返）洛阳人员，须持有48小时内核酸检测阴性证明，抵达后进行“5天3检”，每次检测间隔24小时。推广“落地检”，按照“自愿免费即采即走，不限制流动”的原则，抵达我市后，立即进行1次核酸检测。\n六、加强重点机构场所疫情防控，坚持非必要不举办，对确需举办的培训、会展、文艺演出等大型聚集性活动，查验48小时内核酸检测阴性证明；建筑工地等人员密集型单位，查验外省（区、市）返岗人员48小时内核酸检测阴性证明；养老机构、儿童福利机构等查验探访人员48小时内核酸检测阴性证明；对进入宾馆、酒店和旅游景区等人流密集场所时，查验48小时内核酸检测阴性证明。\n七、近期有外出旅行史的人员，请密切关注疫情发生地区公布的病例和无症状感染者流调轨迹信息和中高风险区信息。有涉疫风险的人员要立即向社区（村）、住宿宾馆和单位报告，配合落实隔离医学观察。\n八、发热病人、健康码“黄码”等人员要履行个人防护责任，主动配合健康监测和核酸检测，在未排除感染风险前不出行。\n', '<p style=\"text-indent: 0px; text-align: justify;\">近期，国内部分地区疫情频发，多地疫情出现外溢，为有效降低我市疫情输入和传播风险，洛阳市疾病预防控制中心发布疫情防控公众提示：</p><p style=\"text-indent: 0px; text-align: justify;\">一、所有入（返）洛阳人员均需提前3天向目的地社区（村居）、酒店宾馆、接待单位等所属网格进行报备，或通过“洛阳即时通系统”进行自主报备，配合做好健康码和行程码查验、核酸检测、隔离观察和健康监测等相关疫情防控措施。</p><p style=\"text-indent: 0px; text-align: justify;\">二、倡导广大群众减少跨地市出行，避免人群大范围流动引发的疫情传播扩散风险。</p><p style=\"text-indent: 0px; text-align: justify;\">三、对7天内有高风险区旅居史的人员，采取7天集中隔离医学观察；对7天内有中风险区旅居史的人员，采取7天居家隔离医学观察，如不具备居家隔离医学观察条件的，采取集中隔离医学观察。</p><p style=\"text-indent: 0px; text-align: justify;\">四、对疫情发生地出现一定范围社区传播或已实施大范围社区管控措施，基于对疫情输入风险研判结果，对近7天内来自疫情发生地所在县（市、区）的流入人员，参照中风险区旅居史人员的防控要求采取相应措施。</p><p style=\"text-indent: 0px; text-align: justify;\">五、对所有省外入（返）洛阳人员，须持有48小时内核酸检测阴性证明，抵达后进行“5天3检”，每次检测间隔24小时。推广“落地检”，按照“自愿免费即采即走，不限制流动”的原则，抵达我市后，立即进行1次核酸检测。</p><p style=\"text-indent: 0px; text-align: justify;\">六、加强重点机构场所疫情防控，坚持非必要不举办，对确需举办的培训、会展、文艺演出等大型聚集性活动，查验48小时内核酸检测阴性证明；建筑工地等人员密集型单位，查验外省（区、市）返岗人员48小时内核酸检测阴性证明；养老机构、儿童福利机构等查验探访人员48小时内核酸检测阴性证明；对进入宾馆、酒店和旅游景区等人流密集场所时，查验48小时内核酸检测阴性证明。</p><p style=\"text-indent: 0px; text-align: justify;\">七、近期有外出旅行史的人员，请密切关注疫情发生地区公布的病例和无症状感染者流调轨迹信息和中高风险区信息。有涉疫风险的人员要立即向社区（村）、住宿宾馆和单位报告，配合落实隔离医学观察。</p><p style=\"text-indent: 0px; text-align: justify;\">八、发热病人、健康码“黄码”等人员要履行个人防护责任，主动配合健康监测和核酸检测，在未排除感染风险前不出行。</p><p style=\"text-indent: 0px; text-align: justify;\"><br></p>', '', 0, 0, '行政部', '卓大', '1024创新实验室发〔2022〕字第40号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:46:00');
INSERT INTO `t_notice` VALUES (58, 2, '办公室消杀关键位置通知', 1, 0, '2024-01-01 20:22:23', '开展消毒消杀是杀灭病源、切断疫情传播的有效手段，是防控疫情的重要措施。为了切实将新型冠状病毒肺炎疫情防控工作落到实处，守护好辖区居民及工作人员的身体健康和生命安全，青山镇高度重视新型冠状病毒肺炎的消杀工作，将采购的防护服，防护面罩，一次性手套，口罩，84消毒液，酒精消毒液以及喷雾工具等消毒消杀物资，分发到镇级各站所各村（社区），全镇开展消杀工作。', '<p><span style=\"color: rgb(93, 93, 93); background-color: rgb(247, 247, 247);\">开展消毒消杀是杀灭病源、切断疫情传播的有效手段，是防控疫情的重要措施。为了切实将新型冠状病毒肺炎疫情防控工作落到实处，守护好辖区居民及工作人员的身体健康和生命安全，青山镇高度重视新型冠状病毒肺炎的消杀工作，将采购的防护服，防护面罩，一次性手套，口罩，84消毒液，酒精消毒液以及喷雾工具等消毒消杀物资，分发到镇级各站所各村（社区），全镇开展消杀工作。</span></p>', '', 0, 0, '行政部', '卓大', '1024创新实验室发〔2022〕字第26号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:47:12');
INSERT INTO `t_notice` VALUES (59, 2, '十月份人事任命通知', 1, 0, '2024-01-01 20:22:23', '1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号', '<p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p>', '', 0, 0, '销售部', '卓大', '1024创新实验室发〔2022〕字第30号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:50:11');
INSERT INTO `t_notice` VALUES (60, 2, '1024创新实验室 春节放假通知', 1, 0, '2024-01-01 20:22:23', '春节假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：10月1日至7日放假调休，共7天。\n衷心预祝\n国庆快乐，阖家幸福！', '<p style=\"text-indent: 0px; text-align: justify;\">国庆假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：<strong>10月1日至7日放假调休</strong>，共7天。</p><p style=\"text-indent: 0px; text-align: justify;\"><strong>衷心预祝</strong></p><p style=\"text-indent: 0px; text-align: justify;\"><strong>国庆快乐，阖家幸福！</strong></p>', '', 0, 0, '人力行政部', '卓大', '1024创新实验室发〔2022〕字第36号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:37:57');

-- ----------------------------
-- Table structure for t_notice_type
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_type`;
CREATE TABLE `t_notice_type`  (
  `notice_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知类型',
  `notice_type_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_notice_type
-- ----------------------------
INSERT INTO `t_notice_type` VALUES (1, '新闻', '2022-08-16 20:29:15', '2024-09-03 21:44:42');
INSERT INTO `t_notice_type` VALUES (2, '通知', '2022-08-16 20:29:20', '2022-08-16 20:29:20');

-- ----------------------------
-- Table structure for t_notice_view_record
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_view_record`;
CREATE TABLE `t_notice_view_record`  (
  `notice_id` bigint NOT NULL COMMENT '通知公告id',
  `employee_id` bigint NOT NULL COMMENT '员工id',
  `page_view_count` int NULL DEFAULT 0 COMMENT '查看次数',
  `first_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首次ip',
  `first_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首次用户设备等标识',
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次ip',
  `last_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次用户设备等标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_id`, `employee_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知查看记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_notice_view_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_notice_visible_range
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_visible_range`;
CREATE TABLE `t_notice_visible_range`  (
  `notice_id` bigint NOT NULL COMMENT '资讯id',
  `data_type` tinyint NOT NULL COMMENT '数据类型1员工 2部门',
  `data_id` bigint NOT NULL COMMENT '员工or部门id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE INDEX `uk_notice_data`(`notice_id` ASC, `data_type` ASC, `data_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知可见范围' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_notice_visible_range
-- ----------------------------
INSERT INTO `t_notice_visible_range` VALUES (63, 1, 63, '2024-08-09 10:40:32');

-- ----------------------------
-- Table structure for t_oa_bank
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_bank`;
CREATE TABLE `t_oa_bank`  (
  `bank_id` bigint NOT NULL AUTO_INCREMENT COMMENT '银行信息ID',
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开户银行',
  `account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户名称',
  `account_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `business_flag` tinyint(1) NOT NULL COMMENT '是否对公',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`bank_id`) USING BTREE,
  INDEX `idx_enterprise_id`(`enterprise_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OA银行信息\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oa_bank
-- ----------------------------
INSERT INTO `t_oa_bank` VALUES (26, '工商银行', '1024创新实验室', '1024', '基本户', 1, 2, 0, 0, 1, '管理员', '2022-10-22 17:58:43', '2022-10-22 17:58:43');
INSERT INTO `t_oa_bank` VALUES (27, '建设银行', '1024创新实验室', '10241', '其他户', 0, 2, 0, 0, 1, '管理员', '2022-10-22 17:59:19', '2022-10-22 17:59:19');

-- ----------------------------
-- Table structure for t_oa_enterprise
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_enterprise`;
CREATE TABLE `t_oa_enterprise`  (
  `enterprise_id` bigint NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `enterprise_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业名称',
  `enterprise_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业logo',
  `type` int NOT NULL DEFAULT 1 COMMENT '类型（1:有限公司;2:合伙公司）',
  `unified_social_credit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统一社会信用代码',
  `contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人',
  `contact_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `province_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份名称',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `city_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市名称',
  `district` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区县',
  `district_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区县名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `business_license` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`enterprise_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OA企业模块\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oa_enterprise
-- ----------------------------
INSERT INTO `t_oa_enterprise` VALUES (1, '1024创新区块链实验室', 'public/common/34f5ac0fc097402294aea75352c128f0_20240306112435.png', 1, '1024lab_block', '开云', '18637925892', NULL, '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '区块链大楼', 'public/common/1d89055e5680426280446aff1e7e627c_20240306112451.jpeg', 0, 0, 1, '管理员', '2021-10-22 17:03:35', '2022-10-22 17:04:18');
INSERT INTO `t_oa_enterprise` VALUES (2, '1024创新实验室', '', 2, '1024lab', '卓大', '18637925892', 'lab1024@163.com', '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '1024大楼', 'public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg', 0, 0, 44, '卓大', '2022-10-22 14:57:36', '2022-10-22 17:03:57');

-- ----------------------------
-- Table structure for t_oa_enterprise_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_enterprise_employee`;
CREATE TABLE `t_oa_enterprise_employee`  (
  `enterprise_employee_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enterprise_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单ID',
  `employee_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货物名称',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`enterprise_employee_id`) USING BTREE,
  UNIQUE INDEX `uk_enterprise_employee`(`enterprise_id` ASC, `employee_id` ASC) USING BTREE,
  INDEX `idx_employee_id`(`employee_id` ASC) USING BTREE,
  INDEX `idx_enterprise_id`(`enterprise_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业关联的员工' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oa_enterprise_employee
-- ----------------------------
INSERT INTO `t_oa_enterprise_employee` VALUES (154, '2', '2', '2022-10-22 17:57:50', '2022-10-22 17:57:50');
INSERT INTO `t_oa_enterprise_employee` VALUES (155, '2', '44', '2022-10-22 17:57:50', '2022-10-22 17:57:50');

-- ----------------------------
-- Table structure for t_oa_invoice
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_invoice`;
CREATE TABLE `t_oa_invoice`  (
  `invoice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '发票信息ID',
  `invoice_heads` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开票抬头',
  `taxpayer_identification_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '纳税人识别号',
  `account_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行账户',
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开户行',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`invoice_id`) USING BTREE,
  INDEX `idx_enterprise_id`(`enterprise_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OA发票信息\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oa_invoice
-- ----------------------------
INSERT INTO `t_oa_invoice` VALUES (15, '1024创新实验室', '1024lab', '1024lab', '中国银行', '123', 2, 0, 0, 1, '管理员', '2022-10-22 17:59:35', '2023-09-27 16:26:07');

-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operate_log`;
CREATE TABLE `t_operate_log`  (
  `operate_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate_user_id` bigint NOT NULL COMMENT '用户id',
  `operate_user_type` int NOT NULL COMMENT '用户类型',
  `operate_user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `module` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作内容',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求路径',
  `method` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求方法',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回值',
  `ip` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求ip',
  `ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求ip地区',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求user-agent',
  `success_flag` tinyint NULL DEFAULT NULL COMMENT '请求结果 0失败 1成功',
  `fail_reason` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '失败原因',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`operate_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4626 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_operate_log
-- ----------------------------
INSERT INTO `t_operate_log` VALUES (4499, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 13:02:43', '2025-12-03 13:02:43');
INSERT INTO `t_operate_log` VALUES (4500, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 13:02:43', '2025-12-03 13:02:43');
INSERT INTO `t_operate_log` VALUES (4501, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:04:51', '2025-12-03 14:04:51');
INSERT INTO `t_operate_log` VALUES (4502, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:04:51', '2025-12-03 14:04:51');
INSERT INTO `t_operate_log` VALUES (4503, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:19:08', '2025-12-03 14:19:08');
INSERT INTO `t_operate_log` VALUES (4504, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:19:08', '2025-12-03 14:19:08');
INSERT INTO `t_operate_log` VALUES (4505, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:19:08', '2025-12-03 14:19:08');
INSERT INTO `t_operate_log` VALUES (4506, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:19:09', '2025-12-03 14:19:09');
INSERT INTO `t_operate_log` VALUES (4507, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:25:42', '2025-12-03 14:25:42');
INSERT INTO `t_operate_log` VALUES (4508, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:25:42', '2025-12-03 14:25:42');
INSERT INTO `t_operate_log` VALUES (4509, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:26:16', '2025-12-03 14:26:16');
INSERT INTO `t_operate_log` VALUES (4510, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:26:17', '2025-12-03 14:26:17');
INSERT INTO `t_operate_log` VALUES (4511, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-03 14:41:18', '2025-12-03 14:41:18');
INSERT INTO `t_operate_log` VALUES (4512, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 11:59:45', '2025-12-04 11:59:45');
INSERT INTO `t_operate_log` VALUES (4513, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 11:59:45', '2025-12-04 11:59:45');
INSERT INTO `t_operate_log` VALUES (4514, 1, 1, '管理员', 'OA办公-通知公告', '通知公告类型-获取全部 @author 卓大', '/oa/noticeType/getAll', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.getAll', '', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 11:59:50', '2025-12-04 11:59:50');
INSERT INTO `t_operate_log` VALUES (4515, 1, 1, '管理员', 'OA办公-通知公告', '【管理】通知公告-分页查询 @author 卓大', '/oa/notice/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.query', '[{\"documentNumber\":\"\",\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 11:59:50', '2025-12-04 11:59:50');
INSERT INTO `t_operate_log` VALUES (4516, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:00:09', '2025-12-04 12:00:09');
INSERT INTO `t_operate_log` VALUES (4517, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:00:24', '2025-12-04 12:00:24');
INSERT INTO `t_operate_log` VALUES (4518, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:00:40', '2025-12-04 12:00:40');
INSERT INTO `t_operate_log` VALUES (4519, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:00:59', '2025-12-04 12:00:59');
INSERT INTO `t_operate_log` VALUES (4520, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:01:05', '2025-12-04 12:01:05');
INSERT INTO `t_operate_log` VALUES (4521, 1, 1, '管理员', 'OA办公-企业', '导出企业信息 @author 卓大', '/oa/enterprise/exportExcel', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.exportExcel', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', NULL, '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:01:19', '2025-12-04 12:01:19');
INSERT INTO `t_operate_log` VALUES (4522, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:53:08', '2025-12-04 12:53:08');
INSERT INTO `t_operate_log` VALUES (4523, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-04 12:53:08', '2025-12-04 12:53:08');
INSERT INTO `t_operate_log` VALUES (4524, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-21 08:00:32', '2025-12-21 08:00:32');
INSERT INTO `t_operate_log` VALUES (4525, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-21 08:00:32', '2025-12-21 08:00:32');
INSERT INTO `t_operate_log` VALUES (4526, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 13:20:23', '2025-12-24 13:20:23');
INSERT INTO `t_operate_log` VALUES (4527, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 13:20:23', '2025-12-24 13:20:23');
INSERT INTO `t_operate_log` VALUES (4528, 1, 1, '管理员', 'OA办公-通知公告', '通知公告类型-获取全部 @author 卓大', '/oa/noticeType/getAll', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.getAll', '', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 13:49:46', '2025-12-24 13:49:46');
INSERT INTO `t_operate_log` VALUES (4529, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"keywords\":\"\",\"notViewFlag\":false,\"pageNum\":1,\"pageSize\":10}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 13:49:46', '2025-12-24 13:49:46');
INSERT INTO `t_operate_log` VALUES (4530, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:39:36', '2025-12-24 14:39:36');
INSERT INTO `t_operate_log` VALUES (4531, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:39:36', '2025-12-24 14:39:36');
INSERT INTO `t_operate_log` VALUES (4532, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:40:20', '2025-12-24 14:40:20');
INSERT INTO `t_operate_log` VALUES (4533, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:40:20', '2025-12-24 14:40:20');
INSERT INTO `t_operate_log` VALUES (4534, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:40:41', '2025-12-24 14:40:41');
INSERT INTO `t_operate_log` VALUES (4535, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:40:41', '2025-12-24 14:40:41');
INSERT INTO `t_operate_log` VALUES (4536, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:42:56', '2025-12-24 14:42:56');
INSERT INTO `t_operate_log` VALUES (4537, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:42:57', '2025-12-24 14:42:57');
INSERT INTO `t_operate_log` VALUES (4538, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:46:23', '2025-12-24 14:46:23');
INSERT INTO `t_operate_log` VALUES (4539, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-24 14:46:23', '2025-12-24 14:46:23');
INSERT INTO `t_operate_log` VALUES (4540, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 01:30:22', '2026-01-28 01:30:22');
INSERT INTO `t_operate_log` VALUES (4541, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 01:30:22', '2026-01-28 01:30:22');
INSERT INTO `t_operate_log` VALUES (4542, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 01:30:37', '2026-01-28 01:30:37');
INSERT INTO `t_operate_log` VALUES (4543, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 01:30:37', '2026-01-28 01:30:37');
INSERT INTO `t_operate_log` VALUES (4544, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 01:51:20', '2026-01-28 01:51:20');
INSERT INTO `t_operate_log` VALUES (4545, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 01:51:20', '2026-01-28 01:51:20');
INSERT INTO `t_operate_log` VALUES (4546, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 02:41:03', '2026-01-28 02:41:03');
INSERT INTO `t_operate_log` VALUES (4547, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 02:41:03', '2026-01-28 02:41:03');
INSERT INTO `t_operate_log` VALUES (4548, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 02:41:07', '2026-01-28 02:41:07');
INSERT INTO `t_operate_log` VALUES (4549, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 02:41:07', '2026-01-28 02:41:07');
INSERT INTO `t_operate_log` VALUES (4550, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 12:21:34', '2026-01-28 12:21:34');
INSERT INTO `t_operate_log` VALUES (4551, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 12:21:34', '2026-01-28 12:21:34');
INSERT INTO `t_operate_log` VALUES (4552, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 13:22:01', '2026-01-28 13:22:01');
INSERT INTO `t_operate_log` VALUES (4553, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-28 13:22:01', '2026-01-28 13:22:01');
INSERT INTO `t_operate_log` VALUES (4554, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-29 12:43:36', '2026-01-29 12:43:36');
INSERT INTO `t_operate_log` VALUES (4555, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-29 12:43:36', '2026-01-29 12:43:36');
INSERT INTO `t_operate_log` VALUES (4556, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-29 12:56:50', '2026-01-29 12:56:50');
INSERT INTO `t_operate_log` VALUES (4557, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-29 12:56:50', '2026-01-29 12:56:50');
INSERT INTO `t_operate_log` VALUES (4558, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-29 12:56:53', '2026-01-29 12:56:53');
INSERT INTO `t_operate_log` VALUES (4559, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-29 12:56:53', '2026-01-29 12:56:53');
INSERT INTO `t_operate_log` VALUES (4560, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-31 11:39:26', '2026-01-31 11:39:26');
INSERT INTO `t_operate_log` VALUES (4561, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2026-01-31 11:39:26', '2026-01-31 11:39:26');
INSERT INTO `t_operate_log` VALUES (4562, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:16:06', '2026-02-26 12:16:06');
INSERT INTO `t_operate_log` VALUES (4563, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:16:06', '2026-02-26 12:16:06');
INSERT INTO `t_operate_log` VALUES (4564, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:18:18', '2026-02-26 12:18:18');
INSERT INTO `t_operate_log` VALUES (4565, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:18:18', '2026-02-26 12:18:18');
INSERT INTO `t_operate_log` VALUES (4566, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:18:19', '2026-02-26 12:18:19');
INSERT INTO `t_operate_log` VALUES (4567, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:18:19', '2026-02-26 12:18:19');
INSERT INTO `t_operate_log` VALUES (4568, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:20:01', '2026-02-26 12:20:01');
INSERT INTO `t_operate_log` VALUES (4569, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 12:20:01', '2026-02-26 12:20:01');
INSERT INTO `t_operate_log` VALUES (4570, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 13:00:57', '2026-02-26 13:00:57');
INSERT INTO `t_operate_log` VALUES (4571, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-02-26 13:00:57', '2026-02-26 13:00:57');
INSERT INTO `t_operate_log` VALUES (4572, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 12:37:31', '2026-03-04 12:37:31');
INSERT INTO `t_operate_log` VALUES (4573, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 12:37:31', '2026-03-04 12:37:31');
INSERT INTO `t_operate_log` VALUES (4574, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 12:44:00', '2026-03-04 12:44:00');
INSERT INTO `t_operate_log` VALUES (4575, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 12:44:00', '2026-03-04 12:44:00');
INSERT INTO `t_operate_log` VALUES (4576, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:16:25', '2026-03-04 13:16:25');
INSERT INTO `t_operate_log` VALUES (4577, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:16:25', '2026-03-04 13:16:25');
INSERT INTO `t_operate_log` VALUES (4578, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:40:18', '2026-03-04 13:40:18');
INSERT INTO `t_operate_log` VALUES (4579, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:40:25', '2026-03-04 13:40:25');
INSERT INTO `t_operate_log` VALUES (4580, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:40:27', '2026-03-04 13:40:27');
INSERT INTO `t_operate_log` VALUES (4581, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:40:27', '2026-03-04 13:40:27');
INSERT INTO `t_operate_log` VALUES (4582, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:40:32', '2026-03-04 13:40:32');
INSERT INTO `t_operate_log` VALUES (4583, 1, 1, '管理员', 'OA办公-企业', '分页查询企业模块 @author 开云', '/oa/enterprise/page/query', 'net.lab1024.sa.admin.module.business.oa.enterprise.controller.EnterpriseController.queryByPage', '[{\"deletedFlag\":false,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10,\"searchCount\":true}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-04 13:40:51', '2026-03-04 13:40:51');
INSERT INTO `t_operate_log` VALUES (4584, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 08:39:24', '2026-03-15 08:39:24');
INSERT INTO `t_operate_log` VALUES (4585, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 08:39:24', '2026-03-15 08:39:24');
INSERT INTO `t_operate_log` VALUES (4586, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 09:17:33', '2026-03-15 09:17:33');
INSERT INTO `t_operate_log` VALUES (4587, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 09:17:33', '2026-03-15 09:17:33');
INSERT INTO `t_operate_log` VALUES (4588, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 09:20:33', '2026-03-15 09:20:33');
INSERT INTO `t_operate_log` VALUES (4589, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 09:20:33', '2026-03-15 09:20:33');
INSERT INTO `t_operate_log` VALUES (4590, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 10:16:34', '2026-03-15 10:16:34');
INSERT INTO `t_operate_log` VALUES (4591, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 10:16:34', '2026-03-15 10:16:34');
INSERT INTO `t_operate_log` VALUES (4592, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 10:17:21', '2026-03-15 10:17:21');
INSERT INTO `t_operate_log` VALUES (4593, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 10:17:22', '2026-03-15 10:17:22');
INSERT INTO `t_operate_log` VALUES (4594, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 10:17:23', '2026-03-15 10:17:23');
INSERT INTO `t_operate_log` VALUES (4595, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-15 10:17:23', '2026-03-15 10:17:23');
INSERT INTO `t_operate_log` VALUES (4596, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-16 07:06:04', '2026-03-16 07:06:04');
INSERT INTO `t_operate_log` VALUES (4597, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-16 07:06:04', '2026-03-16 07:06:04');
INSERT INTO `t_operate_log` VALUES (4598, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 00:33:03', '2026-03-17 00:33:03');
INSERT INTO `t_operate_log` VALUES (4599, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 00:33:03', '2026-03-17 00:33:03');
INSERT INTO `t_operate_log` VALUES (4600, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 01:01:38', '2026-03-17 01:01:38');
INSERT INTO `t_operate_log` VALUES (4601, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 01:01:38', '2026-03-17 01:01:38');
INSERT INTO `t_operate_log` VALUES (4602, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 01:45:48', '2026-03-17 01:45:48');
INSERT INTO `t_operate_log` VALUES (4603, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 01:45:48', '2026-03-17 01:45:48');
INSERT INTO `t_operate_log` VALUES (4604, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 03:46:41', '2026-03-17 03:46:41');
INSERT INTO `t_operate_log` VALUES (4605, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 03:46:41', '2026-03-17 03:46:41');
INSERT INTO `t_operate_log` VALUES (4606, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 03:49:30', '2026-03-17 03:49:30');
INSERT INTO `t_operate_log` VALUES (4607, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 03:49:30', '2026-03-17 03:49:30');
INSERT INTO `t_operate_log` VALUES (4608, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:16', '2026-03-17 06:34:16');
INSERT INTO `t_operate_log` VALUES (4609, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:16', '2026-03-17 06:34:16');
INSERT INTO `t_operate_log` VALUES (4610, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:23', '2026-03-17 06:34:23');
INSERT INTO `t_operate_log` VALUES (4611, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:23', '2026-03-17 06:34:23');
INSERT INTO `t_operate_log` VALUES (4612, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:27', '2026-03-17 06:34:27');
INSERT INTO `t_operate_log` VALUES (4613, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:28', '2026-03-17 06:34:28');
INSERT INTO `t_operate_log` VALUES (4614, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:32', '2026-03-17 06:34:32');
INSERT INTO `t_operate_log` VALUES (4615, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:32', '2026-03-17 06:34:32');
INSERT INTO `t_operate_log` VALUES (4616, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:42', '2026-03-17 06:34:42');
INSERT INTO `t_operate_log` VALUES (4617, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 06:34:42', '2026-03-17 06:34:42');
INSERT INTO `t_operate_log` VALUES (4618, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:06:19', '2026-03-17 12:06:19');
INSERT INTO `t_operate_log` VALUES (4619, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:06:19', '2026-03-17 12:06:19');
INSERT INTO `t_operate_log` VALUES (4620, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:26:06', '2026-03-17 12:26:06');
INSERT INTO `t_operate_log` VALUES (4621, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:26:06', '2026-03-17 12:26:06');
INSERT INTO `t_operate_log` VALUES (4622, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:26:43', '2026-03-17 12:26:43');
INSERT INTO `t_operate_log` VALUES (4623, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:26:43', '2026-03-17 12:26:43');
INSERT INTO `t_operate_log` VALUES (4624, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:26:44', '2026-03-17 12:26:44');
INSERT INTO `t_operate_log` VALUES (4625, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '{\"code\":0,\"dataType\":1,\"msg\":\"操作成功\",\"ok\":true}', '0:0:0:0:0:0:0:1', '', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36', 1, NULL, '2026-03-17 12:26:44', '2026-03-17 12:26:44');

-- ----------------------------
-- Table structure for t_password_log
-- ----------------------------
DROP TABLE IF EXISTS `t_password_log`;
CREATE TABLE `t_password_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `old_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '旧密码',
  `new_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新密码',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_and_type_index`(`user_id` ASC, `user_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '密码修改记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_password_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_position
-- ----------------------------
DROP TABLE IF EXISTS `t_position`;
CREATE TABLE `t_position`  (
  `position_id` bigint NOT NULL AUTO_INCREMENT COMMENT '职务ID',
  `position_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职务名称',
  `position_level` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职级',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted_flag` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '职务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_position
-- ----------------------------
INSERT INTO `t_position` VALUES (3, '技术P7', 'L1', 3, '', 0, '2024-06-29 15:57:07', '2024-07-15 23:34:35');
INSERT INTO `t_position` VALUES (4, '技术P8', 'L2', 1, NULL, 0, '2024-07-15 23:34:14', '2024-07-15 23:34:23');
INSERT INTO `t_position` VALUES (5, '管理M5', 'L1', 4, NULL, 0, '2024-07-15 23:34:48', '2024-07-15 23:34:48');
INSERT INTO `t_position` VALUES (6, '管理M6', 'L2', 5, NULL, 0, '2024-07-15 23:35:00', '2024-07-15 23:35:00');

-- ----------------------------
-- Table structure for t_reload_item
-- ----------------------------
DROP TABLE IF EXISTS `t_reload_item`;
CREATE TABLE `t_reload_item`  (
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项名称',
  `args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数 可选',
  `identification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运行标识',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`tag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'reload项目' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_reload_item
-- ----------------------------
INSERT INTO `t_reload_item` VALUES ('system_config', '4', '234', '2024-08-13 14:14:30', '2019-04-18 11:48:27');

-- ----------------------------
-- Table structure for t_reload_result
-- ----------------------------
DROP TABLE IF EXISTS `t_reload_result`;
CREATE TABLE `t_reload_result`  (
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `identification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运行标识',
  `args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `result` tinyint UNSIGNED NOT NULL COMMENT '是否成功 ',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'reload结果' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_reload_result
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_code_uni`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, '技术总监', NULL, '', '2022-10-19 20:24:09', '2019-06-21 12:09:34');
INSERT INTO `t_role` VALUES (34, '销售总监', 'cto', '', '2023-09-06 19:10:34', '2019-08-30 09:30:50');
INSERT INTO `t_role` VALUES (35, '总经理', NULL, '', '2019-08-30 09:31:05', '2019-08-30 09:31:05');
INSERT INTO `t_role` VALUES (36, '董事长', NULL, '', '2019-08-30 09:31:11', '2019-08-30 09:31:11');
INSERT INTO `t_role` VALUES (37, '财务', NULL, '', '2019-08-30 09:31:16', '2019-08-30 09:31:16');

-- ----------------------------
-- Table structure for t_role_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_role_data_scope`;
CREATE TABLE `t_role_data_scope`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_scope_type` int NOT NULL COMMENT '数据范围类型',
  `view_type` int NOT NULL COMMENT '数据可见范围类型',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色的数据范围' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_data_scope
-- ----------------------------
INSERT INTO `t_role_data_scope` VALUES (67, 1, 2, 1, '2024-03-18 20:41:00', '2024-03-18 20:41:00');

-- ----------------------------
-- Table structure for t_role_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_role_employee`;
CREATE TABLE `t_role_employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL COMMENT '角色id',
  `employee_id` bigint NOT NULL COMMENT '员工id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_employee`(`role_id` ASC, `employee_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 342 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色员工功能表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_employee
-- ----------------------------
INSERT INTO `t_role_employee` VALUES (325, 36, 63, '2022-10-19 20:25:26', '2022-10-19 20:25:26');
INSERT INTO `t_role_employee` VALUES (329, 34, 72, '2022-11-05 10:56:54', '2022-11-05 10:56:54');
INSERT INTO `t_role_employee` VALUES (330, 36, 72, '2022-11-05 10:56:54', '2022-11-05 10:56:54');
INSERT INTO `t_role_employee` VALUES (333, 1, 44, '2023-10-07 18:53:29', '2023-10-07 18:53:29');
INSERT INTO `t_role_employee` VALUES (334, 1, 47, '2023-10-07 18:55:00', '2023-10-07 18:55:00');
INSERT INTO `t_role_employee` VALUES (341, 1, 48, '2024-09-02 23:03:28', '2024-09-02 23:03:28');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `role_menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 820 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色-菜单\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES (236, 1, 138, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (237, 1, 132, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (238, 1, 142, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (239, 1, 149, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (240, 1, 150, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (241, 1, 185, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (242, 1, 186, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (243, 1, 187, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (244, 1, 188, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (245, 1, 145, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (246, 1, 196, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (247, 1, 144, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (248, 1, 181, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (249, 1, 183, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (250, 1, 184, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (251, 1, 165, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (252, 1, 47, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (253, 1, 48, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (254, 1, 137, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (255, 1, 166, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (256, 1, 194, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (257, 1, 78, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (258, 1, 173, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (259, 1, 174, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (260, 1, 175, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (261, 1, 176, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (262, 1, 50, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (263, 1, 26, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (264, 1, 40, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (265, 1, 105, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (266, 1, 106, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (267, 1, 109, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (268, 1, 163, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (269, 1, 164, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (270, 1, 199, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (271, 1, 110, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (272, 1, 159, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (273, 1, 160, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (274, 1, 161, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (275, 1, 162, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (276, 1, 130, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (277, 1, 157, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (278, 1, 158, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (279, 1, 133, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (280, 1, 117, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (281, 1, 156, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (282, 1, 193, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (283, 1, 200, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (284, 1, 220, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (285, 1, 45, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (286, 1, 219, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (287, 1, 46, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (288, 1, 91, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (289, 1, 92, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (290, 1, 93, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (291, 1, 94, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (292, 1, 95, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (293, 1, 96, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (294, 1, 86, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (295, 1, 87, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (296, 1, 88, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (297, 1, 76, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (298, 1, 97, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (299, 1, 98, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (300, 1, 99, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (301, 1, 100, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (302, 1, 101, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (303, 1, 102, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (304, 1, 103, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (305, 1, 104, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (306, 1, 213, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (307, 1, 214, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (308, 1, 143, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (309, 1, 203, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (310, 1, 215, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (311, 1, 218, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (312, 1, 147, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (313, 1, 170, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (314, 1, 171, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (315, 1, 168, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (316, 1, 169, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (317, 1, 202, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (318, 1, 201, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (319, 1, 148, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (320, 1, 152, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (321, 1, 190, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (322, 1, 191, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (323, 1, 192, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (324, 1, 198, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (325, 1, 207, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (326, 1, 111, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (327, 1, 206, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (328, 1, 81, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (329, 1, 204, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (330, 1, 205, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` VALUES (331, 1, 122, '2024-06-30 23:21:37', '2024-06-30 23:21:37');

-- ----------------------------
-- Table structure for t_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `t_serial_number`;
CREATE TABLE `t_serial_number`  (
  `serial_number_id` int NOT NULL,
  `business_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务名称',
  `format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '格式[yyyy]表示年,[mm]标识月,[dd]表示日,[nnn]表示三位数字',
  `rule_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则格式。none没有周期, year 年周期, month月周期, day日周期',
  `init_number` int UNSIGNED NOT NULL COMMENT '初始值',
  `step_random_range` int UNSIGNED NOT NULL COMMENT '步长随机数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `last_number` bigint NULL DEFAULT NULL COMMENT '上次产生的单号, 默认为空',
  `last_time` datetime NULL DEFAULT NULL COMMENT '上次产生的单号时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`serial_number_id`) USING BTREE,
  UNIQUE INDEX `key_name`(`business_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '单号生成器定义表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_serial_number
-- ----------------------------
INSERT INTO `t_serial_number` VALUES (1, '订单编号', 'DK[yyyy][mm][dd]NO[nnnnn]', 'day', 1000, 10, 'DK20201101NO321', 1, '2023-12-04 09:16:42', '2024-01-08 19:24:46', '2021-02-19 14:37:50');
INSERT INTO `t_serial_number` VALUES (2, '合同编号', 'HT[yyyy][mm][dd][nnnnn]-CX', 'none', 1, 1, '', 8, '2023-12-04 09:54:53', '2023-12-04 09:54:52', '2021-08-12 20:40:37');

-- ----------------------------
-- Table structure for t_serial_number_record
-- ----------------------------
DROP TABLE IF EXISTS `t_serial_number_record`;
CREATE TABLE `t_serial_number_record`  (
  `serial_number_id` int NOT NULL,
  `record_date` date NOT NULL COMMENT '记录日期',
  `last_number` bigint NOT NULL DEFAULT 0 COMMENT '最后更新值',
  `last_time` datetime NOT NULL COMMENT '最后更新时间',
  `count` bigint NOT NULL DEFAULT 0 COMMENT '更新次数',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `uk_generator`(`serial_number_id` ASC, `record_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'serial_number记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_serial_number_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_smart_job
-- ----------------------------
DROP TABLE IF EXISTS `t_smart_job`;
CREATE TABLE `t_smart_job`  (
  `job_id` int NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_class` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务执行类',
  `trigger_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发类型',
  `trigger_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发配置',
  `enabled_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启',
  `param` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `last_execute_time` datetime NULL DEFAULT NULL COMMENT '最后一次执行时间',
  `last_execute_log_id` int NULL DEFAULT NULL COMMENT '最后一次执行记录id',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务配置 @listen' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_smart_job
-- ----------------------------
INSERT INTO `t_smart_job` VALUES (1, '示例任务1', 'net.lab1024.sa.base.module.support.job.sample.SmartJobSample1', 'cron', '10 15 0/1 * * *', 1, '执行示例任务1', '2026-03-17 20:15:10', 7934, 1, '执行示例任务1', 0, '管理员', '2024-06-17 20:00:46', '2026-03-17 12:15:18');
INSERT INTO `t_smart_job` VALUES (2, '示例任务2', 'net.lab1024.sa.base.module.support.job.sample.SmartJobSample2', 'fixed_delay', '120', 1, '执行示例任务2', '2026-03-17 20:26:08', 7939, 2, '执行示例任务2', 0, '管理员', '2024-06-18 20:45:35', '2026-03-17 12:26:15');

-- ----------------------------
-- Table structure for t_smart_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_smart_job_log`;
CREATE TABLE `t_smart_job_log`  (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `job_id` int NOT NULL COMMENT '任务id',
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行参数',
  `success_flag` tinyint(1) NOT NULL COMMENT '是否成功',
  `execute_start_time` datetime NOT NULL COMMENT '执行开始时间',
  `execute_time_millis` int NULL DEFAULT NULL COMMENT '执行时长',
  `execute_end_time` datetime NULL DEFAULT NULL COMMENT '执行结束时间',
  `execute_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip',
  `process_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '进程id',
  `program_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '程序目录',
  `create_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_job_id`(`job_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7940 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务-执行记录 @listen' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_smart_job_log
-- ----------------------------
INSERT INTO `t_smart_job_log` VALUES (7933, 2, '示例任务2', '执行示例任务2', 1, '2026-03-17 20:14:05', 278, '2026-03-17 20:14:05', '执行成功,本次处理数据1条', '10.239.211.58', '24336', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:14:13');
INSERT INTO `t_smart_job_log` VALUES (7934, 1, '示例任务1', '执行示例任务1', 1, '2026-03-17 20:15:10', 0, '2026-03-17 20:15:10', '执行完毕,随便说点什么吧', '10.239.211.58', '24336', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:15:18');
INSERT INTO `t_smart_job_log` VALUES (7935, 2, '示例任务2', '执行示例任务2', 1, '2026-03-17 20:16:06', 254, '2026-03-17 20:16:06', '执行成功,本次处理数据1条', '10.239.211.58', '24336', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:16:14');
INSERT INTO `t_smart_job_log` VALUES (7936, 2, '示例任务2', '执行示例任务2', 1, '2026-03-17 20:20:05', 253, '2026-03-17 20:20:05', '执行成功,本次处理数据1条', '10.239.211.58', '53716', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:20:13');
INSERT INTO `t_smart_job_log` VALUES (7937, 2, '示例任务2', '执行示例任务2', 1, '2026-03-17 20:22:06', 268, '2026-03-17 20:22:06', '执行成功,本次处理数据1条', '10.239.211.58', '53716', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:22:14');
INSERT INTO `t_smart_job_log` VALUES (7938, 2, '示例任务2', '执行示例任务2', 1, '2026-03-17 20:24:07', 273, '2026-03-17 20:24:07', '执行成功,本次处理数据1条', '10.239.211.58', '53716', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:24:15');
INSERT INTO `t_smart_job_log` VALUES (7939, 2, '示例任务2', '执行示例任务2', 1, '2026-03-17 20:26:08', 260, '2026-03-17 20:26:08', '执行成功,本次处理数据1条', '10.239.211.58', '53716', 'F:\\project\\smart-admin', 'system', '2026-03-17 12:26:15');

-- ----------------------------
-- Table structure for t_table_column
-- ----------------------------
DROP TABLE IF EXISTS `t_table_column`;
CREATE TABLE `t_table_column`  (
  `table_column_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `table_id` int NOT NULL COMMENT '表格id',
  `columns` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '具体的表格列，存入的json',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`table_column_id`) USING BTREE,
  UNIQUE INDEX `uni_employee_table`(`user_id` ASC, `table_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表格的自定义列存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_table_column
-- ----------------------------
INSERT INTO `t_table_column` VALUES (6, 1, 1, 30002, '[{\"columnKey\":\"unifiedSocialCreditCode\",\"showFlag\":true,\"sort\":1},{\"columnKey\":\"type\",\"showFlag\":true,\"sort\":2,\"width\":100},{\"columnKey\":\"enterpriseName\",\"showFlag\":true,\"sort\":3},{\"columnKey\":\"contact\",\"showFlag\":true,\"sort\":4,\"width\":100},{\"columnKey\":\"contactPhone\",\"showFlag\":true,\"sort\":5,\"width\":120},{\"columnKey\":\"email\",\"showFlag\":true,\"sort\":6},{\"columnKey\":\"disabledFlag\",\"showFlag\":true,\"sort\":7,\"width\":50},{\"columnKey\":\"createUserName\",\"showFlag\":true,\"sort\":8,\"width\":60},{\"columnKey\":\"createTime\",\"showFlag\":true,\"sort\":9,\"width\":150},{\"columnKey\":\"action\",\"showFlag\":true,\"sort\":10,\"width\":100}]', '2025-12-04 12:00:22', '2025-12-04 12:00:22');

SET FOREIGN_KEY_CHECKS = 1;
