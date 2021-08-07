package com.peanut.confirm;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 发布确认模式 (单个确认)
 *
 *
 */
public class Producer01 {
    private final static String QUEUE_NAME = "confirm_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        // 开启发布确认
        channel.confirmSelect();

        // 记录开始时间
        long begin = System.currentTimeMillis();

        // 发送消息
        for (int i = 1; i <= 1000; i++) {
            String message = i + "";
            // MessageProperties.PERSISTENT_TEXT_PLAIN : 表示消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 等待发送消息的结果
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("成功发送第" + i + "条消息...");
            }
        }

        // 记录结束时间
        long end = System.currentTimeMillis();

        System.out.println("耗时：" + (end - begin) + "ms");

    }
}
