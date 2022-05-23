package com.cloudDisk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
 * @since 2022-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserInfo对象", description="")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String userInfoId;

    @ApiModelProperty(value = "用户唯一标识	")
      @TableId(value = "user_id", type = IdType.ID_WORKER)
    private String userId;

    @ApiModelProperty(value = "用户名称（默认手机+随机字符串5-7位）")
    private String userName;

    @ApiModelProperty(value = "登陆凭证")
    private String userTel;

    @ApiModelProperty(value = "登陆密码")
    private String userPwd;

    @ApiModelProperty(value = "0 未完成；	1 完成")
    private Integer userInitialize;

    @ApiModelProperty(value = "用户自我描述")
    private String userIntroduction;

    @ApiModelProperty(value = "用户地址")
    private String userLocal;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户注册时间")
    private Date createTime;


}
