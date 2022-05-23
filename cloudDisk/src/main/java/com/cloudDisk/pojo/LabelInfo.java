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
 * @since 2022-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LabelInfo对象", description="")
public class LabelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String labelInfoId;

    @ApiModelProperty(value = "标签唯一标识")
      @TableId(value = "interest_label_id", type = IdType.ID_WORKER)
    private String interestLabelId;

    @ApiModelProperty(value = "标签名称")
    private String labelName;


}
