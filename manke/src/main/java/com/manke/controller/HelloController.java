package com.manke.controller;


import com.manke.utils.file.FileUtil;
import com.manke.utils.globalResult.R;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 成大事
 * @since 2022/3/29 18:11
 */
@RestController
public class HelloController {


    @GetMapping("/hello")
    public R testFileUpload(MultipartFile files){
        String id = "111";
        System.out.println(id);
        FileUtil.saveImgFile(files,id,true);
        return R.success();
    }


    @GetMapping("/hl")
    public R testHello(String s){
        return R.success(s);
    }
}
