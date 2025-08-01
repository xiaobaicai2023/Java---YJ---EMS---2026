/*
 Navicat Premium Dump SQL

 Source Server         : XMG-MySql
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : 112.31.119.29:9299
 Source Schema         : yunpower_config

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 30/07/2025 16:26:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 236 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    host: 127.0.0.1\n    port: 6379\n    password: redis_Px4beK\n    database: 7\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  servlet:\n    # 文件上传限制大小\n    multipart:\n      max-file-size: 20MB\n      max-request-size: 50MB\n\n# 开发环境配置\nserver:\n  # undertow 配置\n  undertow:\n    # HTTP post内容的最大大小。当值为-1时，默认值为大小是无限的\n    max-http-post-size: -1\n    # 以下的配置会影响buffer,这些buffer会用于服务器连接的IO操作,有点类似netty的池化内存管理\n    # 每块buffer的空间大小,越小的空间被利用越充分\n    buffer-size: 512\n    # 是否分配的直接内存\n    direct-buffers: true\n    threads:\n      # 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个线程\n      io: 8\n      # 阻塞任务线程池, 当执行类似servlet请求阻塞操作, undertow会从这个线程池中取得线程,它的值设置取决于系统的负载\n      worker: 256\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n      min-request-size: 8192\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'', '901df2c47bfd74c7392cebfb00d7e204', '2020-05-20 12:00:00', '2025-06-27 10:39:36', NULL, '14.110.95.234', '', '', '通用配置', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (212, 'yunpower-collect-biz-dev.yml', 'DEFAULT_GROUP', 'spring:\n  # 动态数据源配置\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginpassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        # 主库数据源\n        master:\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          url: jdbc:mysql://127.0.0.1:3306/yunpower_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n          username: root\n          password: mysql_bNzZ8E\n        # 数据采集\n        collect:\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          url: jdbc:mysql://127.0.0.1:3306/yunpower_collect?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n          username: root\n          password: mysql_bNzZ8E\n  # 分库分表配置（采集数据库）\n  shardingsphere:\n    datasource:\n      names: ds0\n      ds0:\n        type: com.zaxxer.hikari.HikariDataSource\n        driver-class-name: com.mysql.cj.jdbc.Driver\n        jdbc-url: jdbc:mysql://127.0.0.1:3306/yunpower_collect?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: mysql_bNzZ8E\n    rules:\n      sharding:\n        tables:\n          # *** 逻辑表名称（日存储数据表）***\n          sharding_day:\n            # 由数据源名 + 表名组成，以小数点分隔\n            actual-data-nodes: ds0.sharding_day,ds0.day_$->{20231018..20231020}\n            # 分库策略 databaseStrategy（可选）\n            # 分表策略\n            table-strategy:\n              # 用于单分片键的标准分片场景\n              standard:\n                # 分片列名称\n                sharding-column: save_time\n                # 分片算法名称（不能含有下划线）\n                sharding-algorithm-name: sharding-day-inline\n          # *** 逻辑表名称（月统计数据表）***\n          sharding_month:\n            actual-data-nodes: ds0.sharding_month,ds0.month_$->{202310..202312}\n            table-strategy:\n              standard:\n                sharding-column: min_time\n                sharding-algorithm-name: sharding-month-inline\n          # *** 逻辑表名称（月累计值统计数据表）***\n          sharding_month_accumulate:\n            actual-data-nodes: ds0.sharding_month_accumulate,ds0.month_accumulate_$->{202310..202312}\n            table-strategy:\n              standard:\n                sharding-column: save_time\n                sharding-algorithm-name: sharding-month-accumulate-inline\n        binding-tables:\n          sharding_day, sharding_month, sharding_month_accumulate\n        # 分片算法配置\n        sharding-algorithms:\n          # 上面配置的分片算法名称（sharding-day-inline）\n          sharding-day-inline:\n            # 分片算法类型（自定义分片策略）\n            type: CLASS_BASED\n            # 分片算法属性配置\n            props:\n              # 分片策略（标准分片）\n              strategy: standard\n              # 自定义类实现分片逻辑处理\n              algorithmClassName: com.yunpower.collect.storage.algorithm.DayShardingAlgorithm\n          sharding-month-inline:\n            type: CLASS_BASED\n            props:\n              strategy: standard\n              algorithmClassName: com.yunpower.collect.storage.algorithm.MonthShardingAlgorithm\n          sharding-month-accumulate-inline:\n            type: CLASS_BASED\n            props:\n              strategy: standard\n              algorithmClassName: com.yunpower.collect.storage.algorithm.MonthShardingAlgorithm\n    props:\n      # 是否显示SQL\n      sql-show: true\n\n  # RabbitMQ配置\n  rabbitmq:\n    host: mqhost\n    port: 5672\n    virtual-host: /yunpower\n    username: yunpower\n    password: P1o2w3er\n    # 设置MQ的连接超时时间\n    connection-timeout: 1s\n    # 关闭确认机制\n    publisher-confirm-type: none\n    publisher-returns: false\n    # 生产者\n    template:\n      retry:\n        # 开启超时重试机制\n        enabled: true\n        # 失败后的初始等待时间\n        initial-interval: 1000ms\n        # 失败后下次的等待时长倍数\n        multiplier: 2\n        # 最大重试次数\n        max-attempts: 3\n    # 消费者\n    listener:\n      simple:\n        prefetch: 1\n        # 确认机制\n        acknowledge-mode: auto\n        retry:\n          # 开启重试机制\n          enabled: true\n          # 失败后的初始等待时间\n          initial-interval: 1000ms\n          # 失败后下次的等待时长倍数\n          multiplier: 2\n          # 最大重试次数\n          max-attempts: 3\n          # 是否无状态：true无状态，false有状态\n          stateless: true\n\n  # 模板引擎\n  thymeleaf:\n    mode: HTML\n    encoding: utf-8\n    # 禁用缓存\n    cache: false\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n  # 文件上传\n  servlet:\n    multipart:\n      # 单个文件大小\n      max-file-size: 200MB\n      # 设置总上传的文件大小\n      max-request-size: 200MB\n\n#配置mybatis规则\nmybatis:\n  #搜索指定包别名\n  type-aliases-package: com.yunpower.collect.storage.domain\n  #加载全局的配置文件\n  config-location: classpath:mybatis/mybatis-config.xml\n  #配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapper-locations: classpath:mybatis/mapper/**/*Mapper.xml\n\n#防止XSS攻击\nxss:\n  # 过滤开关\n  enabled: true\n\n#日志配置\nlogging:\n  level:\n    com.yunpower: debug\n    org.springframework: warn\n    \n\n#项目相关配置\nhunttown:\n  # 名称\n  name: yunpowerCollect\n  # 版本\n  version: 1.0.1\n  # 版权年份\n  copyrightYear: 2023\n  # 实例演示开关\n  demoEnabled: false\n  # 文件路径 示例（Windows配置 D:/yunpower/uploadPath；Linux配置 /home/yunpower/uploadPath）\n  profile: /home/yunpower/uploadPath\n  # 获取ip地址开关\n  addressEnabled: false\n\n# Actuator监控暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: always\n\nyunpower:\n  collect:\n    scheduled:\n      corePoolSize: 128\n    protocols:\n      iec104:\n        address: 127.0.0.1\n        port: 2404\n      modbus:\n        address: 127.0.0.1\n        port: 502\n      websocket:\n        address: 0.0.0.0\n        port: 9501\n\n# 业务变量配置\napp:\n  database:\n    # 采集数据库配置（用于动态更新 shardingsphere 节点配置）\n    collect:\n      db0:\n        name: yunpower_collect\n        alias: ds0\n    # 数据库创建日期（填写当月1日）\n    create-date: 2023-10-01', '21dcd0a466aead070ca774a74d501895', '2024-06-27 09:50:13', '2025-06-27 10:42:27', NULL, '14.110.95.234', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (219, 'yunpower-gateway-dev.yml', 'DEFAULT_GROUP', 'spring:\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: yunpower-auth\n          uri: lb://yunpower-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 系统模块\n        - id: yunpower-system\n          uri: lb://yunpower-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: yunpower-file\n          uri: lb://yunpower-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: yunpower-job\n          uri: lb://yunpower-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 数据统计\n        - id: yunpower-datav\n          uri: lb://yunpower-datav\n          predicates:\n            - Path=/datav/**\n          filters:\n            - StripPrefix=1\n      # 跨域配置\n      globalcors:\n        corsConfigurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /favicon.ico\n\n# 测试路由\ntest:\n  username: testing', '2cbb95a736a8472306631b7f60795a67', '2024-06-29 07:47:54', '2024-09-04 06:40:17', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (220, 'yunpower-auth-dev.yml', 'DEFAULT_GROUP', '# 业务变量配置\napp:\n  auth:\n    login:\n      # 是否启用单浏览器登录限制\n      single-browser: true', '0d97733f2b931c58ee24a0d0974d9fdd', '2024-06-29 07:48:34', '2024-09-04 06:02:52', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (221, 'yunpower-monitor-dev.yml', 'DEFAULT_GROUP', '# spring\r\nspring:\r\n  security:\r\n    user:\r\n      name: admin\r\n      password: 123456\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: 云捷服务监控', '7ecf4401f8911793c3eea0e64c7d09a6', '2024-06-29 07:50:43', '2024-06-29 07:50:43', 'nacos', '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (222, 'yunpower-system-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  # 动态数据源配置\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginpassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        # 主库数据源\n        master:\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          url: jdbc:mysql://127.0.0.1:3306/yunpower_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n          username: root\n          password: mysql_bNzZ8E\n        # 日志数据源\n        logdb:\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          url: jdbc:mysql://127.0.0.1:3306/yunpower_log?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n          username: root\n          password: mysql_bNzZ8E\n    props:\n      # 是否显示SQL\n      sql-show: false\n  jackson:\n    deserialization:\n      # 是否接受空字符串为null\n      accept-empty-string-as-null-object: true\n\n# 业务变量配置\napp:\n  database:\n    # 采集数据库配置（用于动态更新 shardingsphere 节点配置）\n    collect:\n      db0:\n        name: yunpower_collect\n        alias: ds0\n    # 数据库创建日期（填写当月1日）\n    create-date: 2023-10-01\n  upload:\n    file-domain: http://127.0.0.1:9300\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.yunpower.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# knife4j\nknife4j:\n  # 是否启用增强版功能\n  enable: true\n  # 如果是生产环境，将此设置为 true，然后就能够禁用了 knife4j 的页面\n  production: false\n\n# swagger\nswagger:\n  title: 主系统接口\n  license: Powered by yunpower\n  licenseUrl: http://www.yunpower.cn\n\n# 日志级别\nlogging:\n  level:\n    com.yunpower.system.*: debug', '1b46868472a0a64b3ff8648e8a4243b5', '2024-06-29 07:55:10', '2025-06-27 10:43:56', NULL, '14.110.95.234', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (223, 'yunpower-job-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://127.0.0.1:3306/yunpower_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: mysql_bNzZ8E\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.yunpower.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# knife4j\nknife4j:\n  # 是否启用增强版功能\n  enable: true\n  # 如果是生产环境，将此设置为 true，然后就能够禁用了 knife4j 的页面\n  production: false\n\n# swagger\nswagger:\n  title: 定时任务接口\n  license: Powered by yunpower\n  licenseUrl: http://www.yunpower.cn', '88b5a77a32de5133c57b1419068881d4', '2024-06-29 07:57:45', '2025-06-27 10:44:19', NULL, '14.110.95.234', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (225, 'yunpower-file-dev.yml', 'DEFAULT_GROUP', '# 本地文件上传    \nfile:\n  domain: http://127.0.0.1:9300\n  path: /home/yunpower/uploadPath\n  prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://127.0.0.1\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 127.0.0.1:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:19000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test\n\n# knife4j\nknife4j:\n  # 是否启用增强版功能\n  enable: true\n  # 如果是生产环境，将此设置为 true，然后就能够禁用了 knife4j 的页面\n  production: false\n\n# swagger\nswagger:\n  title: 文件上传接口\n  license: Powered by yunpower\n  licenseUrl: http://www.yunpower.cn', '04fb2ee34b8ef5c54d8a5cda211b6214', '2024-06-29 07:59:43', '2025-06-27 10:44:52', NULL, '14.110.95.234', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (226, 'yunpower-datav-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  # 动态数据源配置\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginpassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        # 主库数据源\n        master:\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          url: jdbc:mysql://127.0.0.1:3306/yunpower_cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n          username: root\n          password: mysql_bNzZ8E\n        # 日志数据源\n        logdb:\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          url: jdbc:mysql://127.0.0.1:3306/yunpower_log?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n          username: root\n          password: mysql_bNzZ8E\n\n  # 分库分表配置（采集数据库）\n  shardingsphere:\n    datasource:\n      names: ds0\n      ds0:\n        type: com.zaxxer.hikari.HikariDataSource\n        driver-class-name: com.mysql.cj.jdbc.Driver\n        jdbc-url: jdbc:mysql://127.0.0.1:3306/yunpower_collect?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: mysql_bNzZ8E\n    rules:\n      sharding:\n        tables:\n          # *** 逻辑表名称（日存储数据表）***\n          sharding_day:\n            # 由数据源名 + 表名组成，以小数点分隔\n            actual-data-nodes: ds0.sharding_day,ds0.day_$->{20231018..20231020}\n            # 分库策略 databaseStrategy（可选）\n            # 分表策略\n            table-strategy:\n              # 用于单分片键的标准分片场景\n              standard:\n                # 分片列名称\n                sharding-column: save_time\n                # 分片算法名称（不能含有下划线）\n                sharding-algorithm-name: sharding-day-inline\n          # *** 逻辑表名称（月统计数据表）***\n          sharding_month:\n            actual-data-nodes: ds0.sharding_month,ds0.month_$->{202310..202312}\n            table-strategy:\n              standard:\n                sharding-column: min_time\n                sharding-algorithm-name: sharding-month-inline\n          # *** 逻辑表名称（月累计值统计数据表）***\n          sharding_month_accumulate:\n            actual-data-nodes: ds0.sharding_month_accumulate,ds0.month_accumulate_$->{202310..202312}\n            table-strategy:\n              standard:\n                sharding-column: save_time\n                sharding-algorithm-name: sharding-month-accumulate-inline\n        binding-tables:\n          sharding_day, sharding_month, sharding_month_accumulate\n        # 分片算法配置\n        sharding-algorithms:\n          # 上面配置的分片算法名称（sharding-day-inline）\n          sharding-day-inline:\n            # 分片算法类型（自定义分片策略）\n            type: CLASS_BASED\n            # 分片算法属性配置\n            props:\n              # 分片策略（标准分片）\n              strategy: standard\n              # 自定义类实现分片逻辑处理\n              algorithmClassName: com.yunpower.datav.algorithm.DayShardingAlgorithm\n          sharding-month-inline:\n            type: CLASS_BASED\n            props:\n              strategy: standard\n              algorithmClassName: com.yunpower.datav.algorithm.MonthShardingAlgorithm\n          sharding-month-accumulate-inline:\n            type: CLASS_BASED\n            props:\n              strategy: standard\n              algorithmClassName: com.yunpower.datav.algorithm.MonthShardingAlgorithm\n    props:\n      # 是否显示SQL\n      sql-show: true\n  jackson:\n    deserialization:\n      # 是否接受空字符串为null\n      accept-empty-string-as-null-object: true\n    serialization:\n      write-bigdecimal-as-plain: true\n\n# 业务变量配置\napp:\n  database:\n    # 采集数据库配置（用于动态更新 shardingsphere 节点配置）\n    collect:\n      db0:\n        name: ykite_collect\n        alias: ds0\n    # 数据库创建日期（填写当月1日）\n    create-date: 2023-10-01\n  upload:\n    file-domain: http://127.0.0.1:9300\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.yunpower.datav\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# knife4j\nknife4j:\n  # 是否启用增强版功能\n  enable: true\n  # 如果是生产环境，将此设置为 true，然后就能够禁用了 knife4j 的页面\n  production: false\n\n# swagger\nswagger:\n  title: 数据服务接口\n  license: Powered by yunpower\n  licenseUrl: http://www.yunpower.cn\n\n# 日志级别\nlogging:\n  level:\n    com.yunpower.datav.*: debug', '9c9cd3be5c83496c914d6886502095f2', '2024-06-29 08:09:00', '2025-06-27 10:45:30', NULL, '14.110.95.234', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (228, 'sentinel-yunpower-gateway', 'DEFAULT_GROUP', '[\r\n    {\r\n        \"resource\": \"yunpower-auth\",\r\n        \"count\": 500,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"yunpower-system\",\r\n        \"count\": 1000,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"yunpower-datav\",\r\n        \"count\": 1000,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"yunpower-job\",\r\n        \"count\": 300,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    }\r\n]', 'fe778433ae0db4533c7cd4be49f34066', '2024-06-29 08:16:05', '2024-06-29 08:16:05', 'nacos', '127.0.0.1', '', '', NULL, NULL, NULL, 'json', NULL, '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$v7Hi54M.yUL28VDdycZZtO3dFrEkj7Fxslk.sN1lMPZ8Y8o8vSnkq', 1);

SET FOREIGN_KEY_CHECKS = 1;
