CREATE TABLE IF NOT EXISTS `t_ai_app`
(
    `id`              VARCHAR(36) NOT NULL UNIQUE COMMENT 'id',
    `name`            VARCHAR(50) COMMENT '应用名称',
    `description`     VARCHAR(255) COMMENT '应用描述',
    `app_provider_id` VARCHAR(10) COMMENT '应用提供商id',
    `create_by`       VARCHAR(36) COMMENT '创建人',
    `create_by_name`  VARCHAR(255) COMMENT '创建人姓名',
    `create_at`       DATETIME COMMENT '创建时间',
    `update_by`       VARCHAR(36) COMMENT '更新人',
    `update_at`       DATETIME COMMENT '更新时间',
    `update_by_name`  VARCHAR(255) COMMENT '更新人姓名',
    PRIMARY KEY (`id`)
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
    `update_by_name` VARCHAR(255) COMMENT '创建人名称',
    PRIMARY KEY (`id`)
) COMMENT ='应用提供商';


CREATE TABLE IF NOT EXISTS `t_ai_conversations`
(
    `id`             VARCHAR(36) NOT NULL UNIQUE  COMMENT 'id',
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
    `id`             VARCHAR(255) NOT NULL UNIQUE COMMENT 'id',
    `conversaton_id` VARCHAR(255) COMMENT '会话ID',
    `query`          TEXT(65535) COMMENT '查询',
    `message`        TEXT(65535) COMMENT '消息',
    `inputs`         VARCHAR(255) COMMENT '入参',
    `answer`         TEXT(65535) COMMENT '回答',
    `create_by`      VARCHAR(255) COMMENT '创建人',
    `create_by_name` VARCHAR(255) COMMENT '创建人姓名',
    `create_at`      DATETIME COMMENT '创建时间',
    `update_by`      VARCHAR(255) COMMENT '更新人',
    `update_by_name` VARCHAR(255) COMMENT '更新人姓名',
    `update_at`      DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT ='消息表';

