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
@ApiModel(value="InstituteInfo对象", description="")
public class InstituteInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学院的uuid")
    @TableId(value = "institute_uuid", type = IdType.ID_WORKER)
    private String instituteUuid;

    @ApiModelProperty(value = "学院名称")
    private String instituteName;

    @ApiModelProperty(value = "学院的学生总数")
    private Integer instituteStudentNums;


}
