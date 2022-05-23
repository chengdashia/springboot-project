package com.oneKeyRecycling.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 成大事
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PayRecord对象", description="")
public class PayRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付id")
      @TableId(value = "pay_id", type = IdType.ID_WORKER)
    private String payId;

    @ApiModelProperty(value = "用户id")
    private String uId;

    @ApiModelProperty(value = "商家id")
    private String sId;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "支付时间")
    private Date createTime;


}
