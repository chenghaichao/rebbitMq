package com.example.testrabbitmqconsumer;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * work模式 消费者2
 */
public class Receiver2 {
    private final static  String QUEUE_NAME = "queue_work";

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false,false, false,null);
        //同一时刻服务器只会发送一条消息给消费者
        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, false, consumer);

        while(true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("[x] Received2 '"+message+"'");
            Thread.sleep(1000);
            //返回确认状态
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

    }
}
