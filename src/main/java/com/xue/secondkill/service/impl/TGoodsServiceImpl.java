package com.xue.secondkill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xue.secondkill.entity.TGoods;
import com.xue.secondkill.mapper.TGoodsMapper;
import com.xue.secondkill.service.ITGoodsService;
import com.xue.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements ITGoodsService {
    @Autowired
    private TGoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId)  {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
