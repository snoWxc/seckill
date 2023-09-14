package com.xue.secondkill.rabbitmq;

import com.xue.secondkill.entity.SeckillMessage;
import com.xue.secondkill.entity.TSeckillOrder;
import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.service.ITGoodsService;
import com.xue.secondkill.service.ITOrderService;
import com.xue.secondkill.utils.JsonUtil;
import com.xue.secondkill.vo.GoodsVo;
import com.xue.secondkill.vo.RespBean;
import com.xue.secondkill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private ITGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ITOrderService orderService;

    /**
     * 下单操作
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收到的消息："+message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        TUser user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount()<1){
            return;
        }
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return;
        }
        //下单操作
        orderService.seckill(user,goodsVo);

    }
}
