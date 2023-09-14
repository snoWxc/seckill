package com.xue.secondkill.controller;


import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.service.ITOrderService;
import com.xue.secondkill.vo.OrderDetailVo;
import com.xue.secondkill.vo.RespBean;
import com.xue.secondkill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
@RestController
@RequestMapping("/order")
public class TOrderController{

    @Autowired
    private ITOrderService orderService;
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(TUser user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail=orderService.detail(orderId);
        return RespBean.success(detail);
    }
}
