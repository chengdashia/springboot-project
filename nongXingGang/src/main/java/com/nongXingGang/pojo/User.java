package com.nongXingGang.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
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
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信的openid")
      @TableId(value = "user_openid", type = IdType.ID_WORKER)
    private String userOpenid;

    @ApiModelProperty(value = "微信昵称")
    private String userNickName;

    @ApiModelProperty(value = "性别  1 男  0 女")
    private Integer userGender;

    @ApiModelProperty(value = "session_key")
    private String sessionKey;

    @ApiModelProperty(value = "微信头像地址")
    private String userAvatarUrl;

    @ApiModelProperty(value = "状态（0：买家；	1：卖家  ；	2:合作社；	3:超管  	-1：未认证）")
    private Integer userStatus;

    @ApiModelProperty(value = "注册时间")
    private Date createTime;


    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;


}
