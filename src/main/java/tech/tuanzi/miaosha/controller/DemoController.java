package tech.tuanzi.miaosha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试页面跳转
 *
 * @author Patrick Ji
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xxx");
        return "hello";
    }
}
