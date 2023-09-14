package com.xue.secondkill.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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
public class TSeckillGoods{

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 秒杀家
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startDate;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endDate;


}
