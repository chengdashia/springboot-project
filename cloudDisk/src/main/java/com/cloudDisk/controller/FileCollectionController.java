package com.cloudDisk.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.pojo.FileCollection;
import com.cloudDisk.service.FileCollectionService;
import com.cloudDisk.utils.globalResult.CodeType;
import com.cloudDisk.utils.globalResult.MsgType;
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
 * @since 2022-04-13
 */
@Api(tags = "文件收藏信息")
@RestController
@RequestMapping("/fileCollection")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileCollectionController {

    private final FileCollectionService fileCollectionService;


    /**
     * 文件收藏
     * @param fileId  文件id
     * @return          R
     */
    @ApiOperation("文件收藏")
    @PostMapping("/fileCollection")
    public R fileCollection(
            @ApiParam(value = "文件的id",required = true) @RequestParam("fileId") @NotBlank(message = "文件的id") @NotNull String fileId
    ){
        String uuId = (String) StpUtil.getLoginId();
        try {
            // 查询是否已经收藏
            FileCollection one = fileCollectionService.getOne(new QueryWrapper<FileCollection>()
                    .eq("file_id", fileId)
                    .eq("user_id", uuId));
            // 还没有收藏
            if (one == null){
                one = new FileCollection();
                one.setCollectionId(IdUtil.simpleUUID());
                one.setFileId(fileId);
                one.setUserId(uuId);
                // 收藏
                boolean save = fileCollectionService.save(one);
                if (save){
                    // 收藏成功
                    return R.ok();
                }else {
                    // 收藏失败
                    return R.error();
                }

            }else {
                // 已经收藏
                return R.error(CodeType.COLLECTION_EXIST, MsgType.COLLECTION_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }



    /**
     * 获取自己收藏的文件信息
     * @return  R
     */
    @ApiOperation("获取自己收藏的文件信息")
    @PostMapping("/getMyCollection")
    public R getMyCollection(
    ){
        // 文件名称   文件备注  收藏时间 文件id
        String uuId = (String) StpUtil.getLoginId();

        // 查询自己收藏的文件
        Map<String, Object> map = fileCollectionService.getMyCollection(uuId);
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
     * 删除自己收藏的文件信息
     * @return  R
     * @param fileId    文件id
     */
    @ApiOperation("删除自己收藏的文件信息")
    @PostMapping("/delMyFileCollection")
    public R delMyFileCollection(
            @ApiParam(value = "文件的id",required = true) @RequestParam("fileId") @NotBlank(message = "文件的id") @NotNull String fileId
    ){
        //直接删除
        String uuId = (String) StpUtil.getLoginId();
        try {
            boolean remove = fileCollectionService.remove(new QueryWrapper<FileCollection>()
                    .eq("file_id", fileId)
                    .eq("user_id", uuId));
            if (remove){
                return R.ok();
            }else {
                return R.error();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return R.sqlError();
        }
    }





}

