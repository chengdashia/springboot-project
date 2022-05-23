package com.cloudDisk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2022-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FileInfo对象", description="")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String fileInfoId;

    @ApiModelProperty(value = "文件唯一标识")
      @TableId(value = "file_id", type = IdType.ID_WORKER)
    private String fileId;

    @ApiModelProperty(value = "文件在hdfs中的路劲")
    private String filePath;

    @ApiModelProperty(value = "所属文件夹Id")
    private String fileFolderId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "1 word文档类;2 音乐类;3 视频类;4 图片类;5 其他类别")
    private Integer fileType;

    @ApiModelProperty(value = "文件上传者Id")
    private String fileUploadId;

    @ApiModelProperty(value = "文件上传时间")
    private Date fileUploadTime;

    @ApiModelProperty(value = "0 私密;1 公开;2 在售")
    private Integer fileStatus;

    @ApiModelProperty(value = "文件备注")
    private String fileOthers;

    @ApiModelProperty(value = "只要该文件被访问一次之后，点击量+1")
    private String fileClickNums;

    @ApiModelProperty(value = "文件下载次数")
    private String fileDownloadNums;

    @ApiModelProperty(value = "文件的封面地址")
    private String fileAvatar;

    @TableLogic(value = "1",delval = "0")
    @ApiModelProperty(value = "0:删除；1:存在")
    private Integer fileDel;


}
