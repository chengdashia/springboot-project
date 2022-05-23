package com.onlineExam.pojo.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 成大事
 * @date 2022/2/19 12:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TestPaperBody对象(试卷题！）", description="")
public class TestPaperBody {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "试卷的uuid")
    @TableId(value = "test_uuid", type = IdType.ID_WORKER)
    private String testUuid;

    @ApiModelProperty(value = "试卷的题目")
    private List<RequestQuestion> questionList;


}
