package com.cloudDisk.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.pojo.FileHistory;
import com.cloudDisk.service.FileHistoryService;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.redis.RedisUtil;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 成大事
 * @since 2022-04-13
 */
@Api(tags = "浏览记录信息")
@RestController
@RequestMapping("/fileHistory")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileHistoryController {

    private final FileHistoryService fileHistoryService;

    private final RedisUtil redisUtil;


    /**
     * 添加浏览记录
     * @return  R
     */
    @ApiOperation("添加浏览记录")
    @PostMapping("/addMyFileHistoryRedis")
    public R addMyFileHistoryRedis(
            @ApiParam(value = "文件id",required = true) @NotNull @NotBlank(message = "文件Id 不能为空") @RequestParam("fileId") String fileId
    ){

        String uuId = (String) StpUtil.getLoginId();
        String userHistoryList = Constant.getHistoryList(uuId);
        String userHistorySet = Constant.getHistorySet(uuId);
        if(redisUtil.hasKey(userHistoryList)){
            // 获取list中的数据的个数
            long listSize = redisUtil.lGetListSize(userHistorySet);
            //如果set已经没有这个。就插入。
            if(!redisUtil.sHasKey(userHistorySet,fileId)){
                //如果list里面的数量超过了100个。就删除一个。
                if(listSize >= 100){
                    //list 中删除一个早的
                    redisUtil.lRemove(userHistoryList,listSize - 2,listSize - 1);
                    //list 中添加一个新的
                    redisUtil.lPush(userHistoryList,fileId);
                    //set中添加一个新的
                    redisUtil.sSet(userHistorySet,fileId);
                }else {
                    //list 中添加一个新的
                    redisUtil.lPush(userHistoryList,fileId);
                    //set中添加一个新的
                    redisUtil.sSet(userHistorySet,fileId);
                }
            } else {
                //如果set已经有这个了。就不插入
                return R.ok();
            }
        }else {
            List<FileHistory> fileHistoryList = fileHistoryService.getBaseMapper().selectList(new QueryWrapper<FileHistory>()
                    .select("file_id")
                    .eq("user_id", uuId));
            List<String> collect = fileHistoryList.stream().map(FileHistory::getFileId).collect(Collectors.toList());
            for (String s : collect) {
                redisUtil.sSet(userHistorySet,s);
                redisUtil.lPush(userHistoryList,s);
            }
            // 获取list中的数据的个数
            long listSize = redisUtil.lGetListSize(userHistorySet);
            //如果set已经没有这个。就插入。
            if(!redisUtil.sHasKey(userHistorySet,fileId)){
                //如果list里面的数量超过了100个。就删除一个。
                if(listSize >= 100){
                    //list 中删除一个早的
                    redisUtil.lRemove(userHistoryList,listSize - 2,listSize - 1);
                    //list 中添加一个新的
                    redisUtil.lPush(userHistoryList,fileId);
                    //set中添加一个新的
                    redisUtil.sSet(userHistorySet,fileId);
                }else {
                    //list 中添加一个新的
                    redisUtil.lPush(userHistoryList,fileId);
                    //set中添加一个新的
                    redisUtil.sSet(userHistorySet,fileId);
                }
            } else {
                //如果set已经有这个了。就不插入
                return R.ok();
            }
        }
        return R.ok();
    }


    @ApiOperation("添加浏览记录 使用redis")
    @PostMapping("/addMyFileHistory")
    public void addMyFileHistory(
            @ApiParam(value = "文件id",required = true) @NotNull @NotBlank(message = "文件Id 不能为空") @RequestParam("fileId") String fileId
    ){
        String uuId = (String) StpUtil.getLoginId();
        FileHistory fileHistory = new FileHistory();
        fileHistory.setHistoryId(IdUtil.fastUUID());
        fileHistory.setFileId(fileId);
        fileHistory.setUserId(uuId);
        fileHistoryService.save(fileHistory);
    }

    /**
     * 获取自己浏览的文件信息
     * @return  R
     */
    @ApiOperation("获取自己浏览的文件信息")
    @PostMapping("/getMyFileHistory")
    public R getMyFileHistory(
    ){
        // 文件名称   文件备注  收藏时间  文件的id
        String uuId = (String) StpUtil.getLoginId();
        // 查询自己收藏的文件
       Map<String ,Object> map = fileHistoryService.getMyFileHistory(uuId);
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
     * 删除自己浏览的文件信息
     * @return  R
     * @param historyId  文件id
     */
    @ApiOperation("删除自己浏览的文件信息")
    @PostMapping("/delMyFileHistory")
    public R delMyFileHistory(
            @ApiParam(value = "浏览记录的id",required = true) @RequestParam("historyId") @NotBlank(message = "文件的id") @NotNull String historyId
    ){
        //直接删除
        String uuId = (String) StpUtil.getLoginId();
        try {
            boolean remove = fileHistoryService.remove(new QueryWrapper<FileHistory>()
                    .eq("history_id", historyId)
                    .eq("user_id",uuId));
            if (remove){
                return  R.ok();
            }else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }
}

