package com.peanut.direct;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class Consumer01 {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 生成队列
        channel.queueDeclare("console", false, false, false, null);

        // 队列绑定交换机
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        // 接受消息
        channel.basicConsume(
                "console",
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println("Consumer01 接受消息：" + message);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );
        System.out.println("Consumer01启动成功");
    }
}
