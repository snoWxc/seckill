package com.xue.secondkill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xue.secondkill.entity.TUser;
import com.xue.secondkill.vo.LoginVo;
import com.xue.secondkill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wxc
 * @since 2023-08-16
 */
public interface ITUserService extends IService<TUser> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    TUser getTUserByCookie(String userTicket,HttpServletRequest request, HttpServletResponse response);

    RespBean updatePassword(String userTicket,Long id,String password,HttpServletRequest request, HttpServletResponse response);
}
