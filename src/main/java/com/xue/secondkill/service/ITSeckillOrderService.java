package com.xue.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.secondkill.entity.TSeckillOrder;
import com.xue.secondkill.entity.TUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
public interface ITSeckillOrderService extends IService<TSeckillOrder> {

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId：成功，-1：秒杀失败，0：排队中
     */
    Long getResult(TUser user, Long goodsId);
}
