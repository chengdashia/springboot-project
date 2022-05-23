package com.onlineExam.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 成大事
 * @since 2022-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OptionInfo对象", description="")
public class OptionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "选项的uuid")
    @TableId(value = "option_uuid", type = IdType.ID_WORKER)
    private String optionUuid;

    @ApiModelProperty(value = "问题的uuid")
    private String questionUuid;

    @ApiModelProperty(value = "选项内容")
    private String optionContent;

    @ApiModelProperty(value = "选项位置（如A B C)")
    private String serialLetter;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
