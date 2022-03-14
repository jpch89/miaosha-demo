package tech.tuanzi.miaosha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.tuanzi.miaosha.entity.User;
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
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success(user);
    }
}
