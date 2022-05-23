package com.nongXingGang.pojo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nongXingGang.utils.validate.phone.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 成大事
 * @date 2022/3/18 20:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodsBody对象,用来接收json数据", description="")
public class GoodsBody {


    @ApiModelProperty(value = "商品的uuid")
    private String goodsUUId;

    @NotBlank(message = "联系人不为空")
    @NotNull(message = "联系人不为空")
    @ApiModelProperty(value = "联系人")
    private String liaisonMan;

    @NotBlank(message = "联系电话不为空")
    @NotNull(message = "联系电话不为空")
    @Phone
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;


    @NotBlank(message = "品种不为空")
    @NotNull(message = "品种不为空")
    @ApiModelProperty(value = "品种")
    private String varieties;

    @ApiModelProperty(value = "种类")
    private String type;

    @DecimalMin(value = "0")
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @DecimalMin(value = "0")
    @NotNull(message = "重量不为空")
    @ApiModelProperty(value = "重量")
    private BigDecimal kilogram;

    @NotBlank(message = "生产地区不为空")
    @NotNull(message = "生产地区不为空")
    @ApiModelProperty(value = "生产地区")
    private String productionArea;

    @NotNull(message = "图片的地址不为空")
    @ApiModelProperty(value = "图片的地址")
    private List<String> fileList;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @Future
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "上新时间（预售商品填写）")
    private Date goodsNewTime;

    @NotNull(message = "状态不为空")
    @Min(0)
    @Max(1)
    @ApiModelProperty(value = "状态（0：在售  1：预售）")
    private Integer status;

}
