package com.onlineExam.pojo.stu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 成大事
 * @date 2022/3/19 16:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AnswerInfo对象", description="")
public class StuTestPaper {

    @ApiModelProperty(value = "试卷的uuid")
    @TableId(value = "test_uuid", type = IdType.ID_WORKER)
    private String testUuid;

    @ApiModelProperty(value = "试卷的题目")
    private String testName;

    @ApiModelProperty(value = "试卷的描述")
    private String testDescription;

    @ApiModelProperty(value = "试卷的制作者")
    private String testFounder;

    @ApiModelProperty(value = "试卷总分")
    private Integer pTotalScore;

    @ApiModelProperty(value = "试卷的图片地址")
    private String testImgUrl;


    @ApiModelProperty(value = "学生成绩")
    private Integer score;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
