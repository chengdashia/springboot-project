package com.cloudDisk.pojo;

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
 * @since 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserLabel对象", description="")
public class UserLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String userLabelId;

    @ApiModelProperty(value = "用户唯一标识")
      @TableId(value = "user_id", type = IdType.ID_WORKER)
    private String userId;

    @ApiModelProperty(value = "标签唯一标识")
    private String interestLabelId;


}
