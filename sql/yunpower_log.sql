

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log_execute
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_execute`;
CREATE TABLE `sys_log_execute`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `method` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行方法',
  `operate_type` int(11) NULL DEFAULT NULL COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `operate_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行人员',
  `operate_result` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行结果',
  `status` int(11) NULL DEFAULT NULL COMMENT '操作状态（0正常 1异常）',
  `execute_time` datetime(0) NULL DEFAULT NULL COMMENT '执行时间',
  `cost_time` int(11) NULL DEFAULT NULL COMMENT '消耗时间（秒）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_execute_time`(`execute_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '执行日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_login`;
CREATE TABLE `sys_log_login`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `dept_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `ipaddr` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录IP地址',
  `login_location` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `status` int(11) NULL DEFAULT NULL COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示信息',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_login_time`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 392 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log_oper
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_oper`;
CREATE TABLE `sys_log_oper`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块标题',
  `business_type` int(11) NULL DEFAULT NULL COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `operator_type` int(11) NULL DEFAULT NULL COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人员',
  `dept_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `oper_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `oper_param` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `json_result` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回参数',
  `status` int(11) NULL DEFAULT NULL COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误消息',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) NULL DEFAULT NULL COMMENT '消耗时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_oper_time`(`oper_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1088 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log_system
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_system`;
CREATE TABLE `sys_log_system`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `error_msg` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误消息',
  `occur_time` datetime(0) NULL DEFAULT NULL COMMENT '发生时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_occur_time`(`occur_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
