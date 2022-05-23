package com.oneKeyRecycling.controller;

import com.oneKeyRecycling.utils.file.FileUtil;
import com.oneKeyRecycling.utils.globalResult.R;
import com.oneKeyRecycling.utils.globalResult.StatusType;
import com.oneKeyRecycling.utils.redis.RedisUtil;
import com.oneKeyRecycling.utils.safe.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这整个文件是管理上传的
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 上传身份证照片
     *
     * @param request        可以获取token
     * @param idFrontImgFile 身份证前面的照片
     * @param idBackImgFile  身份证后面的照片
     * @return R
     */
    @ApiOperation("上传身份证图片 一次两张")
    @PostMapping("/idUpload")
    public R uploadId(HttpServletRequest request,
                      @RequestParam("idFrontImg") MultipartFile idFrontImgFile,
                      @RequestParam("idBackImg") MultipartFile idBackImgFile) {
        String openid = JWTUtil.getUUId(request);
        String idFrontImgPath = "";
        String idBackImgPath = "";
        //isThumbnail  是否压缩
        Map<String, Object> frontMap = FileUtil.saveImgFile(idFrontImgFile, openid, false);
        if ("1".equals(frontMap.get("status"))) {
            Map<String, Object> backMap = FileUtil.saveImgFile(idBackImgFile, openid, false);
            idFrontImgPath = String.valueOf(frontMap.get("filePath"));
            if ("1".equals(backMap.get("status"))) {
                idBackImgPath = String.valueOf(backMap.get("filePath"));
                HashMap<String, String> resultMap = new HashMap<>();
                resultMap.put("idFrontPath", idFrontImgPath);
                resultMap.put("idBackPath", idBackImgPath);
                return R.success(request);
            }
        }
        return R.failure(frontMap);

    }


    /**
     * @param request 获取token
     * @param file    文件名
     * @return R
     */
    @ApiOperation("上传身份证图片 一次一张")
    @PostMapping("/idImgUpload")
    public R uploadLegal(HttpServletRequest request,
                         @ApiParam("身份证图片文件") @RequestParam("file") MultipartFile file) {
        String openid = JWTUtil.getUUId(request);
        Map<String, Object> map = FileUtil.saveImgFile(file, openid, false);
        if (StatusType.SUCCESS == (int) map.get("status")) {
            return R.success(map);
        }
        return R.failure(map);
    }


}