<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">云捷EMS数据采集系统</h1>
<h4 align="center">基于 Spring Boot/Spring Cloud & Alibaba 微服务架构</h4>


## 平台简介

云捷EMS数据采集系统，接收通讯设备上传的数据，解析并保存到数据库，目前支持IEC104协议。


## 框架说明
1. 框架采用Spring Boot、Spring Cloud & Alibaba</br>
2. 注册中心、配置中心选型Nacos2.2，权限认证使用Redis</br>
3. 流量控制框架选型Sentinel，分布式事务选型Seata</br>
4. 数据库采用MYSQL >= 5 .7, JDK1.8, Maven >= 3.0
5. RabbitMQ

## 云捷EMS系统分为两部分：
* （1）数据服务部分：包括前端、后端、一次线图等功能  
* （2）数据采集部分：包括采集、解析、存储等功能  

## 系统模块

~~~
com.yunpower     
├─yunpower-collect-biz            // 启动项 [9500]
├─yunpower-collect-mq             // 消息队列
│  ├─yunpower-collect-mq-consumer // 消息消费者
│  └─yunpower-collect-mq-producer // 消息生产者
├─yunpower-collect-protocols      // 协议相关
│  ├─IEC104                     // 104协议 [2404]
│  ├─Modbus
│  ├─MQTT
│  └─WebSocket                  // 提供实时数据
├─yunpower-collect-storage        // 数据存储
├─yunpower-common
│  ├─yunpower-common-core         // 核心模块
│  ├─yunpower-common-dds          // 多数据源
│  └─yunpower-common-redis        // 缓存服务
~~~

## 功能说明

1. 协议解析	目前支持IEC104协议
2. 实时数据	采用 WebSocket
3. 设备状态	WebSocket 配合 Redis 实现
4. 报警功能	采用RabbitMQ

## 更新说明
1. 2024-09-05 更新有内容：
* 增加消息队列模块：数据采集通过MQ实现，防止数据积压
* 增加报警记录推送
* 增加设备、通讯状态推送
* 增加内存型变量计算
* 优化数据计算逻辑