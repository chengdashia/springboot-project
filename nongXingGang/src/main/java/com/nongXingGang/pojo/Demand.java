package com.nongXingGang.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@ApiModel(value="Demand对象", description="")
public class Demand implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "需求的uuid")
    @TableId(value = "demand_uuid", type = IdType.ID_WORKER)
    private String demandUuid;

    @ApiModelProperty(value = "微信用户的uuid")
    private String userOpenid;

    @NotNull
    @NotBlank(message = "需求的品种不能为空")
    @ApiModelProperty(value = "需求的品种")
    private String demandVarieties;

    @NotNull
    @NotBlank(message = "需求的种类不能为空")
    @ApiModelProperty(value = "需求的种类")
    private String demandType;

    @NotNull
    @DecimalMin(value = "0")
    @ApiModelProperty(value = "需求的重量")
    private BigDecimal demandKilogram;

    @DecimalMin(value = "0")
    @ApiModelProperty(value = "价格")
    private BigDecimal demandPrice;


    @NotNull
    @NotBlank(message = "联系人不能为空")
    @ApiModelProperty(value = "联系人")
    private String demandLisisonMan;


    @NotNull
    @NotBlank(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话")
    private String demandContactNumber;

    @NotNull
    @NotBlank(message = "详细地址不能为空")
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;


    @ApiModelProperty(value = "图片地址")
    private String demandImgUrl;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "发布时间")
    private Date createTime;

    @Future
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "截止日期时间")
    private Date deadline;


}
