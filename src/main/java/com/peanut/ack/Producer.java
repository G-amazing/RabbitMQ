package com.peanut.ack;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class Producer {
    private final static String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // durable true 表示队列持久化
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        for (int i = 1; i <= 4; i++) {
            String message = "hello world" + i;
            // MessageProperties.PERSISTENT_TEXT_PLAIN : 表示消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("成功发送第" + i + "条消息...");
        }

    }
}
