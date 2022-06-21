package com.example.springboot.controller;

import com.example.springboot.common.access.AccessLimit;
import com.example.springboot.common.minio.MinioUtil;
import com.example.springboot.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 成大事
 * @since 2022/5/29 10:36
 */

@Api(tags = "测试")
@Validated
@RestController
@Slf4j
public class TestController {


    @Resource
    private MinioUtil minioUtil;

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public R upload(MultipartFile file, @RequestParam(value = "bucketName") String bucketName) {
        if (StringUtils.isBlank(bucketName)) {
            log.error("存储bucket名称为空，无法上传");
            return R.error("存储bucket名称为空，无法上传");
        }
        if (!minioUtil.upload(file, bucketName)) {
            log.error("文件上传异常");
            return R.error("文件上传异常");
        }
        return R.ok("文件上传成功");
    }


    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public void download(HttpServletResponse response,
                         @RequestParam(value = "bucketName") String bucketName,
                         @RequestParam(value = "fileName") String fileName
    ) {
        minioUtil.download("file",fileName,response);
    }

    @ApiOperation("测试")
    @GetMapping("/test")
    public R test(){
        for (int i = 0; i < 10000; i++) {
            log.info("这是log4j2的日志测试，info级别");
            log.warn("这是log4j2的日志测试，warn级别");
            log.error("这是log4j2的日志测试，error级别");

            //如果在日志输出中想携带参数的化，可以这样设置
            String bug = "约翰·冯·诺依曼 保佑，永无BUG";
            log.info("这是带参数的输出格式:{}",bug);
        }

       return R.error("错误");
    }


    @GetMapping("/index")
    @AccessLimit(seconds = 1, maxCount = 3) //1秒内 允许请求3次
    public R getImageList(){
        return R.ok();
    }



}
