package com.onlineExam.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 成大事
 * @since 2022-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AnswerInfo对象", description="")
public class AnswerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "答案uuid")
    @TableId(value = "answer_uuid", type = IdType.ID_WORKER)
    private String answerUuid;

    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

    @ApiModelProperty(value = "单选题的答案")
    private String singleAnswer;

    @ApiModelProperty(value = "多选题的答案")
    private String multipleAnswer;

    @ApiModelProperty(value = "判断题的答案")
    private String judgmentAnswer;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
