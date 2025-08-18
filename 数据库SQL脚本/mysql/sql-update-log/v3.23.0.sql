
-- ----------------------------
-- Table structure for t_position
-- ----------------------------
DROP TABLE IF EXISTS `t_position`;
CREATE TABLE `t_position` (
  `position_id` bigint NOT NULL AUTO_INCREMENT COMMENT '职务ID',
  `position_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职务名称',
  `position_level` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '职级',
  `sort` int DEFAULT '0' COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `deleted_flag` tinyint(1) DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='职务表';

-- ----------------------------
-- Records of t_position
-- ----------------------------

INSERT INTO `t_position`(`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (3, '技术P7', 'L1', 3, '', 0, '2024-06-29 15:57:07', '2024-07-15 23:34:35');
INSERT INTO `t_position`(`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (4, '技术P8', 'L2', 1, NULL, 0, '2024-07-15 23:34:14', '2024-07-15 23:34:23');
INSERT INTO `t_position`(`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (5, '管理M5', 'L1', 4, NULL, 0, '2024-07-15 23:34:48', '2024-07-15 23:34:48');
INSERT INTO `t_position`(`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (6, '管理M6', 'L2', 5, NULL, 0, '2024-07-15 23:35:00', '2024-07-15 23:35:00');




-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operate_log`;
CREATE TABLE `t_operate_log` (
  `operate_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate_user_id` bigint NOT NULL COMMENT '用户id',
  `operate_user_type` int NOT NULL COMMENT '用户类型',
  `operate_user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `module` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作模块',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '操作内容',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求路径',
  `method` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求方法',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `ip` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求ip',
  `ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求ip地区',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求user-agent',
  `success_flag` tinyint DEFAULT NULL COMMENT '请求结果 0失败 1成功',
  `fail_reason` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '失败原因',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`operate_log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4499 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作记录';


