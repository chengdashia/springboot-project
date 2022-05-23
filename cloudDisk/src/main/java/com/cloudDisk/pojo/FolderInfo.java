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
@ApiModel(value="FolderInfo对象", description="")
public class FolderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String folderInfoId;

    @ApiModelProperty(value = "文件夹唯一标识")
      @TableId(value = "folder_id", type = IdType.ID_WORKER)
    private String folderId;

    @ApiModelProperty(value = "该文件夹属于哪一个用户")
    private String userId;

    @ApiModelProperty(value = "该文件在Hdfs中的路劲")
    private String folderUrl;

    @ApiModelProperty(value = "文件夹名称")
    private String folderName;

    @ApiModelProperty(value = "文件夹描述")
    private String folderTips;

    @ApiModelProperty(value = "文件夹创建时间")
    private Date folderCreateTime;


}
