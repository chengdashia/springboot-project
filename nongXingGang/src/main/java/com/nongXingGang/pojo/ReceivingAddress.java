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
@ApiModel(value="ReceivingAddress对象", description="")
public class ReceivingAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
      @TableId(value = "uuid", type = IdType.ID_WORKER)
    private String uuid;

    @ApiModelProperty(value = "微信用户的openid")
    private String userOpenid;

    @ApiModelProperty(value = "用户的真实姓名")
    private String userRealName;

    @ApiModelProperty(value = "用户的手机号")
    private String userTel;

    @ApiModelProperty(value = "用户的详细地址")
    private String userDetailedAddress;


}
