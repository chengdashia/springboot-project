package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信用户信息
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="微信用户信息")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "open_id")
    @TableId(value = "open_id", type = IdType.ID_WORKER)
    private String openId;

    @ApiModelProperty(value = "skey")
    private String skey;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastVisitTime;

    @ApiModelProperty(value = "session_key")
    private String sessionKey;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "国")
    private String country;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "网名")
    private String nickName;


}
