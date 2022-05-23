package com.example.controller;

import com.example.utils.FileTypeUtils;
import com.example.utils.ImageUtil;
import com.example.utils.MultipartFileToFileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {


    @RequestMapping("/upload/idCard")
    public Map<String, String> toString(MultipartFile fileUpload) {
        return upload(fileUpload);
    }

    public Map<String, String> upload(MultipartFile fileUpload) {

        Map<String, String> map = new HashMap<>();

        File file = null;
        /**
         * 先将MultipartFile 转为 file 文件 然后判断格式
         */
        try {
            file = MultipartFileToFileUtils.multipartFileToFile(fileUpload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 如果 file 不为 null  则获取其格式
         */
        if (file != null) {
            //获取文件格式
            FileTypeUtils fileTypeUtils = new FileTypeUtils();
            String fileByFile = fileTypeUtils.getFileByFile(file);

            if (fileByFile != null) {

                //获取图片格式
                String imageFileType = fileTypeUtils.getImageFileType(file);
                System.out.println("图片格式："+imageFileType);

                String dateFormat = "yyyy年M月d日H时m秒s毫秒";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat);
                String time = format.format(new Date());

                //生成文件名
                String fileName = "token-openid" + time + "." + imageFileType;

                //指定本地文件夹存储图片，写到需要保存的目录下
                String folderPath = "E:\\test\\" + "token-openid222";
                File folder = new File(folderPath);

                if (!folder.exists()) {
                    folder.mkdir();
                }

                String filePath = folderPath + "\\";
                try {
//                    fileUpload.transferTo(new File(filePath+fileName));

                    ImageUtil.generateThumbnail2Directory(filePath, String.valueOf(file));
                    //返回提示信息
                    map.put("status", "0");
                    map.put("msg", "上传成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("status", "1");
                    map.put("msg", "上传失败");
                }
            }else{
                map.put("status", "-2");
                map.put("msg", "图片格式不对");
            }
        } else {
            map.put("status", "-1");
            map.put("msg", "文件格式不对");
        }

        return map;
    }
}