package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.tuanzi.miaosha.entity.Goods;
import tech.tuanzi.miaosha.mapper.GoodsMapper;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品服务实现类
 * </p>
 *
 * @author Patrick Ji
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
