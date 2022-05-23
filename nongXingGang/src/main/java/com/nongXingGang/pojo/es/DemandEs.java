package com.nongXingGang.pojo.es;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 成大事
 * @since 2022/4/22 22:22
 */
@Data
@Document(indexName = "demand")
public class DemandEs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword)
    @ApiModelProperty(value = "需求的uuid")
    private String demandUuid;


    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "需求的品种")
    private String demandVarieties;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "需求的种类")
    private String demandType;

    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "需求的重量")
    private BigDecimal demandKilogram;

    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "价格")
    private BigDecimal demandPrice;


    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;


    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "图片地址")
    private String demandImgUrl;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value = "备注")
    private String remarks;

    @Field(type = FieldType.Date)
    @ApiModelProperty(value = "发布时间")
    private Date createTime;

    @Field(type = FieldType.Date)
    @ApiModelProperty(value = "截止日期时间")
    private Date deadline;

    @CompletionField(maxInputLength = 100)
    private Completion suggest;


}
