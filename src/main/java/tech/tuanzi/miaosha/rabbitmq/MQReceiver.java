package tech.tuanzi.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 *
 * @author Patrick Ji
 */
@Service
@Slf4j
public class MQReceiver {
    // @RabbitListener(queues = "queue")
    // public void receive(Object msg) {
    //     log.info("接收消息：" + msg);
    // }
    //
    // // 队列名称要与 RabbitMQConfig 中保持一致
    // @RabbitListener(queues = "queue_fanout01")
    // public void receive01(Object msg) {
    //     log.info("QUEUE01接收消息：" + msg);
    // }
    //
    // @RabbitListener(queues = "queue_fanout02")
    // public void receive02(Object msg) {
    //     log.info("QUEUE02接收消息：" + msg);
    // }

    @RabbitListener(queues = "queue_direct01")
    public void receive03(Object msg) {
        log.info("QUEUE01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void receive04(Object msg) {
        log.info("QUEUE02接收消息：" + msg);
    }
}
