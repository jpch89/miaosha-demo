package tech.tuanzi.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.vo.GoodsVo;

import java.util.Date;

/**
 * 跳转到商品列表页
 *
 * @author Patrick Ji
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;

    @RequestMapping("/toList")
    public String toList(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    /**
     * 跳转商品详情页
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        // 秒杀状态
        int miaoshaStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            // 秒杀还未开始
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀中
            miaoshaStatus = 1;
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodsVo);

        return "goodsDetail";
    }
}
