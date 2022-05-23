package com.nongXingGang.pojo.es;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 成大事
 * @since 2022/4/22 22:14
 */
@Data
@Document(indexName = "goods")
@Setting(settingPath = "elasticsearch/setting.json")
@Mapping(mappingPath = "elasticsearch/mapping.json")
public class GoodEs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @Field(type = FieldType.Keyword)
    private String goodsUuid;

//    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "品种")
    private String goodsVarieties;

//    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "用户的openid")
    private String userOpenid;

//    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "种类")
    private String goodsType;

//    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "价格")
    private BigDecimal goodsPrice;

//    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "重量")
    private BigDecimal goodsKilogram;

//    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "生产地区")
    private String goodsProductionArea;

    @ApiModelProperty(value = "主图地址")
    private String goodsMainImgUrl;

//    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "备注")
    private String remark;

//    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "状态（0：在售  1：预售）")
    private Integer goodsStatus;

//    @Field(type = FieldType.Date)
    @ApiModelProperty(value = "发布时间")
    private Date goodsCreateTime;

//    @Field(type = FieldType.Date)
    @ApiModelProperty(value = "上新时间（预售商品填写）")
    private Date goodsNewTime;

    @CompletionField(maxInputLength = 100)
    private Completion suggest;

}
