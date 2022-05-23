package com.example.utils.fileUtils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {

    /**
     * 保存图片到指定文件夹
     * @param file
     * @return
     */
    public static Map<String, String> saveFile(MultipartFile file ,String token ,boolean isThumbnail){
        Map<String, String> map = new HashMap<>();

        String mimeType2 = FileTypeJudge.getMimeType(file);
        System.out.println("mimeType: "+mimeType2);

        if (file.isEmpty()){
            map.put("status","0");
            map.put("msg","未选择文件");
        }else{
            //判断file 是不是图片 如果是则获取图片格式 如果不是，则返回前端让其重新上传
            if(isImage(file)){
                String mimeType = getType(file);
//                System.out.println("mimeType: "+mimeType);

                /**
                 * 获取图片格式
                 */
                String imageFormat = ImageTypeUtil.FILE_TYPE_MAP.get(mimeType);

                /**
                 * 指定文件夹。如果不存在就创建
                 */
                String folderPath = "E:\\test\\"+token+"\\";
                File temp = new File(folderPath);
                if (!temp.exists()){
                    temp.mkdirs();
                }

                long time = System.currentTimeMillis();

                //生成文件名
                String fileName = "token-openid" + time + "." + imageFormat;

                File filePath = new File(folderPath+fileName);
                try {
                    file.transferTo(filePath); //把上传的文件保存至本地
                    if(isThumbnail){
                        ThumbnailUtil.generateThumbnail2Directory(folderPath, String.valueOf(filePath));
                        isDelete(String.valueOf(filePath));
                    }
                    System.out.println(fileName+" 上传成功");
                }catch (IOException e){
                    e.printStackTrace();
                    map.put("status","1");
                    map.put("msg","上传失败");
                }
                map.put("status","2");
                map.put("msg","上传成功！");
            }else{
                map.put("status","3");
                map.put("msg","文件格式不对！");
            }
        }
        return map;
    }

    /**
     * 目前先写成将文件存储之后再删除
     * @param filePath
     * @return
     */
    public static boolean isDelete(String filePath){
        return new File(filePath).delete();
    }

    public static boolean isImage(MultipartFile file){
        return FileTypeJudge.isImage(file);
    }

    public static String getType(MultipartFile file){
        return FileTypeJudge.getMimeType(file);
    }


}
