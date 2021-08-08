package com.peanut.topic;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class Consumer01 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 生成队列
        channel.queueDeclare("Q1", false, false, false, null);

        // 队列绑定交换机
        channel.queueBind("Q1", EXCHANGE_NAME, "*.orange.*");

        // 接受消息
        channel.basicConsume(
                "Q1",
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println("Q1 接受消息：" + message + " 绑定键：" + delivery.getEnvelope().getRoutingKey());
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );
        System.out.println("Q1启动成功");
    }
}
