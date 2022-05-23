package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 成大事
 * @date 2022/2/19 13:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SqlQuestion对象", description="用来接收使用mybatis-plus-join 连表查出的数据")
public class SqlQuestion {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "试题的uuid")
    private String questionUuid;

    @ApiModelProperty(value = "序号")
    private Integer serialNum;

    @ApiModelProperty(value = "题目")
    private String questionTitle;

    @ApiModelProperty(value = "正确答案")
    private String rightKey;

    @ApiModelProperty(value = "答案解析")
    private String answerAnalysis;

    @ApiModelProperty(value = "题目的图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "题目选项的的内容")
    private String optionContent;

    @ApiModelProperty(value = "题目选项的的字母")
    private String serialLetter;

    @ApiModelProperty(value = "题目的类型")
    private Integer questionType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
