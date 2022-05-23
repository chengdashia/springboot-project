package com.manke.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
 * @since 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
      @TableId(value = "u_id", type = IdType.ID_WORKER)
    private String uId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String uAvatar;

    @ApiModelProperty(value = "身份证前面的图片地址")
    private String idObverse;

    @ApiModelProperty(value = "身份证后面的图片地址")
    private String idReverse;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "用户状态   0：普通游客  2：非遗人  1：商家")
    private Integer uStatus;

    @ApiModelProperty(value = "用户描述")
    private String uDescription;

    @ApiModelProperty(value = "用户展示图片")
    private String uShowImg;

    @ApiModelProperty(value = "用户积分 ")
    private Integer uIntegral;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastTime;

    @ApiModelProperty(value = "逻辑删除 0：删除 1：还在")
    private Integer logicalDel;


}
