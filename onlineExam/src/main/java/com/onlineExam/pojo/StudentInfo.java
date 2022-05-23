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
@ApiModel(value="StudentInfo对象", description="")
public class StudentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生学号")
    @TableId(value = "student_id", type = IdType.ID_WORKER)
    private String studentId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学生班级的uuid")
    private String studentClass;

    @ApiModelProperty(value = "学生学院的uuid")
    private String studentInstitute;

    @ApiModelProperty(value = "学生的密码")
    private String studentPwd;

    @ApiModelProperty(value = "学生的性别 0 女生  1 男生")
    private Integer studentSex;




}
