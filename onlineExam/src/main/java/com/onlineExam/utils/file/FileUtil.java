package com.onlineExam.utils.file;

import com.onlineExam.utils.result.MsgType;
import com.onlineExam.utils.result.StatusType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static Map<String, Object> saveImgFile(MultipartFile file , String openid , boolean isThumbnail){
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()){
            map.put("status", StatusType.ERROR);
            map.put("msg","未选择文件");
        }else{
            //判断file 是不是图片 如果是则获取图片格式 如果不是，则返回前端让其重新上传
            if(isImage(file)){
                //获取图片mine/type格式
                String mimeType = getType(file);

                //获取图片后缀
                String suffix = FileTypeUtil.FILE_TYPE_MAP.get(mimeType);

                //指定文件夹。如果不存在就创建
                String folderPath = "/home/img/";

//                String folderPath = "E:\\test\\"+openid+"\\";

                File temp = new File(folderPath);
                if (!temp.exists()){
                    temp.mkdir();
                }

                //获得时间戳
                long time = System.currentTimeMillis();

                //生成文件名
                String fileName = openid + time + suffix;

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
                    map.put("status",StatusType.FILE_UPLOAD_ERROR);
                    map.put("msg","上传失败");
                }
                map.put("status",StatusType.SUCCESS);
                map.put("filePath", "http://114.215.83.227/img/"+fileName);
//                map.put("filePath", "http://10.11.43.55"+finalFilePath);
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
    public static Map<String, Object> saveFile(MultipartFile file , String openid){
        Map<String, Object> map = new HashMap<>();
        //如果文件没有
        if (file.isEmpty()){
            map.put("status",StatusType.ERROR);
            map.put("msg","未选择文件");
            return map;
        }else{
            //获取文件格式
            String mineType = getType(file);

            System.out.println("mineType:  "+mineType);

            //获取文件后缀
            String suffix = FileTypeUtil.FILE_TYPE_MAP.get(mineType);

            //指定文件夹。如果不存在就创建
//            String folderPath = "E:\\test\\"+openid+"\\";
//            String folderPath = "/index/template/";
            String folderPath = "/home/file/";

            File temp = new File(folderPath);
            if (!temp.exists()){
                temp.mkdir();
            }

            String substring = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
            System.out.println(substring);

            //生成文件名
            String fileName = substring +System.currentTimeMillis()+ suffix;

            //文件路径
            File filePath = new File(folderPath+fileName);
            try {

//                file.transferTo(filePath); //把上传的文件保存至本地
                InputStream sis = file.getInputStream();
                FileOutputStream fos = new FileOutputStream(filePath);
                byte[] content = new byte[1024];
                int len = 0;
                while((len=sis.read(content)) > -1) {
                    fos.write(content, 0, len);
                }
                fos.flush();


                map.put("status", StatusType.SUCCESS);
                map.put("filePath",filePath);
//                System.out.println("fielutil 中的filePath  "+filePath);
                return map;

            }catch (IOException e){
                e.printStackTrace();
                map.put("status", StatusType.FILE_UPLOAD_ERROR);
                isDelete(folderPath);
                return map;
            }
        }
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
        File multipartFileToFile = null;
        try {
            multipartFileToFile = MultipartFileToFile.multipartFileToFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mimeType = FileTypeJudge.getMimeType(multipartFileToFile);
        MultipartFileToFile.delteTempFile(multipartFileToFile);
        return mimeType;
    }

}
