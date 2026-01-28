CREATE TABLE IF NOT EXISTS `t_ai_app`
(
    `id`              VARCHAR(36) NOT NULL UNIQUE COMMENT 'id',
    `name`            VARCHAR(50) COMMENT '应用名称',
    `description`     VARCHAR(255) COMMENT '应用描述',
    `app_provider_id` VARCHAR(36) COMMENT '应用提供商id',
    `create_by`       VARCHAR(36) COMMENT '创建人',
    `create_by_name`  VARCHAR(255) COMMENT '创建人姓名',
    `create_at`       DATETIME COMMENT '创建时间',
    `update_by`       VARCHAR(36) COMMENT '更新人',
    `update_at`       DATETIME COMMENT '更新时间',
    `update_by_name`  VARCHAR(255) COMMENT '更新人姓名',
    PRIMARY KEY (`id`),
    KEY `idx_ai_app_app_provider_id` (`app_provider_id`)
) COMMENT ='ai应用管理表';


CREATE TABLE IF NOT EXISTS `t_ai_app_provider`
(
    `id`             VARCHAR(36) NOT NULL UNIQUE COMMENT 'id',
    `name`           VARCHAR(36) COMMENT '名称',
    `base_url`       VARCHAR(255) COMMENT 'baseUrl',
    `key`            VARCHAR(255) COMMENT 'key',
    `create_at`      DATETIME COMMENT '创建时间',
    `create_by`      VARCHAR(36) COMMENT '创建人',
    `create_by_name` VARCHAR(255) COMMENT '创建人名称',
    `update_at`      DATETIME COMMENT '更新时间',
    `update_by`      VARCHAR(36) COMMENT '更新人',
    `update_by_name` VARCHAR(255) COMMENT '更新人名称',
    PRIMARY KEY (`id`)
) COMMENT ='应用提供商';


-- 模型配置表
CREATE TABLE IF NOT EXISTS `t_ai_model_config`
(
    `id`               int(11)      NOT NULL AUTO_INCREMENT,
    `provider`         varchar(255) NOT NULL COMMENT '厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)',
    `base_url`         varchar(255) NOT NULL COMMENT '关键配置',
    `api_key`          varchar(255) NOT NULL COMMENT 'API密钥',
    `model_name`       varchar(255) NOT NULL COMMENT '模型名称',
    `temperature`      decimal(10, 2) unsigned DEFAULT '0.00' COMMENT '温度参数',
    `is_active`        tinyint(1)              DEFAULT '0' COMMENT '是否激活',
    `max_tokens`       int(11)                 DEFAULT '2000' COMMENT '输出响应最大令牌数',
    `model_type`       varchar(20)  NOT NULL   DEFAULT 'CHAT' COMMENT '模型类型 (CHAT/EMBEDDING)',
    `completions_path` varchar(255)            DEFAULT NULL COMMENT 'Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions',
    `embeddings_path`  varchar(255)            DEFAULT NULL COMMENT '嵌入模型专用。附加到 Base URL 的路径。',
    `create_by`        VARCHAR(36) COMMENT '创建人',
    `create_by_name`   VARCHAR(255) COMMENT '创建人姓名',
    `create_at`        DATETIME COMMENT '创建时间',
    `update_by`        VARCHAR(36) COMMENT '更新人',
    `update_by_name`   VARCHAR(255) COMMENT '更新人姓名',
    `update_at`        DATETIME COMMENT '更新时间',
    `deleted_flag`     int(11)                 DEFAULT '0' COMMENT '0=未删除, 1=已删除',
    PRIMARY KEY (`id`)
) COMMENT ='模型配置表';


CREATE TABLE IF NOT EXISTS `t_ai_conversations`
(
    `id`             VARCHAR(36) NOT NULL UNIQUE COMMENT '会话id',
    `app_id`         VARCHAR(36) COMMENT '应用id',
    `name`           VARCHAR(255) COMMENT '名称',
    `inputs`         VARCHAR(255) COMMENT '入参',
    `summary`        VARCHAR(255) COMMENT '总结',
    `create_at`      DATETIME COMMENT '创建时间',
    `create_by`      VARCHAR(36) COMMENT '创建人',
    `create_by_name` VARCHAR(255) COMMENT '创建人姓名',
    `update_at`      DATETIME COMMENT '更新时间',
    `update_by`      VARCHAR(36) COMMENT '更新人',
    `update_by_name` VARCHAR(255) COMMENT '更新人姓名',
    PRIMARY KEY (`id`)
) COMMENT ='会话表';



CREATE TABLE IF NOT EXISTS `t_ai_messages`
(
    `id`              VARCHAR(36) NOT NULL UNIQUE COMMENT 'id',
    `conversation_id` VARCHAR(36) COMMENT '会话ID',
    `query`           TEXT COMMENT '查询',
    `inputs`          VARCHAR(255) COMMENT '入参',
    `role`            VARCHAR(20) NOT NULL COMMENT '角色：user-用户，assistant-助手，system-系统',
    `content`         TEXT COMMENT '回答',
    `create_by`       VARCHAR(36) COMMENT '创建人',
    `create_by_name`  VARCHAR(255) COMMENT '创建人姓名',
    `create_at`       DATETIME COMMENT '创建时间',
    `update_by`       VARCHAR(36) COMMENT '更新人',
    `update_by_name`  VARCHAR(255) COMMENT '更新人姓名',
    `update_at`       DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_ai_messages_conversation_id` (`conversation_id`),
    KEY `idx_ai_messages_create_at` (`create_at`)
) COMMENT ='消息表';


CREATE TABLE IF NOT EXISTS `t_ai_knowledge`
(
    `id`              VARCHAR(36) NOT NULL UNIQUE COMMENT '知识库id',
    `name`            VARCHAR(36) COMMENT '知识库名称',
    `type`            varchar(50) COMMENT 'knowledge-base',
    `description`     VARCHAR(255) COMMENT '描述',
    `permission`      varchar(20) COMMENT '权限 private-私有 public-公开',
    `embedding_model` varchar(50) COMMENT '向量模型',
    `enable_rerank`   boolean COMMENT '向量模型',
    `rerank_model`    varchar(50) COMMENT 'reRank模型 当启动rerank时使用',
    `inputs`          VARCHAR(255) COMMENT '入参',
    `create_by`       VARCHAR(36) COMMENT '创建人',
    `create_by_name`  VARCHAR(255) COMMENT '创建人姓名',
    `create_at`       DATETIME COMMENT '创建时间',
    `update_by`       VARCHAR(36) COMMENT '更新人',
    `update_by_name`  VARCHAR(255) COMMENT '更新人姓名',
    `update_at`       DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`)

) COMMENT ='知识库';



CREATE TABLE IF NOT EXISTS `t_ai_documents`
(
    `id`             VARCHAR(36) NOT NULL UNIQUE COMMENT 'id',
    `knowledge_id`   bigint COMMENT '知识库id',
    `name`           VARCHAR(36) COMMENT '文档名称',
    `word_count`     int(11) COMMENT '文档字数',
    `status`         varchar(20) COMMENT '状态 pending-待处理 success-成功 fail-失败',
    `file_id`       VARCHAR(255) COMMENT '文件id',
    `create_by`      VARCHAR(36) COMMENT '创建人',
    `create_by_name` VARCHAR(255) COMMENT '创建人姓名',
    `create_at`      DATETIME COMMENT '创建时间',
    `update_by`      VARCHAR(36) COMMENT '更新人',
    `update_by_name` VARCHAR(255) COMMENT '更新人姓名',
    `update_at`      DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`)

) COMMENT ='文档列表';

CREATE TABLE IF NOT EXISTS `t_ai_document_segments`
(
    `id`             bigint NOT NULL UNIQUE COMMENT 'id',
    `knowledge_id`   bigint COMMENT '知识库id',
    `document_id`    bigint COMMENT '文档名称',
    `type`           varchar(20) COMMENT '知识类型: DOCUMENT-文档, QA-问答',
    `word_count`     int COMMENT '文档字数',
    `position`       int COMMENT '排序',
    `content`        VARCHAR(255) COMMENT '分块内容',
    `answer`         VARCHAR(255) COMMENT '回答，QA类型才有',
    "status"         varchar(255) comment '状态 pending-待处理 processing处理中 completed成功 fail失败',
    `create_by`      VARCHAR(36) COMMENT '创建人',
    `create_by_name` VARCHAR(255) COMMENT '创建人姓名',
    `create_at`      DATETIME COMMENT '创建时间',
    `update_by`      VARCHAR(36) COMMENT '更新人',
    `update_by_name` VARCHAR(255) COMMENT '更新人姓名',
    `update_at`      DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`)

) COMMENT ='文档分块表';