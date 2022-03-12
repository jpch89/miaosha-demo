package tech.tuanzi.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.service.impl.GoodsServiceImpl;
import tech.tuanzi.miaosha.vo.GoodsVo;
import tech.tuanzi.miaosha.vo.RespBeanEnum;

/**
 * 秒杀
 *
 * @author Patrick Ji
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    private IGoodsService goodsService;

    /**
     * 秒杀
     */
    @RequestMapping("/doMiaosha")
    public String doMiaosha(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "miaoshaFail";
        }

        return "";
    }
}
