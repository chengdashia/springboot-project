package com.onlineExam.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 成大事
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Admin对象", description="")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "admin的uuid")
    @TableId(value = "admin_uuid", type = IdType.ID_WORKER)
    private String adminUuid;

    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String adminPhone;


    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String adminPwd;

    @ApiModelProperty(value = "名字")
    private String adminName;

    @ApiModelProperty(value = "创建时间")
    private Date adminCreateTime;

    @ApiModelProperty(value = "最后登录时间")
    private Date adminLastTime;


}
