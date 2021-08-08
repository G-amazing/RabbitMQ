package com.peanut.direct;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class Consumer02 {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 生成队列
        channel.queueDeclare("disk", false, false, false, null);

        // 队列绑定交换机
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        // 接受消息
        channel.basicConsume(
                "disk",
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println("Consumer02 接受消息：" + message);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );
        System.out.println("Consumer02启动成功");
    }
}
