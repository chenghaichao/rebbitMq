package com.example.testrabbitmqconsumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 持久化消费者
 *
 * Description:消息接收者
 * @RabbitListener bindings:绑定队列
 * @QueueBinding  value：绑定队列的名称
 *                  exchange：配置交换器
 * @Queue : value：配置队列名称
 *          autoDelete:是否是一个可删除的临时队列
 * @Exchange value:为交换器起个名称
 *           type:指定具体的交换器类型
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.error}",autoDelete = "false"),
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.DIRECT,autoDelete = "false"),
                key = "${mq.config.queue.error.routing.key}"
        )
)
public class DurableErrorReceiver {

    /**
     * 接收消息的方法，采用消息队列监听机制
     * @param msg
     */
    @RabbitHandler
    public void process(String msg) throws InterruptedException {
        Thread.sleep(20000);
        System.out.println("error-receiver："+msg);
    }
}