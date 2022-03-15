package tech.tuanzi.miaosha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.Order;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.service.IMiaoshaOrderService;
import tech.tuanzi.miaosha.service.IOrderService;
import tech.tuanzi.miaosha.vo.GoodsVo;
import tech.tuanzi.miaosha.vo.RespBean;
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
    @Autowired
    private IMiaoshaOrderService miaoshaOrderService;
    @Autowired
    private IOrderService orderService;

    /**
     * 秒杀
     * Windows 优化前 QPS：785
     * Linux 优化前 QPS：170
     */
    @RequestMapping(value = "/doMiaosha", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doMiaosha(Model model, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 判断是否重复抢购
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.getOne(
                new QueryWrapper<MiaoshaOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId)
        );
        if (miaoshaOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        Order order = orderService.miaosha(user, goods);
        return RespBean.success(order);
    }

    /**
     * 秒杀
     * Windows 优化前 QPS：785
     * Linux 优化前 QPS：170
     */
    @RequestMapping(value = "/doMiaosha2")
    public String doMiaosha2(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "miaoshaFail";
        }

        // 判断是否重复抢购
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.getOne(
                new QueryWrapper<MiaoshaOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId)
        );
        if (miaoshaOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return "miaoshaFail";
        }

        Order order = orderService.miaosha(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "orderDetail";
    }
}
