package com.xue.secondkill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xue.secondkill.entity.SeckillMessage;
import com.xue.secondkill.entity.TOrder;
import com.xue.secondkill.entity.TSeckillOrder;
import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.rabbitmq.MQSender;
import com.xue.secondkill.service.ITGoodsService;
import com.xue.secondkill.service.ITOrderService;
import com.xue.secondkill.service.ITSeckillOrderService;
import com.xue.secondkill.utils.JsonUtil;
import com.xue.secondkill.vo.GoodsVo;
import com.xue.secondkill.vo.RespBean;
import com.xue.secondkill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    private ITGoodsService goodsService;
    @Autowired
    private ITSeckillOrderService seckillOrderService;
    @Autowired
    private ITOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> script;

    private Map<Long,Boolean> EmptyStockMap=new HashMap<>();

    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, TUser user, Long goodsId){
        if(user==null){
            return "login";
        }

        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        if(goods.getStockCount()<=0){
            model.addAttribute("errormsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }

        //判断用户是否重复下单,如果有重复的就会存一个seckillOrder
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        TOrder order=orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";

    }

    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(Model model, TUser user, Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记，减少Redis的访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //Long stock = (Long)redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if(stock<0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);

//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//        if(goods.getStockCount()<=0){
//            model.addAttribute("errormsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//
//        //判断用户是否重复下单,如果有重复的就会存一个seckillOrder
//        //TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
//
//        if(seckillOrder!=null){
//            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
//            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
//        }

    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping("/result")
    @ResponseBody
    public RespBean getResult(TUser user,Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId=seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }

    @GetMapping("/path")


    /**
     * 系统初始化，把商品库存数量加载到Redis
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list=goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVo ->{
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
        });


    }

}
