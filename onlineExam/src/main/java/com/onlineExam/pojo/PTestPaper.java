package com.onlineExam.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 成大事
 * @since 2022-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PTestPaper对象", description="")
public class PTestPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
      @TableId(value = "p_uuid", type = IdType.ID_WORKER)
    private String pUuid;

    @ApiModelProperty(value = "试卷的uuid")
    private String pTestUuid;

    @ApiModelProperty(value = "发布的单选题的数量")
    private Integer pSingleNum;

    @ApiModelProperty(value = "发布的多选题的数量")
    private Integer pMultipleNum;

    @ApiModelProperty(value = "发布的判断题的数量")
    private Integer pJudgmentNum;

    @ApiModelProperty(value = "发布的试题的总数")
    private Integer pTotalNum;

    @ApiModelProperty(value = "法术的试题的总分值")
    private Integer pTotalScore;

    @ApiModelProperty(value = "可以做几次")
    private Integer pdoNum;


}
