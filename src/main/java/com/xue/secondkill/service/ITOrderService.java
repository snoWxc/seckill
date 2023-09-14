package com.xue.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.secondkill.entity.TOrder;
import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.vo.GoodsVo;
import com.xue.secondkill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
public interface ITOrderService extends IService<TOrder> {

    TOrder seckill(TUser tUser, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}
