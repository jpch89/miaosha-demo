package tech.tuanzi.miaosha.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 *
 * @author Patrick Ji
 */
@Configuration
public class RabbitMQConfig {
    private static final String QUEUE = "miaoshaQueue";
    private static final String EXCHANGE = "miaoshaExchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange miaoshaExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(miaoshaExchange()).with("miaosha.#");
    }
}
