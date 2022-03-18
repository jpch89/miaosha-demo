package tech.tuanzi.miaosha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.tuanzi.miaosha.entity.MiaoshaMessage;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.Order;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.exception.GlobalException;
import tech.tuanzi.miaosha.rabbitmq.MiaoshaSender;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.service.IMiaoshaOrderService;
import tech.tuanzi.miaosha.service.IOrderService;
import tech.tuanzi.miaosha.utils.JsonUtil;
import tech.tuanzi.miaosha.vo.GoodsVo;
import tech.tuanzi.miaosha.vo.RespBean;
import tech.tuanzi.miaosha.vo.RespBeanEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀
 *
 * @author Patrick Ji
 */
@Slf4j
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IMiaoshaOrderService miaoshaOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MiaoshaSender miaoshaSender;

    @Autowired
    private RedisScript<Long> stockScript;

    /**
     * 内存标记
     * 键为商品 ID
     * 值为 false 表示有库存，值为 true 表示无库存
     */
    private Map<Long, Boolean> emptyStockMap = new HashMap<>();

    /**
     * 秒杀
     * Windows 优化前 QPS：785
     * Linux 优化前 QPS：170
     * <p>
     * 缓存后 Windows QPS：1356
     * RabbitMQ 后 Windows QPS：2454
     */
    @RequestMapping(value = "/{path}/doMiaosha", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doMiaosha(@PathVariable("path") String path, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        // 检查秒杀接口正确性
        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        // 通过内存标记，减少对 Redis 的访问（弹幕说：不能用在分布式环境）
        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 判断是否重复抢购
        MiaoshaOrder miaoshaOrder = (MiaoshaOrder) redisTemplate
                .opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (miaoshaOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        // 预减库存
        Long stock = (Long) redisTemplate.execute(
                stockScript, Collections.singletonList("miaoshaGoods:" + goodsId), Collections.EMPTY_LIST
        );
        if (stock <= 0) {
            emptyStockMap.put(goodsId, true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 下单
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage(user, goodsId);
        miaoshaSender.sendMiaoshaMessage(JsonUtil.object2JsonStr(miaoshaMessage));
        return RespBean.success(0);

        /*
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 判断是否重复抢购
        // MiaoshaOrder miaoshaOrder = miaoshaOrderService.getOne(
        //         new QueryWrapper<MiaoshaOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId)
        // );
        MiaoshaOrder miaoshaOrder = (MiaoshaOrder) redisTemplate
                .opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        Order order = orderService.miaosha(user, goods);
        return RespBean.success(order);
        */
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

    /**
     * 获取秒杀结果
     *
     * @return 如果返回 orderId 表示秒杀成功；返回 -1 表示秒杀失败，返回 0 表示排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        // 弹幕说：秒杀成功后我们会将订单信息存入 Redis，这里建议从 Redis 中查
        Long orderId = miaoshaOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 获取秒杀地址
     */
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId) {
        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        String path = orderService.createPath(user, goodsId);
        return RespBean.success(path);
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if (null == user || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // 设置响应头为输出图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 生成验证码，将结果放入 Redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set(
                "captcha:" + user.getId() + ":" + goodsId, captcha.text(),
                300, TimeUnit.SECONDS
        );
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }

    /**
     * 系统初始化，把商品库存数量加载到 Redis 里面去
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("miaoshaGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);
        });
    }
}
