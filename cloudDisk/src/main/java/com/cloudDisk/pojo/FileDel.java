package com.cloudDisk.pojo;

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
 * @since 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FileDel对象", description="")
public class FileDel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String fileDelId;

    @ApiModelProperty(value = "文件Id")
      @TableId(value = "file_id", type = IdType.ID_WORKER)
    private String fileId;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "文件删除时间")
    private Date fileDelTime;

    @ApiModelProperty(value = "1:未过期；2：过期")
    private Integer fileDelStatus;


}
