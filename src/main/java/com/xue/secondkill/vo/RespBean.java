package com.xue.secondkill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /**
     * 返回成功结果
     * @return
     */
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }

    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBean.success().getMessage(),obj);
    }

    /**
     * 返回成功结果
     * @param RespBeanEnum
     * @return
     */

    public static RespBean error(RespBeanEnum RespBeanEnum){
        return new RespBean(RespBeanEnum.getCode(),RespBeanEnum.getMessage(),null);
    }

    public static RespBean error(RespBeanEnum RespBeanEnum,Object obj){
        return new RespBean(RespBeanEnum.getCode(),RespBeanEnum.getMessage(),obj);
    }

}
