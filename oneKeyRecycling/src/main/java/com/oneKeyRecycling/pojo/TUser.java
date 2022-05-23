package com.oneKeyRecycling.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TUser对象", description="")
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户uuid，主键")
      @TableId(value = "uid", type = IdType.ID_WORKER)
    private String uid;

    @ApiModelProperty(value = "电话号码，用户账号")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "家庭地址")
    private String address;

    @ApiModelProperty(value = "身份证 正面 图片url")
    @TableField("ID_obverse")
    private String idObverse;

    @ApiModelProperty(value = "身份证 反面 图片url")
    @TableField("ID_reverse")
    private String idReverse;

    @ApiModelProperty(value = "身份证号码")
    @TableField("ID_number")
    private String idNumber;

    @ApiModelProperty(value = "真实姓名")
    private String rName;

    @ApiModelProperty(value = "逻辑删除,0,1")
    private Integer isDel;

    @ApiModelProperty(value = "用户状态，0:普通用户,1：商家")
    private Integer uStatus;

    @ApiModelProperty(value = "头像图片url")
    private String uAvatar;

    @ApiModelProperty(value = "账户余额")
    private Double uMoney;

    @ApiModelProperty(value = "备注")
    private String remark;


}
