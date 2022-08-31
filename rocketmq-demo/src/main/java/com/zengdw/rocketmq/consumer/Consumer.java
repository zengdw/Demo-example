package com.zengdw.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


/**
 * 消费者
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/30 16:31
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group-1");
        consumer.setNamesrvAddr("192.168.5.120:9876");

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        // 使用MessageSelector.bySql来过滤消息
        consumer.subscribe("Topic", MessageSelector.bySql("a between 30 and 50"));
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
            list.forEach(l -> {
                String msg = new String(l.getBody(), StandardCharsets.UTF_8);
                System.out.printf("time=%s thread=%s queueId=%s content=%s %n", LocalDateTime.now(), Thread.currentThread().getName(), l.getQueueId(), msg);
            });
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
