package com.xue.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.secondkill.entity.TGoods;
import com.xue.secondkill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
public interface ITGoodsService extends IService<TGoods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
