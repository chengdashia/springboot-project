package com.oneKeyRecycling.pojo;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TOrder对象", description="")
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id，主键")
      @TableId(value = "order_id", type = IdType.ID_WORKER)
    private String orderId;

    @ApiModelProperty(value = "外键，用户id")
    private String uId;

    @ApiModelProperty(value = "外键，商家id")
    private String bId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;


    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "物品名字")
    private String sName;

    @ApiModelProperty(value = "物品数量")
    private Integer sQuantity;

    @ApiModelProperty(value = "物品重量")
    private Float sWeight;

    @ApiModelProperty(value = "总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "图片一")
    private String sImgO;

    @ApiModelProperty(value = "图片二")
    private String sImgTw;

    @ApiModelProperty(value = "图片三")
    private String sImgTh;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDel;

    @ApiModelProperty(value = "状态  携带商家id为 1  否则为0")
    private Integer oStatus;

    @ApiModelProperty(value = "用户地址")
    private String uAddress;

    @ApiModelProperty(value = "预约时间")
    private Date appointmentTime;


}
