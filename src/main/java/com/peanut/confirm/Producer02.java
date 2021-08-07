package com.peanut.confirm;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 异步发布确认模式
 *
 *
 */
public class Producer02 {
    private final static String QUEUE_NAME = "confirm_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        // 开启发布确认
        channel.confirmSelect();

        // 记录开始时间
        long begin = System.currentTimeMillis();

        ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();

        // 消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmMap = map.headMap(deliveryTag);
                confirmMap.clear();
            } else {
                map.remove(deliveryTag);
            }
            System.out.println("已确认的消息: " + deliveryTag);
        };

        // 消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息: " + deliveryTag);
        };

        // 监听器 监听成功和失败的消息 (异步通知)
        channel.addConfirmListener(ackCallback, nackCallback);

        // 发送消息
        for (int i = 1; i <= 1000; i++) {
            String message = i + "";
            map.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        // 记录结束时间
        long end = System.currentTimeMillis();

        System.out.println("耗时：" + (end - begin) + "ms");

    }
}
