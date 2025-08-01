### 交换机和队列说明

vhost = /yunpower

##### 一、采集数据
1. 交换机：yunpower.direct
2. 队列
* 数据队列：queue.collect  
数据格式：{"connectKey":"", "time":"", "address":"", "value":""}  
需要几个队列？= address%N （routingKey=address%N）
