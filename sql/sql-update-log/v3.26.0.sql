-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
ALTER TABLE `t_operate_log` ADD COLUMN `response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '返回结果' AFTER `param` ;
