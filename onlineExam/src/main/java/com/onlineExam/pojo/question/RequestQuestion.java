package com.onlineExam.pojo.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author 成大事
 * @date 2022/2/18 20:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RequestQuestion对象 ", description="用来接收 json格式的添加题目")
@Validated
public class RequestQuestion {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "试卷的uuid不能为空")
    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

    @ApiModelProperty(value = "试题的uuid")
    private String questionUuid;

    @NotNull(message = "序号不能为空")
    @ApiModelProperty(value = "序号")
    private Integer serialNum;

    @NotBlank(message = "题目不能为空")
    @ApiModelProperty(value = "题目")
    private String questionTitle;

    @ApiModelProperty(value = "正确答案")
    @NotEmpty(message = "选项不能为空")
    private List<Integer> rightKey;

    @ApiModelProperty(value = "正确答案解析")
    private String answerAnalysis;

    @NotNull(message = "题目类型不能为空")
    @ApiModelProperty(value = "问题类型")
    private Integer questionType;

    @ApiModelProperty(value = "题目的图片地址")
    private String imgUrl;

    @NotEmpty(message = "选项不能为空")
    @ApiModelProperty(value = "选项")
    private List<String> options;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
