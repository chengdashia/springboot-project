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
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ScoreInfo对象", description="")
public class ScoreInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    @TableId(value = "score_uuid", type = IdType.ID_WORKER)
    private String scoreUuid;

    @ApiModelProperty(value = "学生学号")
    private String studentId;

    @ApiModelProperty(value = "试卷的uuid")
    private String testUuid;

    @ApiModelProperty(value = "成绩")
    private Integer score;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "完成时间")
    private Integer completionTime;

    @ApiModelProperty(value = "次数")
    private Integer frequency;


}
