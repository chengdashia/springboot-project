package com.cloudDisk.controller;


import com.cloudDisk.service.FolderFileInfoService;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
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
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-04-15
 */
@Api(tags = "文件 文件夹信息")
@RestController
@RequestMapping("/folderFileInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FolderFileInfoController {

    private final FolderFileInfoService folderFileInfoService;


    /**
     * 根据文件夹id查询文件夹下的文件
     * @param folderId 文件夹id
     * @return               R
     */
    @ApiOperation("根据文件夹id查询文件夹下的文件")
    @PostMapping("/getFolderFileByFolderId")
    public R getFolderFileByFolderId(
            @ApiParam(value = "文件夹id",required = true) @RequestParam("folderId") @NotBlank(message = "文件夹id不能为空") @NotNull String folderId
    ){
        Map<String, Object> map = folderFileInfoService.getFolderFileByFolderId(folderId);
        Object status = map.get("status");
        if (status.equals(StatusType.SUCCESS)){
            map.remove("status");
            return R.ok(map);
        }else if (status.equals(StatusType.SQL_ERROR)){
            return R.sqlError();
        }else {
            return R.error();
        }
    }
}

