package com.zengdw.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 顺序消费
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/31 13:35
 */
public class OrderConsumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group-1");
        consumer.setNamesrvAddr("192.168.5.120:9876");
        /*
          设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
          如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("Topic", "*");
        consumer.registerMessageListener((MessageListenerOrderly) (list, consumeOrderlyContext) -> {
            list.forEach(l -> {
                String msg = new String(l.getBody(), StandardCharsets.UTF_8);
                System.out.printf("time=%s thread=%s queueId=%s content=%s %n", LocalDateTime.now(), Thread.currentThread().getName(), l.getQueueId(), msg);
            });
            // 标记该消息已经被成功消费
            return ConsumeOrderlyStatus.SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
