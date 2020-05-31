package com.example.testrabbitmqconsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * RPC 服务端
 */
class RPCServer {
    //请求队列
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = ConnectionUtil.getConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            //采用公平调度模式
            channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                                           byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                            .correlationId(properties.getCorrelationId()).build();

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        int n = Integer.parseInt(message);
                        System.out.println(" [.] fib(" + message + ")");
                        response += fib(n);
                    } catch (Exception e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        //向回调队列发送结果信息
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes());
                        //处理完后  发送消息确认
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };
            //采用应答模式监听，处理完后才从请求队列中删除请求
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            while (true) {
                synchronized (consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }

    private static int fib(int n) {
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        return fib(n - 1) + fib(n - 2);
    }
}