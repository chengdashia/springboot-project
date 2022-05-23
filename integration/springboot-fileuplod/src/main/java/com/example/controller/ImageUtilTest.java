package com.example.controller;

import com.example.utils.ImageUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author lnj
 * createTime 2018-10-19 15:39
 **/
public class ImageUtilTest {

    /**
     * 测试在指定目录下生成缩略图
     */
    @Test
    public void testGenerateThumbnail2Directory() throws IOException {
        String path = "E:\\test";
        String[] files = new String[]{
                "D:\\file\\total\\壁纸\\1.jpg",
                "D:\\file\\total\\壁纸\\2.jpg"
        };

        List<String> list = ImageUtil.generateThumbnail2Directory(path, files);
        System.out.println(list);
    }

    /**
     * 将指定目录下的图片生成缩略图
     */
    @Test
    public void testGenerateDirectoryThumbnail() throws IOException {
        String path = "D:\\workspace\\idea\\individual\\springboot-learn\\springboot-thumbnail\\image";
        ImageUtil.generateDirectoryThumbnail(path);
    }
}