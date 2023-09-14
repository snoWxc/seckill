package com.xue.secondkill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xue.secondkill.entity.TOrder;
import com.xue.secondkill.entity.TSeckillGoods;
import com.xue.secondkill.entity.TSeckillOrder;
import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.exception.GlobalException;
import com.xue.secondkill.mapper.TOrderMapper;
import com.xue.secondkill.service.ITGoodsService;
import com.xue.secondkill.service.ITOrderService;
import com.xue.secondkill.service.ITSeckillGoodsService;
import com.xue.secondkill.service.ITSeckillOrderService;
import com.xue.secondkill.vo.GoodsVo;
import com.xue.secondkill.vo.OrderDetailVo;
import com.xue.secondkill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {
    @Autowired
    private ITSeckillGoodsService seckillGoodsService;
    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private ITSeckillOrderService seckillOrderService;
    @Autowired
    private ITGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public TOrder seckill(TUser user, GoodsVo goods) {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        //秒杀商品减库存
        TSeckillGoods seckillGoods=seckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        boolean result = seckillGoodsService.update(new UpdateWrapper<TSeckillGoods>().setSql("stock_count=stock_count-1")
                .eq("goods_id", goods.getId()).gt("stock_count", 0));
        if(seckillGoods.getStockCount()<1){
            //判断是否还有库存
            valueOperations.set("isStockEmpty:"+goods.getId(),"0");
            return null;
        }

        TOrder order=new TOrder();
        //生成订单
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        TSeckillOrder seckillOrder = new TSeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);


        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        TOrder order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail=new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);

        return detail;
    }
}
