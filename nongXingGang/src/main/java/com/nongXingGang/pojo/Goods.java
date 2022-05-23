package com.nongXingGang.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
@ApiModel(value="Goods对象", description="")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @TableId(value = "goods_uuid", type = IdType.ID_WORKER)
    private String goodsUuid;

    @ApiModelProperty(value = "微信用户的openid")
    private String userOpenid;

    @ApiModelProperty(value = "联系人")
    private String goodsLiaisonMan;

    @ApiModelProperty(value = "联系电话")
    private String goodsContactNumber;

    @ApiModelProperty(value = "品种")
    private String goodsVarieties;

    @ApiModelProperty(value = "种类")
    private String goodsType;

    @ApiModelProperty(value = "价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "重量")
    private BigDecimal goodsKilogram;

    @ApiModelProperty(value = "生产地区")
    private String goodsProductionArea;

    @ApiModelProperty(value = "主图地址")
    private String goodsMainImgUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态（0：在售  1：预售）")
    private Integer goodsStatus;

    @ApiModelProperty(value = "发布时间")
    private Date goodsCreateTime;

    @ApiModelProperty(value = "上新时间（预售商品填写）")
    private Date goodsNewTime;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除 0：删除   1：存在")
    private Integer logicalDeletion;

}
