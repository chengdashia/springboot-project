package com.nongXingGang.controller;
import cn.dev33.satoken.stp.StpUtil;
import com.nongXingGang.utils.file.FileUtil;
import com.nongXingGang.utils.redis.RedisUtil;
import com.nongXingGang.utils.result.CodeType;
import com.nongXingGang.utils.result.MsgType;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import com.nongXingGang.utils.safe.JWTUtil;
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
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UploadController {

    private final RedisUtil redisUtil;

    /**
     * 上传身份证照片
     *
     * @param request        可以获取token
     * @param idFrontImgFile 身份证前面的照片
     * @param idBackImgFile  身份证后面的照片
     * @return R
     */
    @ApiOperation("身份证照片上传，可以一次上传正面和反面")
    @PostMapping("/idUpload")
    public R uploadId(HttpServletRequest request,
                      @RequestParam("idFrontImg") MultipartFile idFrontImgFile,
                      @RequestParam("idBackImg") MultipartFile idBackImgFile) {
        String openid = JWTUtil.getOpenid(request);
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
                return R.ok(request);
            }
        }
        return R.error(frontMap);

    }


    /**
     * @param request 获取token
     * @param files    文件名
     * @return R
     */
    @ApiOperation("身份证照片上传，一次一张")
    @PostMapping("/idImgUpload")
    public R idImgUpload(HttpServletRequest request,
                         @ApiParam("身份证图片文件") @RequestParam("files") MultipartFile[] files) {
        String openid = JWTUtil.getOpenid(request);
        List<String> list = new ArrayList<>(files.length);
        for (MultipartFile f : files) {
            Map<String, Object> map = FileUtil.saveImgFile(f,openid,false);
            int status = (int) map.get("status");
            if (StatusType.SUCCESS == status) {
                String filePath = (String) map.get("filePath");
                list.add(filePath);
                return R.ok(list);
            } else {
                return R.fileUploadError();
            }
        }
        return R.error();
    }



    /**
     * @param files    文件名
     * @return R
     */
    @ApiOperation("图片上传")
    @PostMapping("/imgUpload")
    public R imgUpload(@ApiParam("图片文件") @RequestParam("files") MultipartFile[] files) {

        log.info("图片上传的：  openid:  "+StpUtil.getTokenInfo().getTokenValue());

        String openid = (String) StpUtil.getLoginId();
        log.info("上传文件的：  openid:  "+openid);
        List<String> list = new ArrayList<>(files.length);
        for (MultipartFile f : files) {
            Map<String, Object> map = FileUtil.saveImgFile(f,openid,true);
            Object status = map.get("status");
            if (status.equals(StatusType.SUCCESS)) {
                String filePath = (String) map.get("filePath");
                list.add(filePath);
                return R.ok(list);
            } else {
                return R.fileUploadError();
            }
        }
        return R.error();
    }


    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public R uploadFiles(HttpServletRequest request,
                         @RequestParam("file") MultipartFile[] files) {
        String id = JWTUtil.getOpenid(request);
        List<String> list = new ArrayList<>(files.length);
        for (MultipartFile f : files) {
            Map<String, Object> map = FileUtil.saveFile(f,id);
            int status = (int) map.get("status");
            if (StatusType.SUCCESS == status) {
                String filePath = (String) map.get("filePath");
                list.add(filePath);

            } else {
                return new R(CodeType.FILE_UPLOAD_ERROR, MsgType.FILE_UPLOAD_ERROR, null);
            }
        }
        return new R(CodeType.SUCCESS, MsgType.SUCCESS, list);
    }

}