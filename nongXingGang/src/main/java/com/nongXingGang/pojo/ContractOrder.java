package com.nongXingGang.pojo;

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
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ContractOrder对象", description="")
public class ContractOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单uuid")
      @TableId(value = "order_uuid", type = IdType.ID_WORKER)
    private String orderUuid;

    @ApiModelProperty(value = "买家openid")
    private String buyerOpenid;

    @ApiModelProperty(value = "卖家openid")
    private String sellerOpenid;

    @ApiModelProperty(value = "商品的uuid")
    private String goodsUuid;

    @ApiModelProperty(value = "收货地址的uuid")
    private String receivingAddressUuid;

    @ApiModelProperty(value = "订单创建时间")
    private Date orderCreateTime;

    @ApiModelProperty(value = "订单状态（1，代签字 2 ；已签字，3，拒绝签字，4交易成功）")
    private Integer orderStatus;

    @ApiModelProperty(value = "下单重量")
    private BigDecimal orderKilogram;

    @ApiModelProperty(value = "备注")
    private String orderRemarks;

    @ApiModelProperty(value = "合同地址")
    private String contractAddress;


    @ApiModelProperty(value = "订单更新时间")
    private Date updateTime;


}
