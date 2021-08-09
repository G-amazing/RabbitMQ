package com.peanut.deadLetter;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {
    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 接收消息
        channel.basicConsume(
                DEAD_QUEUE,
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println("Consumer02 接受消息：" + message);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );

    }
}
