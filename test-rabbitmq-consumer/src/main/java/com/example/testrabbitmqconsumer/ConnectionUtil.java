package com.example.testrabbitmqconsumer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接工具类
 */
public class ConnectionUtil {


    @Autowired
    private Info info;

    public static Connection getConnection() throws IOException, TimeoutException {
  
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");
//        //连接5672端口  注意15672为工具界面端口  25672为集群端口
//        factory.setPort(5672);
//        factory.setVirtualHost("/");
//        factory.setUsername("guest");
//        factory.setPassword("guest");


        //连接5672端口  注意15672为工具界面端口  25672为集群端口
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        
        
        //获取连接
        Connection connection = factory.newConnection();

        return connection;

    }
}
