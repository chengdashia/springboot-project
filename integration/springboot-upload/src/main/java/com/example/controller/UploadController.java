package com.example.controller;

import com.example.utils.fileUtils.ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/upload/single")
    public Object upload(@RequestParam("file")MultipartFile file){
        String token = "openid-2319";
        return ImageUtil.saveFile(file,token,true);
    }

    @PostMapping("/multiUpload")
    public Object multiUpload(@RequestParam("file")MultipartFile[] files){
        String token = "openid-2319";
        System.out.println("文件的个数:"+files.length);
        for (MultipartFile f : files){
            ImageUtil.saveFile(f,token,false);
        }
        return "ok";
    }




}
