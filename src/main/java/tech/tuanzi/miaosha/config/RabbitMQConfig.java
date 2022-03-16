package tech.tuanzi.miaosha.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 * 主要作用：准备队列
 *
 * @author Patrick Ji
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue() {
        // 只有队列和消息都配置了持久化，才可以持久化
        return new Queue("queue", true);
    }
}
