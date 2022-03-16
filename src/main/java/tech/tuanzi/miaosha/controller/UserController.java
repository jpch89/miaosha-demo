package tech.tuanzi.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.rabbitmq.MQSender;
import tech.tuanzi.miaosha.vo.RespBean;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author Patrick Ji
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MQSender mqSender;

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success(user);
    }

    // /**
    //  * 测试发送 RabbitMQ 消息
    //  */
    // @RequestMapping("/mq")
    // @ResponseBody
    // public void mq() {
    //     mqSender.send("Hello");
    // }
    //
    // /**
    //  * Fanout 模式
    //  */
    // @RequestMapping("/mq/fanout")
    // @ResponseBody
    // public void mq01() {
    //     mqSender.send("Hello");
    // }

    /**
     * Direct 模式
     */
    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mq02() {
        mqSender.send01("Hello, Red");
    }

    /**
     * Direct 模式
     */
    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq03() {
        mqSender.send02("Hello, Green");
    }
}
