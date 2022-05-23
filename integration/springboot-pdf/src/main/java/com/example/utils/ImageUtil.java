package com.example.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageUtil {


    /**
     * 目前先写成将文件存储之后再删除
     * @param filePath 文件路径
     * @return  是否删除
     */
    public static boolean isDelete(String filePath){
        return new File(filePath).delete();
    }




}
