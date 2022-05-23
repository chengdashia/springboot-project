package com.cloudDisk.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudDisk.pojo.FileInfo;
import com.cloudDisk.service.FileDelService;
import com.cloudDisk.service.FileInfoService;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022-04-10
 */
@Slf4j
@Api(tags = "文件信息")
@RestController
@RequestMapping("/fileInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileInfoController {

    private final FileInfoService fileInfoService;

    private final FileDelService fileDelService;

    /**
     * 根据文件id获取文件信息
     * @param fileId  文件id
     * @return  R
     */
    @ApiOperation("根据文件id获取文件信息")
    @PostMapping("/getFileInfoById")
    public R getFileInfoById(
            @ApiParam(value = "文件id",required = true) @RequestParam("FileId") @NotBlank(message = "文件id不能为空") String fileId
    ){

        //获取文件信息 不要file_info_id,file_folder_id,file_status,file_del
        //还要有用户名，用户头像。用户id
        //通过标签表查询标签
        try {
            Map<String, Object> map = fileInfoService.getFileInfoById(fileId);
            Object status = map.get("status");
            if(status.equals(StatusType.SUCCESS)){
                return R.ok(map);
            }else {
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }

    }



    /**
     * 根据页数获取文件信息列表（一页20个）
     * @param page  页码
     * @return  R
     */
    @ApiOperation("根据页数获取文件信息列表（一页20个）")
    @PostMapping("/getFileInfoListByPage")
    public R getFileInfoListByPage(
            @ApiParam(value = "页码",required = true) @RequestParam("Page") @NotNull @Min(0) int page
    ){
        System.out.println("page:  "+page);
        Map<String, Object> map = fileInfoService.getFileInfoListByPage(page);
        Object status = map.get("status");
        if(status.equals(StatusType.SUCCESS)){
            return R.ok(map.get("data"));
        }else if(status.equals(StatusType.SQL_ERROR)){
            return R.sqlError();
        }else {
            return R.error();
        }

    }


    /**
     * 获取点击量最高的前十个文件信息
     * @return  R
     */
    @ApiOperation("获取点击量最高的前十个文件信息")
    @PostMapping("/getFIieTop10")
    public R getFIieTop10(){
        Map<String,Object> map = fileInfoService.getFIieTop10();
        Object status = map.get("status");
        if(status.equals(StatusType.SUCCESS)){
            return R.ok(map.get("data"));
        }else if(status.equals(StatusType.SQL_ERROR)){
            return R.sqlError();
        }else {
            return R.error();
        }
    }


    /**
     * 随机获取十条文件信息
     * @return  R
     */
    @ApiOperation("随机获取十条文件信息")
    @PostMapping("/getFIieRandom10")
    public R getFIieRandom10(){

        try {
            int count = fileInfoService.count();
            int random = RandomUtil.randomInt(0, count - 10);
            Page<Map<String, Object>> mapPage = fileInfoService.getBaseMapper().selectMapsPage(
                    (new Page<>(random, 10)),
                    new QueryWrapper<FileInfo>()
                            .select("file_id","file_name")
                            .eq("file_status", Constant.PUBLIC_TYPE));
            if(mapPage != null){
                return R.ok(mapPage);
            }else {
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 上传文件
     * @param folderId             父文件夹id
     * @param file                 文件
     * @param labelList            标签列表
     * @return         R
     */
    @ApiOperation("上传文件")
    @PostMapping("/uploadFile")
    public R uploadFile(
            @ApiParam(value = "父文件夹id",required = true) @RequestParam("folder_id") @NotBlank(message = "父文件夹id不能为空") String folderId,
            @ApiParam(value = "文件名字",required = true) @RequestParam("fileName") @NotBlank(message = "文件名字不能为空") String fileName,
            @ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "标签列表",required = true) @RequestParam("labelList") List<String> labelList
    ){
        String id = (String) StpUtil.getLoginId();
        log.info("上传文件，用户id："+id);
        log.info("上传文件，父文件夹id："+folderId);
        log.info("上传文件，文件名字："+fileName);
        log.info("上传文件，标签列表："+labelList);
        log.info("上传文件，文件："+file.getOriginalFilename());
        int result = fileInfoService.uploadFile(id, folderId, file, labelList, fileName);
        if(result == StatusType.SUCCESS){
            return R.ok();
        }else {
            return R.error();
        }
//        return R.ok();
    }




    /**
     * 逻辑 删除文件
     * @param fileId  文件id
     * @return          R
     */
    @ApiOperation("逻辑 删除文件")
    @PostMapping("/delFile")
    public R delFile(
            @ApiParam(value = "文件的id",required = true) @RequestParam("fileId") @NotBlank(message = "文件id") String fileId
    ){
        String uuId = (String) StpUtil.getLoginId();
        int result = fileInfoService.delFile(uuId,fileId);
        if (result == StatusType.SUCCESS){
            return R.ok();
        }else {
            return R.error();
        }
    }



    /**
     * 更改文件状态
     * @param fileId  文件id
     * @param fileStatus  文件id
     * @return          R
     */
    @ApiOperation("更改文件状态")
    @PostMapping("/updateFileStatus")
    public R updateFileStatus(
            @ApiParam(value = "文件id",required = true) @RequestParam("fileId") @NotBlank(message = "文件id") String fileId,
            @ApiParam(value = "文件状态",required = true) @RequestParam("fileStatus") @Min(0) @Max(2) int fileStatus
    ){
        String uuId = (String) StpUtil.getLoginId();
        try {
            int update = fileInfoService.getBaseMapper().update(null, new UpdateWrapper<FileInfo>()
                    .set("file_status", fileStatus)
                    .eq("file_id", fileId));
            if (update == 1){
                return R.ok();
            }else {
                return R.error();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 根据文件夹id查询文件
     * @param folderId  文件夹id
     * @return          R
     */
    @ApiOperation("根据文件夹id查询文件")
    @PostMapping("/getFileInfoByFolderId")
    public R updateFileStatus(
            @ApiParam(value = "文件id",required = true) @RequestParam("folderId") @NotBlank(message = "文件id") String folderId
    ){
        String uuId = (String) StpUtil.getLoginId();
        try {
            List<FileInfo> fileInfos = fileInfoService.getBaseMapper().selectList(new QueryWrapper<FileInfo>()
                    .eq("file_upload_id", uuId)
                    .eq("file_folder_id", folderId));
            if (fileInfos != null){
                return R.ok(fileInfos);
            }else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }



    /**
     * 获取自己分享的文件
     * @return  R
     */
    @ApiOperation("获取自己分享的文件")
    @PostMapping("/getFileInfoByShare")
    public R getFileInfoByShare(){
        String uuId = (String) StpUtil.getLoginId();
        try {
            List<FileInfo> fileInfos = fileInfoService.getBaseMapper().selectList(new QueryWrapper<FileInfo>()
                    .eq("file_upload_id", uuId)
                    .eq("file_status", Constant.PUBLIC_TYPE));
            if(fileInfos != null){
                return R.ok(fileInfos);
            }else {
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }





}

