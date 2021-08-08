package com.peanut.fanout;

import com.peanut.util.RabbitMqUtils;
import com.peanut.util.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

public class Consumer01 {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 生成一个临时的队列 队列的名称是随机的
         * 当消费者断开和该队列的连接时 队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();

        //把该临时队列绑定我们的 exchange 其中 routingkey(也称之为 binding key)为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        // 接受消息
        channel.basicConsume(
                queueName,
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
