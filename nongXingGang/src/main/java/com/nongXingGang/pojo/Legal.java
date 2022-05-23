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
@ApiModel(value="Legal对象", description="")
public class Legal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    @TableId(value = "uuid", type = IdType.ID_WORKER)
    private String uuid;

    @ApiModelProperty(value = "微信用户的openid")
    private String userOpenid;

    @ApiModelProperty(value = "注册号")
    private String registrationNum;

    @ApiModelProperty(value = "营业执照的图片")
    private String businessLicenseImg;


}
