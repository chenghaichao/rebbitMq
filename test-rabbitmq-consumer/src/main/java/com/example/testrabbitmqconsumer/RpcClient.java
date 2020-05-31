package com.example.testrabbitmqconsumer;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;


/**
 * RPC客户端
 */
class RPCClient {

    private Connection connection;
    private Channel channel;
    //请求队列
    private static final String requestQueueName  = "rpc_queue";
    //回调队列
    private String replyQueueName;
    public RPCClient() throws IOException, TimeoutException {
        connection = ConnectionUtil.getConnection();
        channel = connection.createChannel();
        //随机生成一个回调队列
        replyQueueName = channel.queueDeclare().getQueue();
    }

    public String call(String message) throws IOException, InterruptedException {
        //随机生产一个id
        final String corrid = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(corrid)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("", requestQueueName, props, message.getBytes());
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        //声明一个消费者 用来消费回调队列
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
                    throws IOException {
                if(properties.getCorrelationId().equals(corrid)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }

        };
        //监听回调队列
        channel.basicConsume(replyQueueName, true, consumer);
        return response.take();
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}

