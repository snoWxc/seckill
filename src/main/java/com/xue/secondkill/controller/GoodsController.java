package com.xue.secondkill.controller;

import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.service.ITGoodsService;
import com.xue.secondkill.service.ITUserService;

import com.xue.secondkill.vo.DetailVo;
import com.xue.secondkill.vo.GoodsVo;
import com.xue.secondkill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.print.attribute.ResolutionSyntax;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private ITUserService userService;
    @Autowired
    private ITGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, TUser user,HttpServletRequest request,HttpServletResponse response){
        //Redis中获取页面缓存，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html =(String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //return "goodsList";
        //如果缓存为空，则手动渲染，存入redis
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model,TUser tUser,@PathVariable Long goodsId,HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",tUser);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus=0;  //秒杀状态
        int remainSeconds=0;  //秒杀倒计时
        if(nowDate.before(startDate)){
            //秒杀还未开始
            remainSeconds=((int)(startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds=-1;
        }else{
            secKillStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goodsVo);
        WebContext context = new WebContext(request, response, request.getServletContext(), response.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        //return "goodsDetail";
        return html;
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, TUser user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int secKillStatus=0;  //秒杀状态
        int remainSeconds=0;  //秒杀倒计时
        if(nowDate.before(startDate)){
            //秒杀还未开始
            remainSeconds=((int)(startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            //秒杀已结束
            secKillStatus=2;
            remainSeconds=-1;
        }else{
            secKillStatus=1;
            remainSeconds=0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);

    }
}
