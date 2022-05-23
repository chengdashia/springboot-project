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
@ApiModel(value="CoopInfo对象", description="")
public class CoopInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    @TableId(value = "uuid", type = IdType.ID_WORKER)
    private String uuid;

    @ApiModelProperty(value = "合作社uuid")
    private String coopUuid;

    @ApiModelProperty(value = "合作社名称")
    private String coopName;

    @ApiModelProperty(value = "联系人")
    private String coopLiaisonMan;

    @ApiModelProperty(value = "联系电话")
    private String coopTel;

    @ApiModelProperty(value = "地址")
    private String coopPosition;


}
