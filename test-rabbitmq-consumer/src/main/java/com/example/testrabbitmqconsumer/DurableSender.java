package com.example.testrabbitmqconsumer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.Mergeable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 持久化生成者
 * Date:2020/4/26
 * Description:发送消息
 */
@Component
public class DurableSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    //exChange 交换器
    @Value("${mq.config.exchange}")
    private String exChange;

    //routingkey 路由键
    @Value("${mq.config.queue.error.routing.key}")
    private String routingKey;
    /**
     * 发送消息的方法
     * @param msg
     */
    public void send(String msg){
        //向消息队列发送消息
        //参数1：交换器名称
        //参数2：路由键
        //参数3：消息
        this.amqpTemplate.convertAndSend(exChange,routingKey,msg);
        Mergeable mergeable = new Mergeable() {
            @Override
            public boolean isMergeEnabled() {
                return false;
            }

            @Override
            public Object merge(Object parent) {
                return null;
            }
        };
        amqpTemplate.convertAndSend(exChange,routingKey,mergeable);

    }
}
