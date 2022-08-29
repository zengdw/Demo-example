package com.zengdw.rabbitmq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 消息推送类
 *
 * @author zengd
 * @version 1.0
 * @date 2022/8/12 15:00
 */
@RestController
public class SendMessageController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() throws JsonProcessingException {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(map);

        // 消息发送到交换器确认
        rabbitTemplate.setConfirmCallback((var1, var2, var3) -> {
            // var2 true 表示消息成功发送到交换器 false 发送失败
            // var3 失败信息
            System.out.println(var1);
            System.out.println(var2);
            System.out.println(var3);
        });
        // 消息从交换器到队列确认 只有失败时才会回调次方法
        rabbitTemplate.setReturnsCallback(System.out::println);
        // 将消息携带绑定键值：DirectRouting 发送到交换机DirectExchange
        rabbitTemplate.convertAndSend("DirectExchange", "DirectRouting", s);
        return "ok";
    }
}
