ALTER TABLE `t_ai_model_config`
  ADD COLUMN `extra_config_json` text COMMENT '扩展配置JSON' AFTER `path`;

CREATE TABLE IF NOT EXISTS `t_ai_merch_task` (
  `task_id` bigint NOT NULL,
  `source_file_id` bigint NOT NULL COMMENT '源图片文件ID',
  `source_file_key` varchar(255) NOT NULL COMMENT '源图片文件Key',
  `prompt_template` text NOT NULL COMMENT '提示词1',
  `prompt_model_id` bigint NOT NULL COMMENT '提示词模型ID',
  `prompt_model_name` varchar(120) NOT NULL COMMENT '提示词模型名称',
  `image_model_id` bigint NOT NULL COMMENT '图片模型ID',
  `image_model_name` varchar(120) NOT NULL COMMENT '图片模型名称',
  `video_model_id` bigint NOT NULL COMMENT '视频模型ID',
  `video_model_name` varchar(120) NOT NULL COMMENT '视频模型名称',
  `generated_prompt` text COMMENT '生成后的成品提示词',
  `prompt_status` varchar(20) NOT NULL DEFAULT 'WAITING' COMMENT '提示词状态',
  `image_status` varchar(20) NOT NULL DEFAULT 'WAITING' COMMENT '图片状态',
  `video_status` varchar(20) NOT NULL DEFAULT 'WAITING' COMMENT '视频状态',
  `image_task_id` varchar(100) DEFAULT NULL COMMENT '图片上游任务号',
  `video_task_id` varchar(100) DEFAULT NULL COMMENT '视频上游任务号',
  `image_result_url` varchar(500) DEFAULT NULL COMMENT '图片结果远程地址',
  `video_result_url` varchar(500) DEFAULT NULL COMMENT '视频结果远程地址',
  `image_file_id` bigint DEFAULT NULL COMMENT '图片文件ID',
  `image_file_key` varchar(255) DEFAULT NULL COMMENT '图片文件Key',
  `video_file_id` bigint DEFAULT NULL COMMENT '视频文件ID',
  `video_file_key` varchar(255) DEFAULT NULL COMMENT '视频文件Key',
  `prompt_error_msg` varchar(500) DEFAULT NULL COMMENT '提示词错误信息',
  `image_error_msg` varchar(500) DEFAULT NULL COMMENT '图片错误信息',
  `video_error_msg` varchar(500) DEFAULT NULL COMMENT '视频错误信息',
  `create_by` varchar(30) DEFAULT NULL,
  `create_by_name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(30) DEFAULT NULL,
  `update_by_name` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`task_id`),
  KEY `idx_ai_merch_task_create_time` (`create_time`),
  KEY `idx_ai_merch_task_status` (`prompt_status`, `image_status`, `video_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI带货任务';

DELETE FROM `t_menu`
WHERE `path` = '/ai/merch/config'
   OR `api_perms` IN ('ai:merch:config:query', 'ai:merch:config:add', 'ai:merch:config:update', 'ai:merch:config:delete');

INSERT INTO `t_menu`
(`menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `frame_flag`, `cache_flag`, `visible_flag`, `disabled_flag`, `perms_type`, `icon`, `create_user_id`)
VALUES
('AI带货', 1, 0, 90, '/ai-merch', '/business/ai/merch', 0, 0, 1, 0, 1, 'appstore-outlined', 1);

SET @ai_merch_catalog_id = LAST_INSERT_ID();

INSERT INTO `t_menu`
(`menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `frame_flag`, `cache_flag`, `visible_flag`, `disabled_flag`, `perms_type`, `create_user_id`)
VALUES
('带货工作台', 2, @ai_merch_catalog_id, 1, '/ai/merch/workbench', '/business/ai/merch/ai-merch-task-workbench.vue', 0, 0, 1, 0, 1, 1);

SELECT menu_id INTO @ai_merch_task_menu_id FROM t_menu WHERE menu_name = '带货工作台' AND parent_id = @ai_merch_catalog_id LIMIT 1;

INSERT INTO `t_menu`
(`menu_name`, `menu_type`, `parent_id`, `frame_flag`, `cache_flag`, `visible_flag`, `disabled_flag`, `perms_type`, `api_perms`, `web_perms`, `context_menu_id`, `create_user_id`)
VALUES
('查询任务', 3, @ai_merch_task_menu_id, 0, 0, 1, 0, 1, 'ai:merch:query', 'ai:merch:query', @ai_merch_task_menu_id, 1),
('创建任务', 3, @ai_merch_task_menu_id, 0, 0, 1, 0, 1, 'ai:merch:create', 'ai:merch:create', @ai_merch_task_menu_id, 1),
('生成图片', 3, @ai_merch_task_menu_id, 0, 0, 1, 0, 1, 'ai:merch:generateImage', 'ai:merch:generateImage', @ai_merch_task_menu_id, 1),
('生成视频', 3, @ai_merch_task_menu_id, 0, 0, 1, 0, 1, 'ai:merch:generateVideo', 'ai:merch:generateVideo', @ai_merch_task_menu_id, 1);
