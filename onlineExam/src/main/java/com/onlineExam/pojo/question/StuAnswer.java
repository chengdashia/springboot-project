package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 成大事
 * @since 2022/4/11 19:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Valid
public class StuAnswer {

    @NotNull
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

    @ApiModelProperty(value = "答案")
    private List<AnswerSubject> answerSubjectList;

    @NotNull
    @ApiModelProperty(value = "完成时间")
    private Integer completionTime;


}
