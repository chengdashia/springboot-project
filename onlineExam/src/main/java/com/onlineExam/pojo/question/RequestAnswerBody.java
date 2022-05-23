package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 成大事
 * @date 2022/2/24 15:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RequestQuestion对象 ", description="用来接收 json格式的添加题目")
public class RequestAnswerBody {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

    @NotNull
//    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "单选题答案")
    private List<String> singleAnswers;

    @NotNull
//    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "多选题答案")
    private List<String> multipleAnswers;

    @NotNull
//    @NotEmpty(message = "不能为空")
    @ApiModelProperty(value = "判断题答案")
    private List<String> judgmentAnswers;

    @NotNull
    @ApiModelProperty(value = "完成时间")
    private Integer completionTime;
}
