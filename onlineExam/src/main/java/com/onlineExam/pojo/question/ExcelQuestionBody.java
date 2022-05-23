package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 成大事
 * @date 2022/2/23 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value="ExcelQuestionBody对象 ", description="用来读取EXCEL格式的添加题目")
public class ExcelQuestionBody {

    @ApiModelProperty(value = "试题的类型")
    private Integer type;

    @ApiModelProperty(value = "试题的序号")
    private Integer serialNum;

    @ApiModelProperty(value = "试题的题目")
    private String title;

    @ApiModelProperty(value = "试题的选项1")
    private String option1;

    @ApiModelProperty(value = "试题的选项2")
    private String option2;

    @ApiModelProperty(value = "试题的选项3")
    private String option3;

    @ApiModelProperty(value = "试题的选项4")
    private String option4;

    @ApiModelProperty(value = "试题的选项5")
    private String option5;

    @ApiModelProperty(value = "正确答案")
    private String rightKey;

    @ApiModelProperty(value = "答案解析")
    private String answerAnalysis;

    @ApiModelProperty(value = "分值")
    private String score;
}
