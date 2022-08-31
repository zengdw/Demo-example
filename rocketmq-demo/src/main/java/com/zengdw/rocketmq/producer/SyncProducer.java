package com.zengdw.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 发送同步消息
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/30 16:26
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("group-1");
        // 设置NameServer地址
        producer.setNamesrvAddr("192.168.5.120:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message message = new Message("Topic", "tag-1", ("Hello Rocketmq " + i).getBytes(StandardCharsets.UTF_8));
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            message.setDelayTimeLevel(3);
            // 发送消息到一个Broker
            SendResult result = producer.send(message);

            // 通过sendResult返回消息是否成功送达
            System.out.printf("%s %s %s %n", LocalDateTime.now(), "Hello Rocketmq " + i, result.getSendStatus());

//            Thread.sleep(500);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
