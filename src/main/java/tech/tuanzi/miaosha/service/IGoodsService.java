package tech.tuanzi.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.tuanzi.miaosha.entity.Goods;
import tech.tuanzi.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品服务类
 * </p>
 *
 * @author Patrick Ji
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 获取商品列表
     */
    List<GoodsVo> findGoodsVo();
}
