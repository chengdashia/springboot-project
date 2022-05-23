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
@ApiModel(value="FileLabel对象", description="")
public class FileLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String fileLableId;

    @ApiModelProperty(value = "文件唯一标识")
      @TableId(value = "file_id", type = IdType.ID_WORKER)
    private String fileId;

    @ApiModelProperty(value = "文件标签Id")
    private String fileLabelId;


}
