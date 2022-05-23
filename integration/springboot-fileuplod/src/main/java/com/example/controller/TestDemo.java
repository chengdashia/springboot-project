package com.example.controller;

import com.example.utils.FileTypeUtils;
import org.junit.Test;

import java.io.File;

public class TestDemo {

    @Test
    public void test2() {
        FileTypeUtils fileTypeTest = new FileTypeUtils();
        File file = new File("D:\\file\\total\\壁纸\\222.doc");
        if(file.exists()){
            System.out.println(fileTypeTest.getImageFileType(file));
            System.out.println(fileTypeTest.getFileByFile(file));
        }
    }
}
