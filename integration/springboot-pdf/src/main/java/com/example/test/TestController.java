package com.example.test;

import com.example.pojo.PdfData;
import com.example.utils.PdfUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestController {

    @Test
    public void testDemo01(){
        PdfData pdfData = new PdfData();
        Field[] fields = pdfData.getClass().getDeclaredFields();
        List<String> values = new LinkedList<>();
        values.add("张三");
        values.add("李四");
        values.add("123");
        values.add("321");
        values.add("北京");
        values.add("洛阳");
        values.add("100");
        values.add("苹果");
        Map<String, String> map = new HashMap<>();
        for(int i = 0;i < fields.length;i++){
            fields[i].setAccessible(true);
            try {
                System.out.println("属性名："+fields[i].getName()+values.get(i));
                map.put(fields[i].getName(),values.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GenerateContract.wordsFillTemplate(map);
    }
}
