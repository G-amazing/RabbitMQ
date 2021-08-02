package com.peanut.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

public class Consumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 创建连接
        Connection connection = factory.newConnection();

        // 获取信道
        Channel channel = connection.createChannel();

        // 接受消息
        channel.basicConsume(
                QUEUE_NAME,
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println(message);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
                );
    }
}
