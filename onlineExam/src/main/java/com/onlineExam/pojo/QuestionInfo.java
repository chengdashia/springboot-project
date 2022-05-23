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
@ApiModel(value="QuestionInfo对象", description="")
public class QuestionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单选题的uuid")
    @TableId(value = "question_uuid", type = IdType.ID_WORKER)
    private String questionUuid;

    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

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

    @ApiModelProperty(value = "0：单选题；1：多选题；2：判断题；")
    private Integer questionType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
