package com.example.testrabbitmqconsumer;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class Sender {
    /**
     * 简单模式   生产者   对应消费者  Receiver
     */

//    private final static String QUEUE_NAME = "simple_queue";
//
//    public static void main(String[] args) throws IOException, TimeoutException {
//        //创建连接
//        Connection connection = ConnectionUtil.getConnection();
//        //创建通道
//        Channel channel = connection.createChannel();
//        //声明队列
//        /**
//         * 队列名
//         * 是否持久化
//         *  是否排外  即只允许该channel访问该队列   一般等于true的话用于一个队列只能有一个消费者来消费的场景
//         *  是否自动删除  消费完删除
//         *  其他属性
//         *
//         */
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//        //消息内容
//        /**
//         * 交换机
//         * 队列名
//         * 其他属性  路由
//         * 消息body
//         */
//        String message = "错的不是我，是这个世界~====00==";
//
//        channel.basicPublish("", QUEUE_NAME,null,message.getBytes());
//      //  System.out.println("[x]Sent '"+message + "'");
//
//        //最后关闭通关和连接
//        channel.close();
//        connection.close();
//
//
//    }

//    /**
//     * work模式  生产者  对应消费者  Receiver1，Receiver2
//     */
//
//    private final  static String QUEUE_NAME = "queue_work";
//
//    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
//        Connection connection = ConnectionUtil.getConnection();
//        Channel channel = connection.createChannel();
//
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        for(int i = 0; i < 100; i++){
//            String message = "冬马小三" + i;
//            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//            System.out.println("[x] Sent '"+message + "'");
//            Thread.sleep(i*10);
//        }
//
//        channel.close();
//        connection.close();
//    }

    /**
     * 路由模式  消费者  Receiver3，Receiver4
     */
//    private final static String EXCHANGE_NAME = "exchange_direct";
//    private final static String EXCHANGE_TYPE = "direct";
//
//    public static void main(String[] args) throws IOException, TimeoutException {
//        Connection connection = ConnectionUtil.getConnection();
//        Channel channel = connection.createChannel();
//
//        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);
//
//        String message = "那一定是蓝色";
//        channel.basicPublish(EXCHANGE_NAME,"key2", null, message.getBytes());
//        System.out.println("[x] Sent '"+message+"'");
//
//        channel.close();
//        connection.close();
//    }


    /**
     * 生产者客户端   持久化   对应消费者  RabbitConsumer
     */

    //交换器
    private static final String EXCHANGE_NAME = "exchange_demo";
    //路由
    private static final String ROUTING_KEY = "routingkey_demo";
    //列队
    private static final String QUEUE_NAME = "queue_demo";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //创建连接

        Connection connection = ConnectionUtil.getConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //创建一个tyep='direct' 持久化。非自动删除的交换器
        /**
         * exchange :交换器的名称
         * type : 交换器的类型，常见的有direct,fanout,topic等
         * durable :设置是否持久化。durable设置为true时表示持久化，反之非持久化.持久化可以将交换器存入磁盘，在服务器重启的时候不会丢失相关信息。
         * autoDelete：设置是否自动删除。autoDelete设置为true时，则表示自动删除。自动删除的前提是至少有一个队列或者交换器与这个交换器绑定，之后，所有与这个交换器绑定的队列或者交换器都与此解绑。不能错误的理解—当与此交换器连接的客户端都断开连接时，RabbitMq会自动删除本交换器
         * arguments:其它一些结构化的参数，比如：alternate-exchange
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null );
        //创建一个持久化，非排他的，非自动删除的列队
        /**
         * queue: 队列名称
         * durable： 是否持久化, 队列的声明默认是存放到内存中的，如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，保存到Erlang自带的Mnesia数据库中，当rabbitmq重启之后会读取该数据库
         * exclusive：是否排外的，有两个作用，一：当连接关闭时connection.close()该队列是否会自动删除；二：该队列是否是私有的private，如果不是排外的，可以使用两个消费者都访问同一个队列，没有任何问题，如果是排外的，会对当前队列加锁，其他通道channel是不能访问的，如果强制访问会报异常：com.rabbitmq.client.ShutdownSignalException: channel error; protocol method: #method<channel.close>(reply-code=405, reply-text=RESOURCE_LOCKED - cannot obtain exclusive access to locked queue 'queue_name' in vhost '/', class-id=50, method-id=20)一般等于true的话用于一个队列只能有一个消费者来消费的场景
         * autoDelete：是否自动删除，当最后一个消费者断开连接之后队列是否自动被删除，可以通过RabbitMQ Management，查看某个队列的消费者数量，当consumers = 0时队列就会自动删除
         * arguments：
         * 队列中的消息什么时候会自动被删除？
         * Message TTL(x-message-ttl)：设置队列中的所有消息的生存周期(统一为整个队列的所有消息设置生命周期), 也可以在发布消息的时候单独为某个消息指定剩余生存时间,单位毫秒, 类似于redis中的ttl，生存时间到了，消息会被从队里中删除，注意是消息被删除，而不是队列被删除， 特性Features=TTL, 单独为某条消息设置过期时间AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().expiration(“6000”);
         * channel.basicPublish(EXCHANGE_NAME, “”, properties.build(), message.getBytes(“UTF-8”));
         * Auto Expire(x-expires): 当队列在指定的时间没有被访问(consume, basicGet, queueDeclare…)就会被删除,Features=Exp
         * Max Length(x-max-length): 限定队列的消息的最大值长度，超过指定长度将会把最早的几条删除掉， 类似于mongodb中的固定集合，例如保存最新的100条消息, Feature=Lim
         * Max Length Bytes(x-max-length-bytes): 限定队列最大占用的空间大小， 一般受限于内存、磁盘的大小, Features=Lim B
         * Dead letter exchange(x-dead-letter-exchange)： 当队列消息长度大于最大长度、或者过期的等，将从队列中删除的消息推送到指定的交换机中去而不是丢弃掉,Features=DLX
         * Dead letter routing key(x-dead-letter-routing-key)：将删除的消息推送到指定交换机的指定路由键的队列中去, Feature=DLK
         * Maximum priority(x-max-priority)：优先级队列，声明队列时先定义最大优先级值(定义最大值一般不要太大)，在发布消息的时候指定该消息的优先级， 优先级更高（数值更大的）的消息先被消费,
         * Lazy mode(x-queue-mode=lazy)： Lazy Queues: 先将消息保存到磁盘上，不放在内存中，当消费者开始消费的时候才加载到内存中
         * Master locator(x-queue-master-locator)
         * ##注意
         * 关于队列的声明，如果使用同一套参数进行声明了，就不能再使用其他参数来声明，要么删除该队列重新删除，可以使用命令行删除也可以在RabbitMQ Management上删除，要么给队列重新起一个名字。
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //将交换器与列队通过路由键绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        //发送一条持久化消息 hello world !
        for (int i=1;i<10;i++){
            String message="hello world !"+i;
            channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            TimeUnit.SECONDS.sleep(3);
        }
        String message="hello world !";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

        //关闭资源
        channel.close();
        connection.close();
    }


}