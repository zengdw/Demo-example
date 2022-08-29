package com.zengdw.rabbitmq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消费者
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/12 15:04
 */
@Component
public class DirectReceiver {
    @RabbitHandler
    @RabbitListener(queues = "DirectQueue")
    public void process(Map<String, Object> testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }
}
