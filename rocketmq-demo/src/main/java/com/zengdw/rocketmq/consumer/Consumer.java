package com.zengdw.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageClientExt;

import java.nio.charset.StandardCharsets;


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

        /*
          设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
          如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("Topic", "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
            list.forEach(l -> {
                String msg = new String(l.getBody(), StandardCharsets.UTF_8);
                System.out.printf("Receive New Messages: %s %n", msg);
            });
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
