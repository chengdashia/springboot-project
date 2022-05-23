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
@ApiModel(value="FolderFileInfo对象", description="")
public class FolderFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String folderFileInfoId;

    @ApiModelProperty(value = "文件夹唯一标识")
      @TableId(value = "folder_file_id", type = IdType.ID_WORKER)
    private String folderFileId;

    @ApiModelProperty(value = "1,文件夹;2文件")
    private Integer folderFileType;

    @ApiModelProperty(value = "父文件夹Id")
    private String folderPd;


}
