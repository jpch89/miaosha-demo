package tech.tuanzi.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 秒杀消息发送者
 *
 * @author Patrick Ji
 */
@Service
@Slf4j
public class MiaoshaSender {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMiaoshaMessage(String message) {
        log.info("发送消息：" + message);
        rabbitTemplate.convertAndSend("miaoshaExchange", "miaosha.message", message);
    }

}
