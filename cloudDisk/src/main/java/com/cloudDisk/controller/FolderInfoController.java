package com.cloudDisk.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.pojo.FolderFileInfo;
import com.cloudDisk.pojo.FolderInfo;
import com.cloudDisk.service.FolderFileInfoService;
import com.cloudDisk.service.FolderInfoService;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.hdfs.HdfsUtil;
import com.cloudDisk.utils.safe.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-04-13
 */
@Api(tags = "文件夹信息")
@RestController
@RequestMapping("/folderInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FolderInfoController {

    private final FolderInfoService folderInfoService;

    private final FolderFileInfoService folderFileInfoService;

    /**
     * 创建文件夹
     *
     * @param folderName     文件夹名称
     * @param folderDesc     文件夹描述
     * @param parentFolderId 父文件夹id
     * @return R
     */
    @ApiOperation("创建文件夹")
    @PostMapping("/createFolder")
    public R createFolder(
            @ApiParam(value = "文件夹名称", required = true) @RequestParam("folderName") @NotBlank(message = "文件夹名称不能为空") String folderName,
            @ApiParam("文件夹描述") @RequestParam(value = "folderDesc", required = false, defaultValue = "") String folderDesc,
            @ApiParam(value = "父文件夹id", required = true) @NotBlank(message = "父文件夹id不能为空") @RequestParam("parentFolderId") String parentFolderId
    ) {
        String id = (String) StpUtil.getLoginId();
        FolderInfo folder = folderInfoService.getOne(new QueryWrapper<FolderInfo>()
                .select("folder_url")
                .eq("user_id", id)
                .eq("folder_id", parentFolderId));

        String folderUrl = folder.getFolderUrl() + "/" + folderName;
        HdfsUtil.createFolder(folderUrl);
        try {
            FolderInfo folderInfo = new FolderInfo();
            folderInfo.setFolderInfoId(IdUtil.fastUUID());
            folderInfo.setFolderId(IdUtil.simpleUUID());
            folderInfo.setFolderName(folderName);
            folderInfo.setFolderTips(folderDesc);
            folderInfo.setUserId(id);
            folderInfo.setFolderUrl(folderUrl);
            boolean save = folderInfoService.save(folderInfo);
            if (save) {
                //添加文件夹成功
                //folder_file_info表插入数据
                //uuid  , folder_file_id (file_info.file_id)   ,folder_file_type = 1 ,folder_pd = folder_id
                FolderFileInfo folderFileInfo = new FolderFileInfo();
                folderFileInfo.setFolderFileInfoId(IdUtil.fastUUID());
                folderFileInfo.setFolderFileId(folderInfo.getFolderId());
                folderFileInfo.setFolderPd(parentFolderId);
                folderFileInfo.setFolderFileType(Constant.FOLDER);
                folderFileInfoService.save(folderFileInfo);
                return R.ok();
            } else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 删除文件夹
     *
     * @param folderId 文件id
     * @return R
     */
    @ApiOperation("删除文件夹")
    @PostMapping("/delFolder")
    public R delFolder(
            @ApiParam(value = "文件夹的id", required = true) @RequestParam("folderId") @NotBlank(message = "文件夹id不能为空") String folderId
    ) {
        String id = (String) StpUtil.getLoginId();
        int result = folderInfoService.deleteFolder(id, folderId);
        if (result == StatusType.SUCCESS) {
            return R.ok();
        }else if(result == StatusType.NOT_EXISTS){
            return R.notExists();
        }else {
            return R.error();
        }

    }

}