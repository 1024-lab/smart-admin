-- ----------------------------
-- Table structure for t_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee`  (
  `employee_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `employee_uid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工uuid',
  `login_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录帐号',
  `login_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `actual_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT 0 COMMENT '性别',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `department_id` bigint(0) NOT NULL COMMENT '部门id',
  `position_id` bigint(0) NULL DEFAULT NULL COMMENT '职务ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `disabled_flag` tinyint unsigned NOT NULL COMMENT '是否被禁用 0否1是',
  `deleted_flag` tinyint unsigned NOT NULL COMMENT '是否删除0否 1是',
  `administrator_flag` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否为超级管理员: 0 不是，1是',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`employee_id`) USING BTREE,
  UNIQUE INDEX `employee_uid_index`(`employee_uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_employee
-- ----------------------------
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (1, 'cf1e361fd46741f5b2a09335cef50db8', 'admin', '$argon2id$v=19$m=16384,t=2,p=1$d9yQEAhck+haKxP2ZtXocg$NEnw3D2Ly8UbYpy2odATLA4ZflZ1FKJjWCuOGrVE4PM', '管理员', 'public/common/f36e59b20faa4720b225edf81d15727a_20250713220349.jpeg', 0, '13500000000', 1, 3, NULL, 0, 0, 1, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (2, '3c253628b4cb4302a7bb83008a82a415', 'huke', '$argon2id$v=19$m=16384,t=2,p=1$3N9HxhPdydtmqXmTmBUxcw$Yh2jMqQ5qmCC1cgezKtFd5vuH8WirHZh6FPnFS0clEY', '胡克', NULL, 0, '13123123121', 1, 4, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (44, '5e2a57cd8eff4346be03dc2acfed0d7c', 'zhuoda', '$argon2id$v=19$m=16384,t=2,p=1$Mt02VdlsDNrteY/sBOs2uw$0gI5gfb/D4iLGi6RRlEq/4Qo71cseuz5YZrwiCj3VQI', '卓大', NULL, 1, '18637925892', 1, 6, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (47, 'b031a061076a4732aa0d63989adb1fbc', 'shanyi', '$argon2id$v=19$m=16384,t=2,p=1$lsqZF68KCPkPaF2ShNhtNQ$Zpsv0GLBeau3x0hL0JzpWtnIlNf0hh3+P6Zu5fM6gJw', '善逸', 'public/common/f823b00873684f0a9d31f0d62316cc8e_20240630015141.jpg', 1, '17630506613', 2, 5, NULL, 0, 0, 0, '这个是备注', '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (48, 'e29327485b784211aa9677a9436d2e00', 'qinjiu', '$argon2id$v=19$m=16384,t=2,p=1$ga8Ww+zlLShAzC8o54qftg$3Ete1M8/zzepZqiEV1yNu/U7svMI0EuDWVKZ9X5M1uQ', '琴酒', NULL, 2, '14112343212', 2, 6, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (63, 'cab6922aeeb949a997c93c043b909b05', 'kaiyun', '$argon2id$v=19$m=16384,t=2,p=1$5TZB3BWsbv0FXrgA60+7ag$pnDVVvjE/J0kOet3xLq19fyv1+a/KGqN6B+xsvDluYc', '开云', NULL, 0, '13112312346', 2, 5, 'ss@qq.com', 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (64, '02ce19c1c707448a81159834a60bbd94', 'qingye', '$argon2id$v=19$m=16384,t=2,p=1$X+M3CF1557PGfLavpWXCPQ$2LsEiOgLFP+VbGA/7TPAbLnkyiLollova6iETB9S/ds', '清野', NULL, 1, '13123123111', 2, 4, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (65, 'e791135b86c34435873f4c9068c0e9ba', 'feiye', '$argon2id$v=19$m=16384,t=2,p=1$cPMw0Xu3dgu4lFX1x+qUvQ$Ol6NktMqi2fGn4Djv+m5ha/DyARWkXA/y784hFVa0rQ', '飞叶', NULL, 1, '13123123112', 4, 3, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (66, '2954e985557745df844e4c88532cd8a6', 'luoyi', '$argon2id$v=19$m=16384,t=2,p=1$D0lXN4LyhLhtHaKFbS3DRw$0FK9A8F1oT38xqIZvNcu1eWsB5C5vXkwULXhvLxYmK8', '罗伊', NULL, 1, '13123123142', 4, 2, NULL, 1, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (67, '39cb2c7de94141d6824e9a167912c23d', 'chuxiao', '$argon2id$v=19$m=16384,t=2,p=1$/BdtVk/U5utWvple9bfCQw$eK8JjH+cei7gNQwPDDdP5ACQT3qkYvz5Qk4k016jRpU', '初晓', NULL, 1, '13123123123', 1, 2, NULL, 1, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
INSERT INTO `t_employee`(`employee_id`, `employee_uid`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (68, '2aaf8c8c393c46b080aca86179388d7e', 'xuanpeng', '$argon2id$v=19$m=16384,t=2,p=1$ldHEjEwCWur/RnSy0JmFJQ$nlhVYiFMELToZ9nXI5QxG4maTV/L7pyPU0GRv3+s+tg', '玄朋', NULL, 1, '13123123124', 1, 3, NULL, 0, 0, 0, NULL, '2025-07-15 10:19:23', '2022-10-04 21:33:50');
