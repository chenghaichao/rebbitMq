package com.example.testrabbitmqconsumer;

import ch.qos.logback.core.util.TimeUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
    //列队
    private static final String QUEUE_NAME = "queue_demo";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //创建连接
        Connection connection = ConnectionUtil.getConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //同一时刻服务器只会发送一条消息给消费者
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("接受消息: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 * queue 队列名
                 * autoAck 是否自动确认消息,true自动确认,false 不自动要手动调用,建立设置为false
                 */
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //指定消费列队
        channel.basicConsume(QUEUE_NAME, consumer);
        //等待回调函数执行完毕之后，关闭资源
        TimeUnit.SECONDS.sleep(20);
        channel.close();
        connection.close();

    }
}
