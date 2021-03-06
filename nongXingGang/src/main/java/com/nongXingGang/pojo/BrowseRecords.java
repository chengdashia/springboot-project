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
@ApiModel(value="BrowseRecords对象", description="")
public class BrowseRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "浏览记录的uuid")
    @TableId(value = "br_uuid", type = IdType.ID_WORKER)
    private String brUuid;

    @ApiModelProperty(value = "微信用户的openid")
    private String userOpenid;

    @ApiModelProperty(value = "在售、预售和需求的类型（0：在售 、1：预售、2：需求）")
    private String thingUuid;

    @ApiModelProperty(value = "在售、预售和需求的类型（0：在售 、1：预售、2：需求）")
    private int thingType;

    @ApiModelProperty(value = "浏览时间")
    private Date createTime;


}
