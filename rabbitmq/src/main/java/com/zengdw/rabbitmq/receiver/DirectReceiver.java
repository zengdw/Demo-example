package com.zengdw.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 消费者
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/12 15:04
 */
@Component
public class DirectReceiver {
    /**
     * ackMode  MANUAL：手动确认 AUTO：自动确认
     */
    @RabbitHandler
    @RabbitListener(queues = "DirectQueue", ackMode = "MANUAL")
    public void process(Message message, Channel channel) {
        MessageProperties properties = message.getMessageProperties();
        try {
            String s = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("DirectReceiver消费者收到消息  : " + s);
            channel.basicAck(properties.getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // 第二个参数 true表示应用于多条序消息 第三个参数 true 重新放入队列
                // channel.basicNack(properties.getDeliveryTag(), false, true);
                // 第二个参数 true表示重新放入队列
                // channel.basicReject(properties.getDeliveryTag(), true);
                // 是否恢复消息到队列，参数默认为true，true则重新入队列，并且尽可能的将之前recover的消息投递给其他消费者消费，而不是自己再次消费。false则消息会重新被投递给自己
                channel.basicRecover(false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
