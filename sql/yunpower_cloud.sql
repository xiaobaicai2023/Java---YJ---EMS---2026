

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alarm_trigger
-- ----------------------------
DROP TABLE IF EXISTS `alarm_trigger`;
CREATE TABLE `alarm_trigger`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备ID',
  `device_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `var_id` int(11) NULL DEFAULT NULL COMMENT '变量ID',
  `var_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量编码',
  `var_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量名称',
  `var_type` int(11) NULL DEFAULT NULL COMMENT '变量类型（1模拟量 2状态量）',
  `category_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警类型名称',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '报警类型',
  `trigger_level` int(11) NULL DEFAULT NULL COMMENT '报警级别',
  `trigger_level_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警级别名称',
  `trigger_condition_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '触发条件',
  `trigger_condition_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '触发条件编码',
  `threshold` float NULL DEFAULT NULL COMMENT '阈值',
  `real_value` double(12, 4) NULL DEFAULT NULL COMMENT '实际值',
  `trigger_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警内容',
  `is_send_sms` int(11) NULL DEFAULT NULL COMMENT '是否发送短信（0否 1是）',
  `receive_type` int(11) NULL DEFAULT NULL COMMENT '接收人员类型（1按岗位发送 1指定具体人员）',
  `receive_concrete` json NULL COMMENT '具体岗位或人员（JSON格式）',
  `happen_time` datetime(0) NULL DEFAULT NULL COMMENT '发生时间',
  `recover_time` datetime(0) NULL DEFAULT NULL COMMENT '恢复时间',
  `confirm_time` datetime(0) NULL DEFAULT NULL COMMENT '确认时间',
  `confirm_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '确认人员',
  `confirm_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '确认过程',
  `is_auto` int(11) NULL DEFAULT NULL COMMENT '是否自动恢复（0否 1是）',
  `trigger_status` int(11) NULL DEFAULT NULL COMMENT '报警状态',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_var_sn`(`var_sn`) USING BTREE,
  INDEX `Index_device_sn`(`device_sn`) USING BTREE,
  INDEX `Index_dept_id`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6503 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报警管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alarm_trigger_category
-- ----------------------------
DROP TABLE IF EXISTS `alarm_trigger_category`;
CREATE TABLE `alarm_trigger_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `trigger_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `trigger_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `trigger_level` int(11) NULL DEFAULT NULL COMMENT '报警级别',
  `trigger_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '触发类型',
  `suit_type` int(11) NULL DEFAULT NULL COMMENT '适用类型（1模拟量 2状态量）',
  `is_alarm` int(11) NULL DEFAULT NULL COMMENT '报警提示',
  `alarm_method` json NULL COMMENT '报警方式（JSON格式）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报警类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alarm_trigger_config
-- ----------------------------
DROP TABLE IF EXISTS `alarm_trigger_config`;
CREATE TABLE `alarm_trigger_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备ID',
  `device_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `var_id` int(11) NULL DEFAULT NULL COMMENT '变量ID',
  `var_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量编码',
  `var_type` int(11) NULL DEFAULT NULL COMMENT '变量类型（1模拟量 2状态量）',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '报警类型',
  `threshold` float NULL DEFAULT NULL COMMENT '阈值',
  `is_send_sms` int(11) NULL DEFAULT NULL COMMENT '是否发送短信（0否 1是）',
  `receive_type` int(11) NULL DEFAULT NULL COMMENT '接收人员类型（1按岗位发送 1指定具体人员）',
  `receive_concrete` json NULL COMMENT '具体岗位或人员（JSON格式）',
  `start_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警开始时间',
  `end_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警结束时间',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_var_sn`(`var_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报警配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for communication_channel
-- ----------------------------
DROP TABLE IF EXISTS `communication_channel`;
CREATE TABLE `communication_channel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `channel_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道名称',
  `channel_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  `access_type` int(11) NULL DEFAULT NULL COMMENT '接入方式1（1TCP 2MQTT）',
  `ip_address` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `port` int(11) NULL DEFAULT NULL COMMENT '端口',
  `registration_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通讯注册码',
  `code_start` int(11) NULL DEFAULT NULL COMMENT '注册码起始位置',
  `code_length` int(11) NULL DEFAULT NULL COMMENT '注册码长度',
  `registration_rsp` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册回应码',
  `conn_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接地址',
  `conn_port` int(11) NULL DEFAULT NULL COMMENT '连接端口',
  `conn_username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接帐号',
  `conn_pwd` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接密码',
  `subscribe_topic` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订阅主题',
  `publish_topic` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布主题',
  `timeout` int(11) NULL DEFAULT NULL COMMENT '连接超时（s）',
  `schedule_interval` int(11) NULL DEFAULT NULL COMMENT '调度周期（ms）',
  `online_time` datetime(0) NULL DEFAULT NULL COMMENT '上线时间',
  `offline_time` datetime(0) NULL DEFAULT NULL COMMENT '下线时间',
  `is_active` int(11) NULL DEFAULT NULL COMMENT '是否激活',
  `active_update_time` datetime(0) NULL DEFAULT NULL COMMENT '激活时间',
  `connect_alarm` int(11) NULL DEFAULT NULL COMMENT '通道连接报警',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_sn_u`(`channel_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通讯通道' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for communication_device
-- ----------------------------
DROP TABLE IF EXISTS `communication_device`;
CREATE TABLE `communication_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `channel_id` int(11) NULL DEFAULT NULL COMMENT '所属通道ID',
  `channel_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属通道编码',
  `connect_model` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接协议（1104 2ModbusRTU 3ModbusTCP 4DLT645）',
  `timeout` int(11) NULL DEFAULT NULL COMMENT '连接超时（s）',
  `online_time` datetime(0) NULL DEFAULT NULL COMMENT '上线时间',
  `offline_time` datetime(0) NULL DEFAULT NULL COMMENT '下线时间',
  `is_active` int(11) NULL DEFAULT NULL COMMENT '是否激活',
  `active_update_time` datetime(0) NULL DEFAULT NULL COMMENT '激活时间',
  `connect_alarm` int(11) NULL DEFAULT NULL COMMENT '通道连接报警',
  `sbdz` int(11) NULL DEFAULT NULL COMMENT '设备地址（Modbus）',
  `csyy_length` int(11) NULL DEFAULT NULL COMMENT '传送原因长度',
  `ggdz` int(11) NULL DEFAULT NULL COMMENT '公共地址（104）',
  `ggdz_length` int(11) NULL DEFAULT NULL COMMENT '公共地址长度',
  `info_length` int(11) NULL DEFAULT NULL COMMENT '信息体地址长度',
  `timeout_t0` int(11) NULL DEFAULT NULL COMMENT '超时T0（s）',
  `timeout_t1` int(11) NULL DEFAULT NULL COMMENT '超时T1（s）',
  `timeout_t2` int(11) NULL DEFAULT NULL COMMENT '超时T2（s）',
  `timeout_t3` int(11) NULL DEFAULT NULL COMMENT '超时T3（s）',
  `ass_call_cycle` int(11) NULL DEFAULT NULL COMMENT '总召采集周期',
  `elc_call_cycle` int(11) NULL DEFAULT NULL COMMENT '电度总召周期',
  `data_area` json NULL COMMENT '数据区域（JSON格式）',
  `pro_type` int(11) NULL DEFAULT NULL COMMENT '产品类型（1通讯管理机 2DTU）',
  `pro_factory` int(11) NULL DEFAULT NULL COMMENT '生产厂家',
  `pro_ver` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品版本',
  `pro_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品SN号',
  `pro_model` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品型号',
  `card_operator` int(11) NULL DEFAULT NULL COMMENT '运营商',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物联卡号',
  `card_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `card_flow` int(11) NULL DEFAULT NULL COMMENT '流量标准',
  `card_postage` float NULL DEFAULT NULL COMMENT '资费标准',
  `card_expire_date` datetime(0) NULL DEFAULT NULL COMMENT '资费到期',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_sn_u`(`device_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通讯设备' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for communication_device_area
-- ----------------------------
DROP TABLE IF EXISTS `communication_device_area`;
CREATE TABLE `communication_device_area`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备ID',
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `area_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `add_start` int(11) NULL DEFAULT NULL COMMENT '起始地址',
  `add_length` int(11) NULL DEFAULT NULL COMMENT '长度',
  `area` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄存器',
  `rw` int(11) NULL DEFAULT NULL COMMENT '读写（1只读 2只写 3读写）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通讯设备数据区域' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for communication_device_area_map
-- ----------------------------
DROP TABLE IF EXISTS `communication_device_area_map`;
CREATE TABLE `communication_device_area_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `protocol` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '协议',
  `area` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '寄存器区',
  `rw` int(11) NULL DEFAULT NULL COMMENT '读写（1只读 2只写 3读写）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通讯设备数据区域类型（公共）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dashboard_config
-- ----------------------------
DROP TABLE IF EXISTS `dashboard_config`;
CREATE TABLE `dashboard_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `page_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面名称',
  `page_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面key 唯一 页面路由+站点ID',
  `page_type` tinyint(4) NULL DEFAULT NULL COMMENT '页面类型 1:大屏页面 2:概览页面 3:通用页面(统一顶部搜索)',
  `page_config` json NULL COMMENT '页面配置json',
  `page_config_pre` json NULL COMMENT '页面配置json 用于预览',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_page_key_page_type`(`page_key`, `page_type`) USING BTREE COMMENT '页面配置key'
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图表仪表盘配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dashboard_config_card
-- ----------------------------
DROP TABLE IF EXISTS `dashboard_config_card`;
CREATE TABLE `dashboard_config_card`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `dashboard_config_id` int(11) NOT NULL COMMENT '配置ID',
  `card_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡片名称',
  `card_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡片key',
  `date_dim` int(11) NULL DEFAULT NULL COMMENT '日期维度（日、月、年）取字典sys_config_page_x值',
  `second_date_dim` int(11) NULL DEFAULT NULL COMMENT '日期维度（5、10、15、30分钟、1时）取字典sys_config_page_x值',
  `is_full_date` int(11) NULL DEFAULT NULL COMMENT '是否全日期（发电时用到）（0否 1是）',
  `indicator` int(11) NULL DEFAULT NULL COMMENT '指标度量（1指定变量  2通用变量）',
  `chart_type` int(11) NULL DEFAULT NULL COMMENT '图表类型',
  `card_config` json NULL COMMENT '卡片配置json',
  `card_config_pre` json NULL COMMENT '卡片配置json 用于预览查看',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 168 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图表仪表盘卡片配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for devops_maintenance
-- ----------------------------
DROP TABLE IF EXISTS `devops_maintenance`;
CREATE TABLE `devops_maintenance`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `maintain_title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维保名称',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型',
  `station_id` int(11) NULL DEFAULT NULL COMMENT '维保站点',
  `device_id_for` json NULL COMMENT '维保设备（多个，JSON格式）',
  `user_name_for` json NULL COMMENT '维保人员（多个，JSON格式）',
  `maintain_time` datetime(0) NULL DEFAULT NULL COMMENT '维保时间',
  `maintain_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维保内容',
  `maintain_files` json NULL COMMENT '图片描述（JSON格式）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '维保记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for devops_order
-- ----------------------------
DROP TABLE IF EXISTS `devops_order`;
CREATE TABLE `devops_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型',
  `order_title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工单名称',
  `station_id` int(11) NULL DEFAULT NULL COMMENT '维修站点',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '维修设备',
  `alarm_id` int(11) NULL DEFAULT NULL COMMENT '关联报警',
  `priority_level` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级',
  `order_type` int(11) NULL DEFAULT NULL COMMENT '工单类型（1普通 2抢修）',
  `plan_start_time` datetime(0) NULL DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` datetime(0) NULL DEFAULT NULL COMMENT '计划结束时间',
  `order_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工单描述',
  `order_files` json NULL COMMENT '图片描述（JSON格式）',
  `charge_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `collaborate_for` json NULL COMMENT '协作人（多人，JSON格式）',
  `copy_for` json NULL COMMENT '抄送人（多人，JSON格式）',
  `verify_for` json NULL COMMENT '验证人（多人，JSON格式）',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '接单时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `complete_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `use_minutes` int(11) NULL DEFAULT NULL COMMENT '实时用时（分钟）',
  `handle_process` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理过程',
  `handle_files` json NULL COMMENT '现场照片（JSON格式）',
  `verify_time` datetime(0) NULL DEFAULT NULL COMMENT '验证时间',
  `verify_advice` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证意见',
  `timeout_status` int(11) NULL DEFAULT NULL COMMENT '超时状态（0正常 1已超时）',
  `status` int(11) NULL DEFAULT NULL COMMENT '工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '工单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for electric_price_scheme
-- ----------------------------
DROP TABLE IF EXISTS `electric_price_scheme`;
CREATE TABLE `electric_price_scheme`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `electric_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电价名称',
  `province` int(11) NULL DEFAULT NULL COMMENT '地址省',
  `city` int(11) NULL DEFAULT NULL COMMENT '地址市',
  `county` int(11) NULL DEFAULT NULL COMMENT '地址县',
  `electric_group` int(11) NULL DEFAULT NULL COMMENT '用电分类',
  `electric_standard` int(11) NULL DEFAULT NULL COMMENT '用电标准',
  `voltage_level` int(11) NULL DEFAULT NULL COMMENT '电压等级',
  `effect_year` int(11) NULL DEFAULT NULL COMMENT '生效年份',
  `start_date` datetime(0) NULL DEFAULT NULL COMMENT '执行开始日期',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '执行结束日期',
  `electric_text` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电度电价',
  `capacity_price` float NULL DEFAULT NULL COMMENT '容量电价',
  `demand_price` float NULL DEFAULT NULL COMMENT '需量电价',
  `demand_percent` float NULL DEFAULT NULL COMMENT '需量计费（超）',
  `demand_rate` float NULL DEFAULT NULL COMMENT '需量计费（倍）',
  `electric_up` float NULL DEFAULT NULL COMMENT '电量达到（千瓦）',
  `electric_up_rate` float NULL DEFAULT NULL COMMENT '电量达到（倍）',
  `deviation_price` json NULL COMMENT '偏差电价（JSON格式）',
  `additive_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加征电价（名称）',
  `additive_price` float NULL DEFAULT NULL COMMENT '加征电价（价格）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电价标准' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for electric_price_scheme_config
-- ----------------------------
DROP TABLE IF EXISTS `electric_price_scheme_config`;
CREATE TABLE `electric_price_scheme_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `scheme_id` int(11) NULL DEFAULT NULL COMMENT '电价标准ID',
  `effect_year` int(11) NULL DEFAULT NULL COMMENT '生效年份',
  `effect_month` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生效月份集合',
  `price_type` int(11) NULL DEFAULT NULL COMMENT '电价类型：1分时电价 2统一电价',
  `uniform_price` float NULL DEFAULT NULL COMMENT '统一电价',
  `jian_price` float NULL DEFAULT NULL COMMENT '尖峰电价',
  `feng_price` float NULL DEFAULT NULL COMMENT '峰段电价',
  `ping_price` float NULL DEFAULT NULL COMMENT '平段电价',
  `gu_price` float NULL DEFAULT NULL COMMENT '谷段电价',
  `shen_price` float NULL DEFAULT NULL COMMENT '深谷电价',
  `whole_price` float NULL DEFAULT NULL COMMENT '全天电价',
  `seasonal_range` json NULL COMMENT '时间段（JSON格式）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电度电价配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_device
-- ----------------------------
DROP TABLE IF EXISTS `monitor_device`;
CREATE TABLE `monitor_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `electric_type` int(11) NULL DEFAULT NULL COMMENT '电能类型（1用电 2发电 3并网 4购电）',
  `channel_id` int(11) NULL DEFAULT NULL COMMENT '所属通道ID',
  `channel_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属通道编码',
  `com_device_id` int(11) NULL DEFAULT NULL COMMENT '所属通信设备ID',
  `com_device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属通信设备编码',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `sbdz` int(11) NULL DEFAULT NULL COMMENT '设备地址',
  `meter_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电表表号',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '设备分组',
  `is_pass` int(11) NULL DEFAULT NULL COMMENT '是否关口表',
  `pro_factory` int(11) NULL DEFAULT NULL COMMENT '生产厂家',
  `pro_type` int(11) NULL DEFAULT NULL COMMENT '设备类型',
  `pro_ver` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品版本',
  `buy_price` float NULL DEFAULT NULL COMMENT '购买价格',
  `pro_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品SN号',
  `pro_model` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品型号',
  `ag_standard` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安规国家',
  `rated_vol` float NULL DEFAULT NULL COMMENT '额定电压（KV）',
  `rated_ele` float NULL DEFAULT NULL COMMENT '额定电流',
  `rated_power` float NULL DEFAULT NULL COMMENT '额定功率',
  `ele_load_rate` float NULL DEFAULT NULL COMMENT '电流负载率',
  `is_analysis_energy` int(11) NULL DEFAULT NULL COMMENT '电能质量分析',
  `monitor_id` int(11) NULL DEFAULT NULL COMMENT '关联监控',
  `electric_attribute` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发电属性',
  `temperature_attribute` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '温度属性',
  `orther_attribute` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其它属性',
  `is_reading` int(11) NULL DEFAULT NULL COMMENT '是否抄表',
  `rated_volume` float NULL DEFAULT NULL COMMENT '组件容量',
  `azimuth` float NULL DEFAULT NULL COMMENT '方位角度',
  `dip_angle` float NULL DEFAULT NULL COMMENT '倾角角度',
  `manufacture_date` datetime(0) NULL DEFAULT NULL COMMENT '生产日期',
  `use_life` int(11) NULL DEFAULT NULL COMMENT '使用年限',
  `attenuation_rate` float NULL DEFAULT NULL COMMENT '性能衰减',
  `reading_var` int(11) NULL DEFAULT NULL COMMENT '抄表变量',
  `dtu_cmd` int(11) NULL DEFAULT NULL COMMENT 'DTU端口',
  `bind_user_id` int(11) NULL DEFAULT NULL COMMENT '绑定用户',
  `charging_type` int(11) NULL DEFAULT NULL COMMENT '付费模式',
  `rule_id` int(11) NULL DEFAULT NULL COMMENT '付费规则',
  `checkout_type` int(11) NULL DEFAULT NULL COMMENT '付费周期',
  `associated_devices` json NULL COMMENT '关联设备（JSON格式）',
  `online_time` datetime(0) NULL DEFAULT NULL COMMENT '上线时间',
  `offline_time` datetime(0) NULL DEFAULT NULL COMMENT '下线时间',
  `is_active` int(11) NULL DEFAULT NULL COMMENT '是否激活',
  `active_update_time` datetime(0) NULL DEFAULT NULL COMMENT '激活时间',
  `timeout` int(11) NULL DEFAULT 300 COMMENT '连接超时（s）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_sn_u`(`device_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '能源监控设备' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_device_association
-- ----------------------------
DROP TABLE IF EXISTS `monitor_device_association`;
CREATE TABLE `monitor_device_association`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备ID',
  `corre_device_id` int(11) NULL DEFAULT NULL COMMENT '关联设备ID',
  `corre_device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联设备名称',
  `corre_device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联设备编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '能源监控设备关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_device_day
-- ----------------------------
DROP TABLE IF EXISTS `monitor_device_day`;
CREATE TABLE `monitor_device_day`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备ID',
  `static_time` datetime(0) NULL DEFAULT NULL COMMENT '统计时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '设备状态',
  `electric_power` float NULL DEFAULT NULL COMMENT '发电功率',
  `electric_quantity` float NULL DEFAULT NULL COMMENT '当日发电量',
  `electric_hours` float NULL DEFAULT NULL COMMENT '当日满发电小时',
  `inverter_performance` int(11) NULL DEFAULT NULL COMMENT '逆变器性能',
  `dc_dispersion_rate` float NULL DEFAULT NULL COMMENT '直流离散率',
  `normalized_rate` float NULL DEFAULT NULL COMMENT '归一化率',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_device_time_u`(`device_id`, `static_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '监控设备日实时变量' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_device_var
-- ----------------------------
DROP TABLE IF EXISTS `monitor_device_var`;
CREATE TABLE `monitor_device_var`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `channel_id` int(11) NULL DEFAULT NULL COMMENT '所属通道ID',
  `channel_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属通道编码',
  `com_device_id` int(11) NULL DEFAULT NULL COMMENT '所属通信设备ID',
  `com_device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属通信设备编码',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '监控设备ID',
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '监控设备编码',
  `var_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量名称',
  `var_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量编码',
  `var_map_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引地图编码',
  `unit` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量单位',
  `is_monitor` int(11) NULL DEFAULT NULL COMMENT '是否监控',
  `var_type` int(11) NULL DEFAULT NULL COMMENT '变量类型（1模拟量 2状态量）',
  `variable_type` int(11) NULL DEFAULT NULL COMMENT '变量通用类型',
  `origin` int(11) NULL DEFAULT NULL COMMENT '数据来源（1IO型 2内存型）',
  `register_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据区域',
  `register_index` int(11) NULL DEFAULT NULL COMMENT '数据地址',
  `register_address` int(11) NULL DEFAULT NULL COMMENT '寄存器地址',
  `message_address` int(11) NULL DEFAULT NULL COMMENT '信息体地址',
  `data_address` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据标识',
  `rw` int(11) NULL DEFAULT NULL COMMENT '读写类型（1只读 2只写 3读写）',
  `compute_formula` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计算公式',
  `deletion_handle` int(11) NULL DEFAULT NULL COMMENT '缺失值处理（1不计算 2使用最近值 3使用0值 4使用1值）',
  `zero_compute` int(11) NULL DEFAULT 0 COMMENT '零值计算（0不计算 1计算）',
  `repair_data` int(11) NULL DEFAULT 0 COMMENT '补当前数据（0不补 1补）',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '数据类型',
  `init_value` float NULL DEFAULT NULL COMMENT '初始赋值',
  `base_value` float NULL DEFAULT NULL COMMENT '数据基值',
  `coefficient` float NULL DEFAULT NULL COMMENT '数据系数',
  `save_cycle` int(11) NULL DEFAULT NULL COMMENT '存盘周期（m）0不存盘',
  `data_fix` int(11) NULL DEFAULT 0 COMMENT '数据修正（0不操作 1操作）',
  `is_accumulation` int(11) NULL DEFAULT NULL COMMENT '是否累积量',
  `accumulation_type` int(11) NULL DEFAULT NULL COMMENT '累积类型（1小时累积 2天累积）',
  `is_data_filter` int(11) NULL DEFAULT NULL COMMENT '是否数据过滤（0否 1是）',
  `more_abs` float NULL DEFAULT NULL COMMENT '绝对值大于',
  `replace_value_upper` float NULL DEFAULT NULL COMMENT '替换值1',
  `less_abs` float NULL DEFAULT NULL COMMENT '绝对值小于',
  `replace_value_lower` float NULL DEFAULT NULL COMMENT '替换值2',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_sn_u`(`var_sn`) USING BTREE,
  INDEX `Index_device_sn`(`device_sn`) USING BTREE,
  INDEX `Index_com_device_sn`(`com_device_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '监控设备变量' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monitor_device_var_map
-- ----------------------------
DROP TABLE IF EXISTS `monitor_device_var_map`;
CREATE TABLE `monitor_device_var_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `parent_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级名称',
  `index_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引名称',
  `index_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引编码',
  `unit` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `var_type` int(11) NULL DEFAULT NULL COMMENT '变量类型（1模拟量 2状态量）',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 855 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '监控设备变量索引地图' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'blob类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日历信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'cron类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(20) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(20) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '已触发的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '暂停的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(20) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(20) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调度器状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(20) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(20) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(20) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'string类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'string类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'string类型的trigger的第三个参数',
  `int_prop_1` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(6) NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '触发器详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_templates
-- ----------------------------
DROP TABLE IF EXISTS `report_templates`;
CREATE TABLE `report_templates`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NOT NULL COMMENT '企业ID',
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏 3用水）',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '报表模版归属人',
  `report_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表名称',
  `report_type` tinyint(4) NULL DEFAULT 1 COMMENT '报表类型（1普通 2峰谷）',
  `visibility_type` tinyint(4) NULL DEFAULT 2 COMMENT '可见类型（1公有 2私有）',
  `multi_level_header` tinyint(4) NULL DEFAULT 1 COMMENT '多级表头 （1是 0否）',
  `head_config` json NULL COMMENT '表头配置',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报表描述',
  `system_flag` tinyint(4) NULL DEFAULT 1 COMMENT '系统默认（0系统 1自定义）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT 0 COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报表模版' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sharding_day
-- ----------------------------
DROP TABLE IF EXISTS `sharding_day`;
CREATE TABLE `sharding_day`  (
  `variable_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量名称',
  `save_time` datetime(0) NULL DEFAULT NULL COMMENT '存盘时间',
  `data_value` double(16, 4) NULL DEFAULT NULL COMMENT '数据',
  UNIQUE INDEX `Index_var_u`(`variable_name`, `save_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日数据存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sharding_month
-- ----------------------------
DROP TABLE IF EXISTS `sharding_month`;
CREATE TABLE `sharding_month`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `variable_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量名称',
  `day_sign` int(11) NULL DEFAULT NULL COMMENT '日期天',
  `min_value` double(16, 4) NULL DEFAULT NULL COMMENT '最小值',
  `min_time` datetime(0) NULL DEFAULT NULL COMMENT '最小值发生时间',
  `max_value` double(16, 4) NULL DEFAULT NULL COMMENT '最大值',
  `max_time` datetime(0) NULL DEFAULT NULL COMMENT '最大值发生时间',
  `avg_value` double(16, 4) NULL DEFAULT NULL COMMENT '平均值',
  `accu_sign` int(11) NULL DEFAULT NULL COMMENT '累积标记：1时累积 2天累积',
  `accu_value` double(16, 4) NULL DEFAULT NULL COMMENT '累积值',
  `runtime_value` double(16, 4) NULL DEFAULT NULL COMMENT '实时值',
  `total_count` int(11) NULL DEFAULT NULL COMMENT '总记录数',
  `save_time` datetime(0) NULL DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_variable_day`(`variable_name`, `day_sign`) USING BTREE,
  INDEX `Index_variable_name`(`variable_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '月统计数据存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sharding_month_accumulate
-- ----------------------------
DROP TABLE IF EXISTS `sharding_month_accumulate`;
CREATE TABLE `sharding_month_accumulate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `variable_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量名称',
  `save_time` datetime(0) NULL DEFAULT NULL COMMENT '存盘时间',
  `data_value` double(16, 4) NULL DEFAULT NULL COMMENT '上一个小时runtime值',
  `runtime_value` double(16, 4) NULL DEFAULT NULL COMMENT '当前数据',
  `record_year` int(11) NULL DEFAULT NULL COMMENT '年',
  `record_month` int(11) NULL DEFAULT NULL COMMENT '月',
  `record_day` int(11) NULL DEFAULT NULL COMMENT '日',
  `record_week` int(11) NULL DEFAULT NULL COMMENT '周',
  `record_hour` int(11) NULL DEFAULT NULL COMMENT '时',
  `accu_data` double(16, 4) NULL DEFAULT NULL COMMENT '累积数据',
  `seasonal_type_name` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '峰谷名称',
  `charge_price` double NULL DEFAULT NULL COMMENT '计费电价',
  `is_complete` int(11) NULL DEFAULT NULL COMMENT '完成标记',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_wy_u`(`variable_name`, `record_year`, `record_month`, `record_day`, `record_hour`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '变量累积数据月存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_address
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_address`;
CREATE TABLE `sys_common_address`  (
  `id` int(11) NOT NULL COMMENT '主键（省市镇县编码）',
  `parent_id` int(11) NOT NULL COMMENT '父级code',
  `level` int(11) NOT NULL COMMENT '等级：1省 2市 3县/县级市/区 4镇/街道 5村/社区',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区名称',
  `valid` smallint(6) NULL DEFAULT 1 COMMENT '状态：1启用 0已作废',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_idx1_parentid`(`parent_id`) USING BTREE COMMENT '父级ID索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '全国四级行政区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_api_apply
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_api_apply`;
CREATE TABLE `sys_common_api_apply`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `company_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入公司',
  `link_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人员',
  `link_mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `app_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `app_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐号',
  `app_secret` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥',
  `token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '令牌',
  `effect_minute` int(11) NULL DEFAULT NULL COMMENT '令牌有效期',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '第三方接入申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_api_interface
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_api_interface`;
CREATE TABLE `sys_common_api_interface`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `interface_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `interface_method` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口方法',
  `request_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `request_type` int(11) NULL DEFAULT NULL COMMENT '请求方式（1GET 2POST 3PUT 4PATCH 5DELETE 6HEAD 7OPTIONS）',
  `request_query` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_path_u`(`request_path`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_config`;
CREATE TABLE `sys_common_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `param_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数Key',
  `param_value` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `is_system` int(11) NULL DEFAULT NULL COMMENT '系统内置（0否 1是）',
  `param_info` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数说明',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_param_key_u`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_create
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_create`;
CREATE TABLE `sys_common_create`  (
  `create_table` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '生成表',
  `create_type` int(11) NULL DEFAULT NULL COMMENT '生成类型：1日表 2月表',
  PRIMARY KEY (`create_table`) USING BTREE,
  UNIQUE INDEX `Index_create_table`(`create_table`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '生成表记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_dict`;
CREATE TABLE `sys_common_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `dict_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '枚举名称',
  `dict_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_sn_u`(`dict_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通用字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_dict_data`;
CREATE TABLE `sys_common_dict_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `dict_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `dict_label` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典键值',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '字典排序',
  `icon_class` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标样式',
  `list_class` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` int(11) NULL DEFAULT NULL COMMENT '是否默认（1是 0否）',
  `is_multi_level` int(11) NULL DEFAULT NULL COMMENT '是否多级（1是 0否）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_sn_u`(`dict_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 434 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通用字典-数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_enterprise
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_enterprise`;
CREATE TABLE `sys_common_enterprise`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `ent_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业编码',
  `ent_type` int(11) NULL DEFAULT NULL COMMENT '企业类型',
  `ent_logo` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业Logo',
  `ent_intro` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业简介',
  `credit_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `legal_person` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业法人',
  `link_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `link_mobile` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `link_address` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `business_scope` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营范围',
  `registered_capital` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册资本',
  `found_date` datetime(0) NULL DEFAULT NULL COMMENT '成立日期',
  `open_start_date` datetime(0) NULL DEFAULT NULL COMMENT '营业期限开始',
  `open_end_date` datetime(0) NULL DEFAULT NULL COMMENT '营业期限结束',
  `address` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '住所',
  `reg_office` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登记机关',
  `Issue_date` datetime(0) NULL DEFAULT NULL COMMENT '发证日期',
  `logic_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_ent_sn_u`(`ent_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_menu`;
CREATE TABLE `sys_common_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `menu_model` int(11) NULL DEFAULT NULL COMMENT '菜单一级分类标识',
  `menu_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `menu_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `request_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `request_query` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `permission_sign` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `open_type` int(11) NULL DEFAULT NULL COMMENT '打开方式（0外链 1页签）',
  `is_cache` int(11) NULL DEFAULT NULL COMMENT '是否缓存（0缓存 1不缓存）',
  `icon` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `visible` int(11) NULL DEFAULT NULL COMMENT '菜单显示（0显示 1隐藏）',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `is_admin_visit` int(11) NULL DEFAULT NULL COMMENT '是否拥有admin权限才可访问',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1742 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '主菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_common_weather
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_weather`;
CREATE TABLE `sys_common_weather`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `country` int(11) NULL DEFAULT NULL COMMENT '国家',
  `province` int(11) NULL DEFAULT NULL COMMENT '省',
  `city` int(11) NULL DEFAULT NULL COMMENT '市',
  `county` int(11) NULL DEFAULT NULL COMMENT '县（区）',
  `town` int(11) NULL DEFAULT NULL COMMENT '镇（街道）',
  `village` int(11) NULL DEFAULT NULL COMMENT '村',
  `weather_date` datetime(0) NULL DEFAULT NULL COMMENT '天气日期',
  `weather_hour` int(11) NULL DEFAULT NULL COMMENT '天气时',
  `sunrise` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日出日落',
  `weather_base` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '天气状况',
  `air_quality` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '空气质量',
  `wind_direction` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风向',
  `wind_force` float NULL DEFAULT NULL COMMENT '风力',
  `wind_speed` float NULL DEFAULT NULL COMMENT '风速',
  `temperature` float NULL DEFAULT NULL COMMENT '气温',
  `sendible_temperature` float NULL DEFAULT NULL COMMENT '体感温度',
  `humidity` float NULL DEFAULT NULL COMMENT '湿度',
  `precipitation` float NULL DEFAULT NULL COMMENT '降水量',
  `light_radiation` float NULL DEFAULT NULL COMMENT '光辐射',
  `ultraviolet_rays` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '紫外线',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通用天气数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_company
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `company_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `company_name_en` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司英文名称',
  `logic_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '公司分组',
  `company_type` int(11) NULL DEFAULT NULL COMMENT '公司类型（1公司 2部门）',
  `industry_group_id` int(11) NULL DEFAULT NULL COMMENT '所属行业',
  `logo_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司Logo',
  `company_grade` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司评级ABCD',
  `company_scale` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司规模',
  `province` int(11) NULL DEFAULT NULL COMMENT '地址省',
  `city` int(11) NULL DEFAULT NULL COMMENT '地址市',
  `county` int(11) NULL DEFAULT NULL COMMENT '地址县',
  `address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '街道地址',
  `post_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `link_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人员',
  `link_mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `linkPhone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `link_fax` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真号码',
  `email` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱Email',
  `invoice_title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票名称',
  `invoice_tax` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票税号',
  `invoice_address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票地址',
  `invoice_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票电话',
  `invoice_back` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `invoice_account` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行帐号',
  `description` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司简介',
  `home_page` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司网址',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公司表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID（部门ID）',
  `origin_type` int(11) NULL DEFAULT NULL COMMENT '来源类型（1企业 2站点）',
  `corre_id` int(11) NULL DEFAULT NULL COMMENT '对应ID',
  `ancestors` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码',
  `is_can_select` int(11) NULL DEFAULT NULL COMMENT '是否可选择（0否 1是）',
  `dept_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `dept_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门编码',
  `leader` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_dept_sn_u`(`dept_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 144 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表（由企业表和站点表生成）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_enterprise_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_enterprise_menu`;
CREATE TABLE `sys_enterprise_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `menu_id` int(11) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业专属菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_files
-- ----------------------------
DROP TABLE IF EXISTS `sys_files`;
CREATE TABLE `sys_files`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据表名',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '记录ID',
  `service_type` int(11) NULL DEFAULT NULL COMMENT '文件服务提供商（0本地存储 1腾讯云 2阿里云）',
  `file_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_size` int(11) NULL DEFAULT NULL COMMENT '文件大小',
  `content_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `absolute_path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本地路径',
  `save_path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `bucket_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储桶',
  `only_key` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `storage_class` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储频率',
  `etag` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ETag',
  `web_url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问地址',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_table_record`(`table_name`, `record_id`) USING BTREE,
  INDEX `Index_etag`(`etag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上传文件管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_group`;
CREATE TABLE `sys_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `map_id` int(11) NULL DEFAULT NULL COMMENT '字典枚举ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `parent_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级名称',
  `group_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类别名称',
  `group_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类别代码',
  `logic_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码',
  `cascade_logic_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '级联逻辑代码（企业+分组）',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `is_show` int(11) NULL DEFAULT NULL COMMENT '是否显示（0否 1是）',
  `is_system` int(11) NULL DEFAULT NULL COMMENT '是否系统（0否 1是）',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `ext1` int(11) NULL DEFAULT NULL COMMENT '扩展字段1',
  `ext2` int(11) NULL DEFAULT NULL COMMENT '扩展字段2',
  `ext3` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段3',
  `ext4` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段4',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 323 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '常用分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `invoke_target` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `invoke_target` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用目标字符串',
  `job_message` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_leave_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_leave_message`;
CREATE TABLE `sys_leave_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据表名',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '记录ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '留言人ID',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言人',
  `reply_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言内容',
  `reply_time` datetime(0) NULL DEFAULT NULL COMMENT '留言日期',
  `reply_files` json NULL COMMENT '附件合集（JSON格式）',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_name_record`(`table_name`, `record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '留言回复管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log_execute
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_execute`;
CREATE TABLE `sys_log_execute`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务标题',
  `method` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行方法',
  `operate_type` int(11) NULL DEFAULT NULL COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `operate_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行人员',
  `operate_result` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行结果',
  `status` int(11) NULL DEFAULT NULL COMMENT '操作状态（0正常 1异常）',
  `execute_time` datetime(0) NULL DEFAULT NULL COMMENT '执行时间',
  `cost_time` int(11) NULL DEFAULT NULL COMMENT '消耗时间（秒）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_execute_time`(`execute_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '执行日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `template_type` int(11) NULL DEFAULT NULL COMMENT '消息类型（1短信 2微信 3小程序）',
  `topic` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息主题',
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码',
  `effect_minute` int(11) NULL DEFAULT NULL COMMENT '有效期',
  `return_result` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交信息',
  `sms_channel` int(11) NULL DEFAULT NULL COMMENT '短信渠道（1腾讯云 2阿里云）',
  `template_id` int(11) NULL DEFAULT NULL COMMENT '模板ID',
  `send_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人员',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `send_status` int(11) NULL DEFAULT NULL COMMENT '发送状态（-10已使用 0发送成功 1发送失败）',
  `send_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送内容',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_message_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_message_template`;
CREATE TABLE `sys_message_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `template_type` int(11) NULL DEFAULT NULL COMMENT '模板类型（1短信 2微信 3小程序）',
  `topic` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息主题',
  `topic_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题代码',
  `template_id` int(11) NULL DEFAULT NULL COMMENT '模板ID',
  `sms_channel` int(11) NULL DEFAULT NULL COMMENT '短信渠道（1腾讯云 2阿里云）',
  `sms_sign` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信签名',
  `template_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `notice_title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `notice_type` int(11) NULL DEFAULT NULL COMMENT '公告类型（1通知 2公告）',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '公告分组',
  `notice_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公告内容',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `post_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位名称',
  `post_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位代码',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `role_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限字符',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `menu_scope` int(11) NULL DEFAULT NULL COMMENT '菜单范围（1全部菜单 2仅浏览 3自定义菜单）',
  `data_scope` int(11) NULL DEFAULT NULL COMMENT '数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5仅本人数据权限）',
  `menu_check_strictly` int(11) NULL DEFAULT NULL COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` int(11) NULL DEFAULT NULL COMMENT '部门树选择项是否关联显示',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_u`(`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与组织关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(11) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_u`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5913 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_station
-- ----------------------------
DROP TABLE IF EXISTS `sys_station`;
CREATE TABLE `sys_station`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '分组ID',
  `station_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电站名称',
  `station_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电站编号',
  `group_or_station` int(11) NULL DEFAULT NULL COMMENT '记录类型（1分组 2站点）',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `pv_type` int(11) NULL DEFAULT NULL COMMENT '光伏类型',
  `logic_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码（企业+站点）',
  `build_site_time` datetime(0) NULL DEFAULT NULL COMMENT '建站时间',
  `voltage_level` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电压等级',
  `capacity_kva` int(11) NULL DEFAULT NULL COMMENT '最大容量',
  `device_limit` int(11) NULL DEFAULT 0 COMMENT '设备上限',
  `var_limit` int(11) NULL DEFAULT 0 COMMENT '测点上限',
  `sation_volume` int(11) NULL DEFAULT NULL COMMENT '装机容量',
  `azimuth` float NULL DEFAULT NULL COMMENT '方位角度',
  `dip_angle` float NULL DEFAULT NULL COMMENT '组件倾角',
  `use_site_time` datetime(0) NULL DEFAULT NULL COMMENT '投运时间',
  `coordinate` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电站坐标',
  `country` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` int(11) NULL DEFAULT NULL COMMENT '省',
  `city` int(11) NULL DEFAULT NULL COMMENT '市',
  `county` int(11) NULL DEFAULT NULL COMMENT '县（区）',
  `station_address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电站地址',
  `pic_url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电站照片',
  `description` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电站简介',
  `link_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人员',
  `link_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `open_alarm` int(11) NULL DEFAULT NULL COMMENT '报警开关',
  `scheme_id` int(11) NULL DEFAULT NULL COMMENT '电价标准',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_ent_sn_u`(`ent_id`, `station_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT 0 COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `logic_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码',
  `logon_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录帐号',
  `logon_pwd` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `nick_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `email` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `post_id` int(11) NULL DEFAULT NULL COMMENT '用户岗位',
  `is_supper` int(11) NULL DEFAULT NULL COMMENT '是否超管',
  `user_level` int(11) NULL DEFAULT NULL COMMENT '用户级别（1注册会员 2银卡会员 3金卡会员 4钻石会员 5Plus会员 10商户）',
  `sex` int(11) NULL DEFAULT NULL COMMENT '用户性别（0女 1男 2未知）',
  `marry` int(11) NULL DEFAULT NULL COMMENT '婚姻状态（0未婚 1已婚 2未知）',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '出生日期',
  `age` int(11) NULL DEFAULT NULL COMMENT '用户年龄',
  `true_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `card_type` int(11) NULL DEFAULT NULL COMMENT '证件类型',
  `card_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `card_pic_front` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证正',
  `card_pic_back` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证反',
  `is_auth` int(11) NULL DEFAULT NULL COMMENT '是否认证',
  `country` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` int(11) NULL DEFAULT NULL COMMENT '省',
  `city` int(11) NULL DEFAULT NULL COMMENT '市',
  `county` int(11) NULL DEFAULT NULL COMMENT '县（区）',
  `town` int(11) NULL DEFAULT NULL COMMENT '镇（街道）',
  `village` int(11) NULL DEFAULT NULL COMMENT '村',
  `detail_address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `post_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `fax` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真号码',
  `head_pic` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `person_des` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人简介',
  `reg_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `reg_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册IP',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `login_times` int(11) NULL DEFAULT NULL COMMENT '登录次数',
  `pwd_update_date` datetime(0) NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_logon_name_u`(`logon_name`) USING BTREE,
  UNIQUE INDEX `Index_nick_name_u`(`nick_name`) USING BTREE,
  UNIQUE INDEX `Index_mobile_u`(`mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_card
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_card`;
CREATE TABLE `sys_user_card`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT 0 COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `logic_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑代码',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `card_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `card_type` int(11) NULL DEFAULT NULL COMMENT '卡类型（1充值卡 2次卡）',
  `card_value` decimal(12, 4) NULL DEFAULT NULL COMMENT '充值卡余额',
  `cash_value` decimal(12, 4) NULL DEFAULT NULL COMMENT '可提现金额',
  `add_up_value` decimal(12, 4) NULL DEFAULT NULL COMMENT '累计消费金额',
  `valid_times` int(11) NULL DEFAULT NULL COMMENT '次卡有效次数',
  `surplus_times` int(11) NULL DEFAULT NULL COMMENT '次卡剩余次数',
  `give_times` int(11) NULL DEFAULT NULL COMMENT '次卡赠送次数',
  `add_up_times` int(11) NULL DEFAULT NULL COMMENT '累计使用次数',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期开始',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '有效期结束',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常1删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_user_id_u`(`user_id`) USING BTREE,
  UNIQUE INDEX `Index_card_no_u`(`card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户充值卡' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `post_id` int(11) NULL DEFAULT NULL COMMENT '岗位ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_u`(`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Index_u`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for webtopo_device_svgnode
-- ----------------------------
DROP TABLE IF EXISTS `webtopo_device_svgnode`;
CREATE TABLE `webtopo_device_svgnode`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `device_id` int(11) NOT NULL COMMENT '设备ID',
  `svg_node_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点ID',
  `svg_node_prop` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点属性',
  `device_prop` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备属性（变量）',
  `device_prop_type` int(11) NULL DEFAULT NULL COMMENT '变量类型（1模拟量 2状态量）',
  PRIMARY KEY (`id`, `project_id`, `device_id`, `svg_node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 199 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接线图设备节点关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for webtopo_project
-- ----------------------------
DROP TABLE IF EXISTS `webtopo_project`;
CREATE TABLE `webtopo_project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号ID',
  `ent_id` int(11) NULL DEFAULT NULL COMMENT '企业ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `station_type` int(11) NULL DEFAULT NULL COMMENT '电站类型（1配电 2光伏）',
  `project_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `thumbnail` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '缩略图',
  `data_model` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据模型',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `stop_flag` int(11) NULL DEFAULT NULL COMMENT '是否停用（0正常 1停用）',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0正常 1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接线图项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for webtopo_project_device
-- ----------------------------
DROP TABLE IF EXISTS `webtopo_project_device`;
CREATE TABLE `webtopo_project_device`  (
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备ID',
  UNIQUE INDEX `Index_pro_device`(`project_id`, `device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接线图项目设备关联表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
