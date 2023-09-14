package com.xue.secondkill.vo;

import com.xue.secondkill.entity.TOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private TOrder order;
    private GoodsVo goodsVo;
}
