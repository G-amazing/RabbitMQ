package com.peanut.work;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class Consumer02 {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 接受消息
        channel.basicConsume(
                QUEUE_NAME,
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println("Consumer02 接受消息：" + message);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );
    }
}
