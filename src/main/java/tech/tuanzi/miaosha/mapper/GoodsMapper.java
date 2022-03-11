package tech.tuanzi.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.tuanzi.miaosha.entity.Goods;
import tech.tuanzi.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品Mapper 接口
 * </p>
 *
 * @author Patrick Ji
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     */
    List<GoodsVo> findGoodsVo();
}
