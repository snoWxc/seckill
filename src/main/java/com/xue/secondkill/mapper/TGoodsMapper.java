package com.xue.secondkill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xue.secondkill.entity.TGoods;
import com.xue.secondkill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
