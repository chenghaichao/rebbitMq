package com.example.testrabbitmqconsumer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Info {
@Value("${spring.rabbitmq.host}")
  private   String host;
  @Value("${spring.rabbitmq.port}")
    private int port;
  @Value("${spring.rabbitmq.username}")
    private String username;
  @Value("${spring.rabbitmq.password}")
    private String password;

}
