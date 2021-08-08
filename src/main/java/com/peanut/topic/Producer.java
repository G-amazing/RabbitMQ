package com.peanut.topic;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class Producer {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 发送消息
        channel.basicPublish(EXCHANGE_NAME, "lazy.orange.male.rabbit", null, "111".getBytes());
        System.out.println("成功发送");
        /*for (int i = 1; i <= 4; i++) {
            String message = "hello world" + i;
            // MessageProperties.PERSISTENT_TEXT_PLAIN : 表示消息持久化
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes());
            System.out.println("成功发送第" + i + "条消息...");
        }*/

    }
}
