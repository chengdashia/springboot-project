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
@ApiModel(value="FileCollection对象", description="")
public class FileCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String collectionId;

    @ApiModelProperty(value = "文件id")
      @TableId(value = "file_id", type = IdType.ID_WORKER)
    private String fileId;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "收藏时间")
    private Date collectionTime;


}
