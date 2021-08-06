package com.peanut.work;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i = 0; i < 10; i++) {
            String message = "hello world" + i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("成功发送第" + i + "条消息...");
        }

    }
}
