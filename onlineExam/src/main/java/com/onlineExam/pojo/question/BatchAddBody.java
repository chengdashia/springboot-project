package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 成大事
 * @date 2022/2/23 23:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RequestQuestion对象 ", description="用来接收 json格式的添加题目")
public class BatchAddBody {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

    @ApiModelProperty(value = "试题的uuid")
    private String questionUuid;

    @ApiModelProperty(value = "序号")
    private Integer serialNum;

    @ApiModelProperty(value = "题目")
    private String questionTitle;

    @ApiModelProperty(value = "正确答案")
    private String rightKey;

    @ApiModelProperty(value = "问题选项")
    private Integer questionType;

    @ApiModelProperty(value = "题目的图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "选项")
    private List<String> options;

    @ApiModelProperty(value = "答案解析")
    private String answerAnalysis;

    @ApiModelProperty(value = "分值")
    private int score;
}
