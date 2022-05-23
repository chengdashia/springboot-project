package com.cloudDisk.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.pojo.FileDel;
import com.cloudDisk.service.FileDelService;
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
 * @author 成大事
 * @since 2022-04-13
 */
@Api(tags = "回收站接口")
@RestController
@RequestMapping("/fileDel")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileDelController {


    private final FileDelService fileDelService;

    /**
     * 获取回收站  自己删除的文件信息
     * @return  R
     */
    @ApiOperation("获取回收站  自己删除的文件信息")
    @PostMapping("/getRecycleMyFileList")
    public R getRecycleMyFileList(){
        // 文件名称   文件备注  删除时间 文件id
        String uuId = (String) StpUtil.getLoginId();
        // 查询自己收藏的文件
        Map<String, Object> map = fileDelService.getRecycleMyFileList(uuId);
        Object status = map.get("status");
        if (status.equals(StatusType.SUCCESS)){
            return R.ok(map.get("data"));
        }else if (status.equals(StatusType.SQL_ERROR)){
            return R.sqlError();
        }else {
            return R.error();
        }
    }

    /**
     * 彻底删除回收站  自己删除的文件信息
     * @return  R
     */
    @ApiOperation("彻底删除回收站  自己删除的文件信息")
    @PostMapping("/delRecycleMyFile")
    public R delRecycleMyFile(
            @ApiParam(value = "文件的id",required = true) @RequestParam("fileId") @NotBlank(message = "文件的id") @NotNull String fileId
    ){
        String id = (String) StpUtil.getLoginId();
        int del = fileDelService.delRecycleMyFile(id, fileId);
        if (del == StatusType.SUCCESS){
            return R.ok();
        }else if (del == StatusType.NOT_EXISTS){
            return R.notExists();
        }else {
            return R.error();
        }
    }

}

