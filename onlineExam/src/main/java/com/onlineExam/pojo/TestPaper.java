package com.onlineExam.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@ApiModel(value="TestPaper对象", description="")
public class TestPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "试卷的uuid")
    @TableId(value = "test_uuid", type = IdType.ID_WORKER)
    private String testUuid;

    @ApiModelProperty(value = "试卷的题目")
    private String testName;

    @ApiModelProperty(value = "试卷的描述")
    private String testDescription;

    @ApiModelProperty(value = "试卷的制作者")
    private String testFounder;

    @ApiModelProperty(value = "单选题数目")
    private Integer singleNum;

    @ApiModelProperty(value = "单选题分值")
    private Integer singleScore;

    @ApiModelProperty(value = "多选题数目")
    private Integer multipleNum;

    @ApiModelProperty(value = "多选题分值")
    private Integer multipleScore;

    @ApiModelProperty(value = "判断题数目")
    private Integer judgmentNum;

    @ApiModelProperty(value = "判断题分值")
    private Integer judgmentScore;

    @ApiModelProperty(value = "试卷的试题总数")
    private Integer totalNum;

    @ApiModelProperty(value = "试卷总分")
    private Integer totalScore;

    @ApiModelProperty(value = "试卷的图片地址")
    private String testImgUrl;

    @ApiModelProperty(value = "1：发布；	-1：未发布 0 ：过期")
    private Integer status;

    @ApiModelProperty(value = "发布的单选题数目")
    private Integer pSingleNum;

    @ApiModelProperty(value = "发布的多选题数目")
    private Integer pMultipleNum;

    @ApiModelProperty(value = "发布的判断题数目")
    private Integer pJudgmentNum;

    @ApiModelProperty(value = "发布的总的题目数目")
    private Integer pTotalNum;

    @ApiModelProperty(value = "发布的试卷的总分")
    private Integer pTotalScore;

    @ApiModelProperty(value = "发布的试卷的总分")
    private Integer pDoNum;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除  1:还在；0:删除")
    @TableLogic
    @TableField(value = "is_del")
    private Integer isDel;


}
