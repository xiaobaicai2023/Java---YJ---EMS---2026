<div align="center">
  <img src="/logo.png" alt="logo" weith="80px"  height = "80px" >
</div>

<div align="center" >
  <p height="50px"><strong>云捷EMS开源能源管理系统</strong></p>
</div>

<div align="center">
  <p height="30px">基于 Vue3 / Spring Boot/Spring Cloud & Alibaba 微服务架构</p>
</div>
<div align="center">

  项目技术框架 RuoYi-Cloud 基础框架上开发而成

</div>
<div align="center">
  代码注释在40%以上
</div>
<div align="center">
本项目采用 MIT License 开源协议
</div>

<div align="center">
    <p height="60px">
   <a href='https://gitee.com/guangdong122/energy-management/stargazers'><img src='https://gitee.com/guangdong122/energy-management/badge/star.svg?theme=dark' alt='star'></img></a>
    <a href='https://gitee.com/guangdong122/energy-management/members'><img src='https://gitee.com/guangdong122/energy-management/badge/fork.svg?theme=dark' alt='fork'></img></a>
    </p>
</div>

#### 一、项目介绍

    云捷EMS开源能源管理系统， 分为物联网硬件网关部分、数据采集部分、数据应用、数据大屏四大模块，

    适用于生产制造类企业、工厂、园区、写字楼等高能耗企业的能源管理场景，
    
    是一套完整的全链路的能源数据采集、上传、存储、计算、分析、展示的EMS系统

    支持高耗能企业，水、电、汽、热、油、空压机、中央空调、光伏逆变器、机房、楼层配电箱、电力等数据的采集和分析


#### 二、主要功能
     
    硬件网关部分：支持包括 Modbus、IEC101、102、103、104、61850、DL/T645、MQTT、OPC等50余种协议的采集和解析；

    数据采集部分：包括电表、水表、热用表、光伏、逆变器、空压机、配电箱等数据采集、解析、存储等功能；

    数据服务部分：包括前端、后端、实时数据、历史数据、各维度能耗分析、报警、运维、工单、派单、一次线图等功能；

    数据大屏部分：包括数据看板、自定义报表配置、可视化大屏配置、界面可视化配置等

  ` <div>
      <img src="/test/niaokantu.png" alt="logo" >
   </div>`

#### 三、技术栈


    1.前端采用Arco Design Pro（ VUE3 版本）
    
    2.后端采用Spring Boot、Spring Cloud & Alibaba
    
    3.注册中心、配置中心选型Nacos=2.2.2，权限认证使用Redis
    
    4.流量控制框架选型Sentinel，分布式事务选型Seata
    
    5.数据库采用MYSQL >= 5 .7, JDK1.8, Maven >= 3.0

#### 四、数据采集-通讯模块 - MQTT

    方式一 ：MQTT格式消息

    硬件设备通过智能电表或者硬件网关上传，大部分为MQTT形式定时上报，采集系统订阅后对上报数据进行处理。

    下图是某硬件网关上报的mqtt报文，由后台进行数据处理。不同的设备格式参数不同，可能需要进行针对性解析。

    【后台可视化配置好参数后，直接可订阅硬件网关上传的数据，基本上不需要开发】
    
   ` <div>
      <img src="/MQTT.png" alt="logo"  weith="400px"  height = "400px" >
   </div>`


#### 五、数据采集-通讯模块 - websocket

    方式二  websocket订阅模式

    websocket订阅模式，系统定时下行指令，设备上报后进入消息队列rabbitmq，由采集服务分布式处理。

  `<div>
      <img src="/websocket.png" alt="logo"  weith="400px"  height = "400px" >
   </div>

 <div>
      <img src="/websocket1.png" alt="logo"  weith="400px"  height = "400px" >
 </div>

 <div>
      <img src="/websocket2.png" alt="logo"  weith="400px"  height = "400px" >
 </div>`
 

#### 六、硬件网关数据转发

【配置路由】

 ` <div>
      <img src="/01.png" alt="logo" >
   </div>`

【配置转发方式：是MQTT 还是 TCP/IP socket  还是 其他方式】

 ` <div>
      <img src="/02.png" alt="logo" >
   </div>`

【配置转发的点位（变量）：如电压参数、电流参数、Ua、Ub等】

 ` <div>
      <img src="/03.png" alt="logo" >
   </div>`

#### 七、数据计算引擎

   1、实时数据，上传至时序库或者redis数据库，以被实时调用；

   2、业务数据，系统会自动计算，按照日月年的数据格式存放至业务库，以便系统进行调用，避免调用时临时计算降低效率

#### 八、EMS界面展示
 
 ` 
` <div>
      <img src="/test/201.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/202.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/203.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/204.png" alt="logo" >
   </div>`
` 
` <div>
      <img src="/test/205.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/svg.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/009.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/206.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/011.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/014.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/015.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/016.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/017.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/018.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/020.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/021.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/207.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/023.png" alt="logo" >
   </div>`

` <div>
      <img src="/test/025.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/026.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/027.png" alt="logo" >
   </div>`


#### 九、数据看板

    **【数据看板支持拖拉拽，选择展示方式，完全可视化配置】** 

` <div>
      <img src="/test/201.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/0002.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/0003.png" alt="logo" >
   </div>`

  

#### 十、智能报表

   **【告别复杂报表，全部可以创建报表模板，关联数据，一键可视化配置】** 

` <div>
      <img src="/test/012.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/013.png" alt="logo" >
   </div>`

#### 十一、智能运维

   **【报警-工单-任务，一气呵成】** 

` <div>
      <img src="/test/019.png" alt="logo" >
   </div>`

#### 十二、数据大屏

  ** [大屏支持可视化配置，创建卡片，编辑卡片信息，选择卡片位置，关联数据，即可完成大屏配置]** 

` <div>
      <img src="/test/024.png" alt="logo" >
   </div>`
` <div>
      <img src="/test/028.png" alt="logo" >
   </div>`

#### 十三、代码结构
        
        com.yunpower     
        ├── yunpower-ui                         // 前端UI界面
        ├── yunpower-diagram-ui                 // 接线图UI界面
        ├── yunpower-gateway                    // 网关模块 [8080]
        ├── yunpower-auth                       // 认证中心 [9200]
        ├── yunpower-api                        // 接口模块
        │       └── yunpower-api-system         // 系统接口
        ├── yunpower-common                     // 通用模块
        │       └── yunpower-common-core        // 核心模块
        │       └── yunpower-common-datascope   // 权限范围
        │       └── yunpower-common-datasource  // 多数据源
        │       └── yunpower-common-log         // 日志记录
        │       └── yunpower-common-redis       // 缓存服务
        │       └── yunpower-common-seata       // 分布式事务
        │       └── yunpower-common-security    // 安全模块
        │       └── yunpower-common-sensitive   // 数据脱敏
        │       └── yunpower-common-swagger     // 系统接口
        ├── yunpower-modules                    // 业务模块
        │       └── yunpower-system             // 系统模块 [9201]
        │       └── yunpower-job                // 定时任务 [9203]
        │       └── yunpower-datav              // 数据服务 [9204]
        │       └── yunpower-file               // 文件服务 [9300]
        ├── yunpower-visual                     // 图形化管理模块
        │       └── yunpower-visual-monitor     // 监控中心 [9100]
        ├──pom.xml                            // 公共依赖


#### 十四、软件部署方案
 
Ubuntu 22.04为例

Mysql和redis安装过程略。

1，更新系统

sudo apt update
sudo apt upgrade

遇到选Y/N的，选y

2，安装jdk1.8

sudo apt install openjdk-8-jdk

3，安装maven 

先执行  sudo apt update   更新
然后    sudo apt install maven
安装完成后使用 mvn -v查看是否安装成功

4，安装minio


请参考这个链接
https://blog.csdn.net/weixin_53510183/article/details/143511235

5，安装rabbitMQ

https://www.rabbitmq.com/docs/install-debian#apt-quick-start-cloudsmith

选择合适的系统版本，复制快速开始脚本到系统中

6，安装node.js

curl -sL https://deb.nodesource.com/setup_18.x | sudo bash -

安装node
sudo apt-get install -y nodejs

7，导入数据库

将sql文件导入数据库，如果报utf8mb4_0900_ai_ci 错误，则全局替换utf8mb4_0900_ai_ci 为utf8_general_ci，同时将utf8mb4替换为utf8。

8，启动nacos

cd到nacos的bin目录下面
bash startup.sh -m standalone

9，配置host

11，启动服务

12，此时访问http://127.0.0.1:8888即可进入系统。



#### 十五、演示地址


  

>   演示地址1

>    http://ems.qd-xiaomage.com:8888/

>    账号：test3

>    密码：12345



>   演示地址2

>    http://ems.qd-xiaomage.com:8888/

>    账号：test4

>    密码：test12345


>   演示地址3

>    http://ems.qd-weimob.com:8888/

>    账号：test5

>    密码：test12345




#### 十六、关于我们

    开源代码，仅供学习和研究使用，如需商务合作请联系我们；

 ` <div>
      <img src="/quan.png" alt="logo" >
   </div>`

    

#### 十七、免责声明

    本软件为开源项目，文档中涉及的硬件、网管、通讯、数据采集、MQTT、websorchet、

    计算引擎、若依、三方Jar包、三方工具包、三方插件包等

    原则上可以直接使用，请笔者在使用的同时及时关注三方更新做必要的技术更新。

#### 十八、其他

 1、视频录制教程，正在更新中。。。。。

 2、每月会有持续的版本迭代和升级。。。

 3、碳足迹、碳交易和碳资产管理，正在同步升级开发中。。。
