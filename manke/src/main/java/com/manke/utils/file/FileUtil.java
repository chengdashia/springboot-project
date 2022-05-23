package com.manke.utils.file;

import com.manke.utils.globalResult.MsgType;
import com.manke.utils.globalResult.StatusType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 成大事
 * @date 2021-12-20
 */
public class FileUtil {


    /**
     * 保存图片到指定文件夹
     * @param file 文件
     * @return Map
     */
    public static Map<String, Object> saveImgFile(MultipartFile file , String id, boolean isThumbnail){
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()){
            map.put("status",StatusType.ERROR);
            map.put("msg","未选择文件");
        }else{
            //判断file 是不是图片 如果是则获取图片格式 如果不是，则返回前端让其重新上传
            if(isImage(file)){
                String mimeType = getType(file);


                 // 获取图片格式
                String imageFormat = FileTypes.FILE_TYPE_MAP.get(mimeType);


                //指定文件夹。如果不存在就创建
                String folderPath = "E:\\test\\"+ id +"\\";
                File temp = new File(folderPath);
                if (!temp.exists()){
                    temp.mkdir();
                }


                //获得时间戳
                long time = System.currentTimeMillis();

                //生成文件名
                String fileName = id + time + "." + imageFormat;

                //文件路径
                File filePath = new File(folderPath+fileName);

                String finalFilePath = null;
                try {
                    file.transferTo(filePath); //把上传的文件保存至本地
                    if(isThumbnail){//是否压缩
                        List<String> filePathThumbnails = ThumbnailUtil.generateThumbnail2Directory(folderPath, String.valueOf(filePath));
                        //压缩之后，删除原图片
                        isDelete(String.valueOf(filePath));
                        finalFilePath = filePathThumbnails.get(0);
                    }else {
                        finalFilePath = String.valueOf(filePath);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                    map.put("status", StatusType.FILE_UPLOAD_ERROR);
                    map.put("msg","上传失败");
                }
                map.put("status",StatusType.SUCCESS);
                map.put("filePath", finalFilePath);
            }else{
                map.put("status",StatusType.FILE_TYPE_ERROR);
                map.put("msg", MsgType.FILE_TYPE_ERROR);
            }
        }
        return map;
    }


    /**
     * 保存文件到指定文件夹
     * @param file 文件
     * @return Map
     */
    public static Map<String, Object> saveFile(MultipartFile file , String id){
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()){
            map.put("status",StatusType.ERROR);
            map.put("msg","未选择文件");
        }else{
            //获取文件格式
            String mimeType = getType(file);

            //指定文件夹。如果不存在就创建
            String folderPath = "E:\\test\\"+ id +"\\";

            File temp = new File(folderPath);
            if (!temp.exists()){
                temp.mkdir();
            }

            //获得时间戳
            long time = System.currentTimeMillis();

            //生成文件名
            String fileName = id + time + "." + mimeType;

            //文件路径
            File filePath = new File(folderPath+fileName);
            try {
                file.transferTo(filePath); //把上传的文件保存至本地
                map.put("status", StatusType.SUCCESS);
                map.put("filePath",filePath);
            }catch (IOException e){
                e.printStackTrace();
                map.put("status", StatusType.FILE_UPLOAD_ERROR);
                isDelete(folderPath);
            }
        }
        return map;
    }



    /**
     * 目前先写成将文件存储之后再删除
     * @param filePath   文件路径
     * @return      String
     */
    public static boolean isDelete(String filePath){
        return new File(filePath).delete();
    }

    /**
     * 判断是否图片
     * @param file  文件
     * @return   String
     */
    public static boolean isImage(MultipartFile file){
        return FileTypeJudge.isImage(file);
    }

    /**
     * 获得文件类型
     * @param file  文件
     * @return String
     */
    public static String getType(MultipartFile file){
        return FileTypeJudge.getMimeType(file);
    }


}