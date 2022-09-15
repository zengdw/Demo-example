package com.zengdw.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

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
        // 设置消费者的最大和最新线程数 即设置ThreadPoolExecutor的核心线程数和最大线程数
//        consumer.setConsumeThreadMax();
//        consumer.setConsumeThreadMin();
        // 每次最大消费的消息条数 默认是 1，即一次只消费一条消息，例如设置为 N，那么每次消费的消息数小于等于 N。
//        consumer.setConsumeMessageBatchMaxSize();
        // 设置消费组的集群/广播模式 默认是集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);

        // 最大重试次数
//        consumer.setMaxReconsumeTimes(10);

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        // 使用MessageSelector.bySql来过滤消息
//        consumer.subscribe("Topic", MessageSelector.bySql("a between 30 and 50"));
        consumer.subscribe("Topic", "*");

        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
            list.forEach(l -> {
                String msg = new String(l.getBody(), StandardCharsets.UTF_8);
                String keys = l.getKeys();
                int reconsumeTimes = l.getReconsumeTimes();
                System.err.printf("重试次数：%s %n", reconsumeTimes);
                System.out.printf("time=%s thread=%s queueId=%s key=%s content=%s %n", LocalDateTime.now(), Thread.currentThread().getName(), l.getQueueId(), keys, msg);
            });
            String id = list.get(0).getProperty("id");
            if(Integer.parseInt(id) % 2 == 0) {
                System.err.println("CONSUME_SUCCESS");
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {
                System.err.println("RECONSUME_LATER");
//                context.setDelayLevelWhenNextConsume(-1);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
