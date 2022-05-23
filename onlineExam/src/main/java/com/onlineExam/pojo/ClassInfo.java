package com.onlineExam.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 成大事
 * @since 2022-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ClassInfo对象", description="")
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班级的uuid")
    @TableId(value = "class_uuid", type = IdType.ID_WORKER)
    private String classUuid;

    @ApiModelProperty(value = "学院的uuid")
    private String instituteUuid;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班级学生总数")
    private Integer classStudentsNum;


}
