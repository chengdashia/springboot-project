package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 成大事
 * @date 2022/2/24 18:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value="ExcelQuestionBody对象 ", description="用来读取EXCEL格式的添加题目")
public class ExportResult {

    @ApiModelProperty(value = "试卷的uuid")
    private String studentUuid;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学院名称")
    private String instituteName;

    @ApiModelProperty(value = "班级")
    private String className;

    @ApiModelProperty(value = "成绩")
    private Integer score;

    @ApiModelProperty(value = "提交时间")
    private String createTime;



}
