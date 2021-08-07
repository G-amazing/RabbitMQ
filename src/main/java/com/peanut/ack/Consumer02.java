package com.peanut.ack;

import com.peanut.util.RabbitMqUtils;
import com.peanut.util.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class Consumer02 {
    private final static String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 设置不公平分发
        channel.basicQos(1);
        // 接受消息
        channel.basicConsume(
                QUEUE_NAME,
                false,
                (String consumerTag, Delivery delivery) -> {
                    SleepUtils.sleep(10);
                    String message= new String(delivery.getBody());
                    System.out.println("Consumer02 接受消息：" + message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );
        System.out.println("Consumer02启动成功，处理速度为10s");
    }
}
