package com.nongXingGang.pojo;

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
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Certification对象", description="")
public class Certification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
      @TableId(value = "uuid", type = IdType.ID_WORKER)
    private String uuid;

    @ApiModelProperty(value = "微信用户的openid")
    private String userOpenid;

    @ApiModelProperty(value = "用户的真实姓名")
    private String userName;

    @ApiModelProperty(value = "用户的身份证号码")
    private String userIdNum;

    @ApiModelProperty(value = "用户的身份证前面照片")
    private String userIdFrontImg;

    @ApiModelProperty(value = "用户的身份证背面照片")
    private String userIdBackImg;


}
