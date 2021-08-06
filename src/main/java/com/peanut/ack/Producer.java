package com.peanut.ack;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

public class Producer {
    private final static String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i = 1; i <= 2; i++) {
            String message = "hello world" + i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("成功发送第" + i + "条消息...");
        }

    }
}
