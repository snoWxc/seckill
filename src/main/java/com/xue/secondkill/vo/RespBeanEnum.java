package com.xue.secondkill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    LOGIN_ERROR(500210,"用户名或密码错误"),
    MOBILE_ERROR(500211,"手机号码格式不正确"),
    MOBEIL_NOT_EXIST(500213,"手机号不存在"),
    PASSWORD_UPDATE_FAIL(500214,"更新密码失败"),
    SESSION_ERROR(500215,"用户不存在"),
    BIND_ERROR(500212,"参数校验异常"),
    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500501,"该商品每人限购一件"),
    ORDER_NOT_EXIST(500300,"订单信息不存在")
    ;
    private final Integer code;
    private final String message;

}
