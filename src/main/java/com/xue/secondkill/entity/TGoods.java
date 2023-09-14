package com.xue.secondkill.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * <p>
 * 
 * </p>
 *
 * @author wxc
 * @since 2023-08-19
 */
@Data
//@EqualsAndHashCode(callSuper = true)
public class TGoods{

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品描述
     */
    private String goodsDetail;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品库存,-1表示没有限制
     */
    private Integer goodsStock;


}
