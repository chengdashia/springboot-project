package com.oneKeyRecycling.pojo;

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
 * @since 2022-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TAdmin对象", description="")
public class TAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "aid", type = IdType.ID_WORKER)
    private String aid;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDel;

    @ApiModelProperty(value = "等级")
    private String grade;


}
