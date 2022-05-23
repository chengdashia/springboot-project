package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * @author 成大事
 * @since 2022/4/11 19:08
 */
@Data
@Valid
public class AnswerSubject {

    @NotNull
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "问题的uuid")
    private String questionUuid;

    @NotNull
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "答案的uuid")
    private String rightKey;


    @NotNull
    @Min(value = 0,message = "不能小于0")
    @Max(value = 2,message = "不能大于2")
    @ApiModelProperty(value = "答案的uuid")
    private Integer questionType;
}
