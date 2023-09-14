package com.xue.secondkill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.exception.GlobalException;
import com.xue.secondkill.mapper.TUserMapper;
import com.xue.secondkill.service.ITUserService;
import com.xue.secondkill.utils.CookieUtil;
import com.xue.secondkill.utils.MD5Util;
import com.xue.secondkill.utils.UUIDUtil;
import com.xue.secondkill.utils.ValidatorUtil;
import com.xue.secondkill.vo.LoginVo;
import com.xue.secondkill.vo.RespBean;
import com.xue.secondkill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wxc
 * @since 2023-08-16
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile=loginVo.getMobile();
        String password=loginVo.getPassword();

//        //判断手机号或者密码是否为空
//        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        //判断手机号格式是否正确
//        if(!ValidatorUtil.isMobile(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        //以前台输入的手机号从数据库中获取用户，如果没获取到就返回错误
        TUser user = userMapper.selectById(mobile);

        if(user==null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //将前台输入的密码用md5util类里面的方法加密之后，是否与数据库中用户的密码一致
        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket= UUIDUtil.uuid();
        //opsForValue就是专门用来操作String类型的数据
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket,user);
        //request.getSession().setAttribute(ticket,tUser);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public TUser getTUserByCookie(String userTicket,HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        TUser user=(TUser) redisTemplate.opsForValue().get("user:"+userTicket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public RespBean updatePassword(String userTicket, Long id, String password,HttpServletRequest request, HttpServletResponse response) {
        TUser user = getTUserByCookie(userTicket, request, response);
        if (user==null){
            throw new GlobalException(RespBeanEnum.MOBEIL_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPasToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if(result==1){
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }

        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
