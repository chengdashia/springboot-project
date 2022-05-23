package com.nongXingGang.pojo;

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
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Store对象", description="")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收藏的uuid")
      @TableId(value = "col_uuid", type = IdType.ID_WORKER)
    private String colUuid;

    @ApiModelProperty(value = "微信用户的openid")
    private String userOpenid;

    @ApiModelProperty(value = "在售、预售和需求的uuid")
    private String thingUuid;

    @ApiModelProperty(value = "在售、预售和需求的类型（0：在售 、1：预售、2：需求）")
    private Integer thingType;

    @ApiModelProperty(value = "收藏时间")
    private Date colTime;


}
