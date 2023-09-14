package com.xue.secondkill.vo;

import com.xue.secondkill.entity.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {
    private TUser user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private int remainSeconds;
}
