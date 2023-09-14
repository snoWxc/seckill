package com.xue.secondkill.controller;


import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.rabbitmq.MQSender;
import com.xue.secondkill.vo.RespBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wxc
 * @since 2023-08-16
 */
@RestController
@RequestMapping("/user")
public class TUserController{


}
