package com.peanut.deadLetter.maxLength;

import com.peanut.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import java.util.HashMap;
import java.util.Map;

/**
 * 超过队列的最大长度，使其成为死信
 */
public class Consumer01 {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //普通队列名称
    private static final String NORMAL_QUEUE = "normal-queue";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**声明死信交换机和普通交换机 类型为 direct*/
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        /**声明普通队列*/
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "lisi");
        // 设置正常队列的最大长度
        params.put("x-max-length", 0);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, params);

        /**声明死信队列*/
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        /**死信队列绑定死信交换机与 routingkey*/
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        /**普通队列绑定普通交换机与 routingkey*/
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");


        // 接收消息
        channel.basicConsume(
                NORMAL_QUEUE,
                true,
                (String consumerTag, Delivery delivery) -> {
                    String message= new String(delivery.getBody());
                    System.out.println("Consumer01 接受消息：" + message);
                },
                (consumerTag)-> System.out.println("消息消费被中断")
        );

    }
}
