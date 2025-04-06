DROP TABLE IF EXISTS `t_dict_key`;

DROP TABLE IF EXISTS `t_dict_value`;


-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `dict_name` varchar(500) NOT NULL COMMENT '字典名字',
  `dict_code` varchar(500) NOT NULL COMMENT '字典编码',
  `remark` varchar(1000) DEFAULT NULL COMMENT '字典备注',
  `disabled_flag` tinyint NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `unique_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';


-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict`(`dict_id`, `dict_name`, `dict_code`, `remark`, `disabled_flag`, `create_time`, `update_time`) VALUES (1, '商品地区', 'GOODS_PLACE', '用于商品管理中的商品地区1', 0, '2025-03-27 14:42:26', '2025-03-31 11:23:03');

-- ----------------------------
-- Table structure for t_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_data`;
CREATE TABLE `t_dict_data` (
  `dict_data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典数据id',
  `dict_id` bigint NOT NULL COMMENT '字典id',
  `data_value` varchar(500) NOT NULL COMMENT '字典项值',
  `data_label` varchar(500) NOT NULL COMMENT '字典项显示名称',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `sort_order` int NOT NULL COMMENT '排序（越大越靠前）',
  `disabled_flag` tinyint NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典数据表';

-- ----------------------------
-- Records of t_dict_data
-- ----------------------------
INSERT INTO `t_dict_data` VALUES (2, 1, 'LUO_YANG', '洛阳', 'sad', 2, 0, '2025-03-27 15:52:39', '2025-03-27 20:53:21');
INSERT INTO `t_dict_data` VALUES (3, 1, 'ZHENG_ZHOU', '郑州', '', 0, 0, '2025-03-27 18:58:16', '2025-03-27 20:53:32');
INSERT INTO `t_dict_data` VALUES (7, 1, 'BEI_JING', '北京', '', 0, 0, '2025-03-27 20:53:45', '2025-03-27 20:53:45');
INSERT INTO `t_dict_data` VALUES (8, 1, 'SHANG_HAI', '上海', '', 0, 0, '2025-03-27 20:53:45', '2025-03-27 20:53:45');



-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `login_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `user_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `login_ip` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ip',
  `login_ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ip地区',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'user-agent信息',
  `login_device` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录设备',
  `login_result` int NOT NULL COMMENT '登录结果：0成功 1失败 2 退出',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`login_log_id`) USING BTREE,
  KEY `customer_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1905 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录日志';