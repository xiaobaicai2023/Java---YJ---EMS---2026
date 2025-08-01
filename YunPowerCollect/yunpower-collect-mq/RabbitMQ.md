### RabbitMQ基础知识

##### 一. 数据隔离
1. user（用户）
2. virtual host（vhost）

##### 二. 队列
1. 生产者可直接发送消息到队列（不指定交换机）
2. 如果一个队列绑定了N个消费者，默认情况下这个队列的数据由N个消费者平分  
这样会产生一个问题：各个消费者的消费速度不一样，导致消费慢的会产生消息堆积  
可通此配置解决：spring.rabbitmq.listener.simple.prefetch=1
3. 什么情况下一个队列要绑定多个消费者？
消息太多，需要多个消费者消费，这时分步式部署的时候就会出现这种情况

##### 三. 交换机
1. **交换机类型（路由功能） ** 
* direct：定向：将消息路由到`指定`队列（通过routingKey -> bindingKey）  
* topic： 主题：功能类似direct，区别是routingKey可以是多个单词列表，并以`.`分隔，同是可以通过`*`和`#`进行模糊匹配。  
  `*`：代表一个单词  
  `#`：代表0个或多个  
* fanout：广播：会把消息广播到`每一个`和它绑定的队列中  

2. **交换机的作用是什么？**
将消息按规则路由到每个绑定的队列中去

3. **声明和绑定**

（1）配置代码中绑定
```java
@Bean
public DirectExchange directExchange(){
    return new DirectExchange("hmall.direct");
}

@Bean
public Queue directQueue1(){
    return new Queue("direct.queue1");
}

@Bean
public Binding directQueue1BindingRed(Queue directQueue1, DirectExchange directExchange){
    return BindingBuilder.bind(directQueue1).to(directExchange).with("red");
}
```
（2）注解绑定
```java
@RabbitListener(bindings = @QueueBinding(
        value = Queue(name ="direct.queue1",durable = "true"),
        exchange = @Exchange(name ="hmall,direct",type = ExchangeTypes.DIRECT),
        key = {"red","blue"}
))
public void listenDirectQueuel(String msg)throws InterruptedException{
    System.out.println("消费者1 收到了 direct.queue1的消息:【" + msg + "】");
}
```

##### 四. 消息转换器
通俗来说就是把一个object对象转换成文本
1. **默认的转换器**：StringMessageConverter（不推荐）
2. **引入消息转换器**：jackson-dataformat-xml
```xml
<!--Jackson-->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

### RabbitMQ进阶知识
##### 一. 生产者可靠性
1. **生产者重连**  
默认是阻塞式的，当MQ不可用时，会一直阻塞，直到可用为止（会影响业务性能）
```yaml
spring:
  rabbitmq:
    connection-timeout: 1s # 设置MQ的连接超时时间
    template:
      retry:
        enabled: true      # 开启超时重试机制
        initial-interval: 1000ms # 失败后的初始等待时间
        multiplier: 1      # 失败后下次的等待时长倍数，下次等待时长 = initial-interval * multiplier
        max-attempts: 3    # 最大重试次数
```

2. **生产者确认**
```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated # 开启publisher confirm机制，并设置confirm类型
    publisher-returns: true # 开启publisher return机制（一般情况下不需要开启）
```
这里`publisher-confirm-type`有三种模式可选：
* `none`：关闭confirm机制
* `simple`：同步阻塞等待MQ的回执
* `correlated`：MQ异步回调返回回执

说明：开启生产者确认后，会大幅度降低MQ的发送效率。可以自己做个实验，发送100万条消息观察一下。

3. **数据持久化**

（1）数据持久化  
默认情况下MQ的数据都是在内存存储的临时数据，重启后就会消失。为了保证数据的可靠性，必须配置数据持久化

* 交换机持久化 -> Durability: durable
* 队列持久化   -> Durability: durable
* 消息持久化   -> DeliveryMode: Persistent

（2）Lazy Queue （惰性队列）
* MQ接收到消息后直接存入磁盘（非内存）， 消费者要消费消息时才会从磁盘中读取并加载到内存（也就是懒加载） 可支持数百万条的消息存储；
* 3.12版本之后，LazyQueue已经成为所有队列的默认格式。

##### 二. 消费者可靠性

1. **消费者确认配置**
```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: none # 不做处理
```
有三种模式：
* `none`：不处理
* `manual`：手动处理
* `auto`：自动处理

2. **失败重试机制**
```yaml
spring:
  rabbitmq:
    listener:
      simple:
        retry:
          enabled: true   # 开启消费者失败重试
          initial-interval: 1000ms # 初识的失败等待时长为1秒
          multiplier: 1   # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
          max-attempts: 3 # 最大重试次数
          stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
```

3. **业务幂等性**
有些数据的更新往往不是幂等的，如果重复执行可能造成不一样的后果。比如：

* 取消订单，恢复库存的业务。如果多次恢复就会出现库存重复增加的情况
* 退款业务。重复退款对商家而言会有经济损失。

所以，我们要尽可能避免业务被重复执行。 **在业务中**，我们可以使用分布式锁来保证幂等性；或者令牌机制来保证幂等性。
但是在**MQ**中，我们可以使用消息的`唯一消息ID`来保证幂等性；或者**判断业务状态**来保证幂等性。

（1）唯一消息ID  
SpringAMQP的MessageConverter自带了MessageID的功能，我们只要开启这个功能即可。
以Jackson的消息转换器为例：
```java
@Bean
public MessageConverter messageConverter(){
    // 1.定义消息转换器
    Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
    // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
    jjmc.setCreateMessageIds(true);
    return jjmc;
}
```

##### 三. 延迟消息

购物：假如用户下单后一直不付款，就会一直占有库存资源，导致其他客户无法正常交易，最终导致商户利益受损！

因此，电商中通常的做法就是：**对于超过一定时间未支付的订单，应该立刻取消订单并释放占用的库存**。

例如，订单支付超时时间为30分钟，则我们应该在用户下单后的第30分钟检查订单支付状态，如果发现未支付，应该立刻取消订单，释放库存。

但问题来了：如何才能准确的实现在下单后第30分钟去检查支付状态呢？

像这种在一段时间以后才执行的任务，我们称之为**延迟任务**，而要实现延迟任务，最简单的方案就是利用MQ的延迟消息了。

在RabbitMQ中实现延迟消息有两种方案：

* 死信交换机+TTL
* 延迟消息插件

1. **死信交换机+TTL**  
当一个队列中的消息满足下列情况之一时，可以成为死信（dead letter）：

* 消费者使用`basic.reject`或 `basic.nack`声明消费失败，并且消息的`requeue`参数设置为false
* 消息是一个过期消息，超时无人消费
* 要投递的队列消息满了，无法投递

利用**死信交换机+TTL**，我们可以实现延迟消息的功能。  
（1）生产者发送一条延迟消息到队列QueueA，并设置消息的TTL为30秒；  
（2）QueueA 不设置消费者，但将其绑定到死信交换机中；  
（3）30秒后无人消费，MQ会自动将消息投递到死信交换机中；  
（4）同时给死信交换机绑定一个队列，并设置消费者。  

2. **延迟消息插件**  
插件的原理是设计了一种支持延迟消息功能的交换机当消息投递到交换机后可以暂存一定时间，到期后再投递到队列。

* DelayExchange插件下载地址：https://github.com/rabbitmq/rabbitmq-delayed-message-exchange

* 插件配置：
```java
@Configuration
public class DelayExchangeConfig {

    @Bean
    public DirectExchange delayExchange(){
        return ExchangeBuilder
                .directExchange("delay.direct") // 指定交换机类型和名称
                .delayed()     // 设置delay的属性为true
                .durable(true) // 持久化
                .build();
    }

    @Bean
    public Queue delayedQueue(){
        return new Queue("delay.queue");
    }
    
    @Bean
    public Binding delayQueueBinding(){
        return BindingBuilder.bind(delayedQueue()).to(delayExchange()).with("delay");
    }
}
```

* 插件使用：
```java
@Test
void testPublisherDelayMessage() {
    // 1.创建消息
    String message = "hello, delayed message";
    // 2.发送消息，利用消息后置处理器添加消息头
    rabbitTemplate.convertAndSend("delay.direct", "delay", message, new MessagePostProcessor() {
        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            // 添加延迟消息属性
            message.getMessageProperties().setDelay(5000);
            return message;
        }
    });
}
```

**[结束]**