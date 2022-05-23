package com.example.test;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class addImage {
    public static void main(String[] args) throws Exception {
        // 模板文件路径
        String templatePath = "E:\\test\\pdf\\1640871736384.pdf";
        // 生成的文件路径
        String targetPath = "E:\\test\\pdf\\imgTest.pdf";
        // 书签名
        String fieldName = "buyerSign";
        // 图片路径
        String imagePath = "E:\\test\\pdf\\111.png";

        // 读取模板文件
        InputStream input = new FileInputStream(new File(templatePath));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
        // 提取pdf中的表单
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));

        // 通过域名获取所在页和坐标，左下角为起点
        int pageNo = form.getFieldPositions(fieldName).get(0).page;
        Rectangle signRect = form.getFieldPositions(fieldName).get(0).position;
        float x = signRect.getLeft();
        float y = signRect.getBottom();
        System.out.println("pageNo:"+pageNo);
        System.out.println("x:"+x);
        System.out.println("y:"+y);

        // 读图片
        Image image = Image.getInstance(imagePath);
        // 获取操作的页面
        PdfContentByte under = stamper.getOverContent(pageNo);
        // 根据域的大小缩放图片
        image.scaleToFit(signRect.getWidth(), signRect.getHeight()+20);
        System.out.println("signRect.getWidth:"+signRect.getWidth());
        System.out.println("signRect.getHeight:"+signRect.getHeight());
        // 添加图片
        image.setAbsolutePosition(x,y-10);
        under.addImage(image);

        stamper.close();
        reader.close();
    }
}
