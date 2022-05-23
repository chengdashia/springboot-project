package com.example.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64Utils {

    /**
     * 将图片文件转成字节数组，并对其进行Base64编码处理
     * @param imgPath 图片文件路径
     * @return  编码后的字符串
     */
    public static String GetImageEncode(String imgPath){
        InputStream in = null;
        byte[] bytes ;
        String encode = null; //返回Base64 编码的字节数组字符串
        BASE64Encoder encoder = new BASE64Encoder();   //对字节数组Base64编码
        try{
            //读取图片字节数组
            in = new FileInputStream(imgPath);
            bytes = new byte[in.available()];
            in.read(bytes);
            encode = encoder.encode(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return encode;
    }


    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param base64Str 前端传过来的Base64编码的json数据
     * @return boolean
     */
    public static String GenerateImage(String base64Str) throws IOException {
        if ("".equals(base64Str)){
            // 图像数据为空
            return null;
        }
//        base64Str = base64Str.replace("data:image/png;base64,", "");

        String type = base64Str.substring(base64Str.indexOf("/") + 1,base64Str.indexOf(";"));
        base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        String imgFilePath;
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成png图片
            imgFilePath = Constants.tempFolder + System.currentTimeMillis()+"."+type;
            out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            System.out.println(imgFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(out != null){
                out.flush();
                out.close();
            }
        }
        return imgFilePath;
    }
}
