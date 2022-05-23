package com.example.utils;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;

import java.io.File;

/**
 * @author 成大事
 * @date 2021/12/31 14:51
 * 添加签名
 */
public class AddAutograph {


    /**
     * 增加买家（甲方）的签名图片
     * @param imagePath           签名图片地址
     * @param templatePdfPath     模板地址
     */
    public static void AddSignA(String imagePath, String templatePdfPath) {
        //创建文档
        PdfDocument pdf = new PdfDocument();

        //
        pdf.loadFromFile(templatePdfPath);

        //添加一个空白页，目的为了删除jar包添加的水印，后面再移除这一页
        pdf.getPages().add();

        //获取第一页
        PdfPageBase page = pdf.getPages().get(0);

        //加载图片，并获取图片高宽
        PdfImage image = PdfImage.fromFile(imagePath);
        int width = image.getWidth()/2;
        int height = image.getHeight()/2;

        //绘制图片到PDF
        page.getCanvas().drawImage(image,160,710,width / 6, height / 6);

        //移除第一个页
        pdf.getPages().remove(pdf.getPages().get(pdf.getPages().getCount()-1));

        //删除签名的图片
        File file = new File(imagePath);
        file.delete();
        //保存文档
        pdf.saveToFile(templatePdfPath);
        pdf.dispose();
    }

    /**
     * 增加卖家（乙方）的签名图片
     * @param imagePath           签名图片地址
     * @param templatePdfPath     模板地址
     */
    public static void AddSignB(String imagePath, String templatePdfPath) {
        //创建文档
        PdfDocument pdf = new PdfDocument();

        //
        pdf.loadFromFile(templatePdfPath);

        //添加一个空白页，目的为了删除jar包添加的水印，后面再移除这一页
        pdf.getPages().add();

        //获取第一页
        PdfPageBase page = pdf.getPages().get(0);

        //加载图片，并获取图片高宽
        PdfImage image = PdfImage.fromFile(imagePath);
        int width = image.getWidth()/2;
        int height = image.getHeight()/2;

        //绘制图片到PDF
        page.getCanvas().drawImage(image,440,710,width / 6, height / 6);

        //移除第一个页
        pdf.getPages().remove(pdf.getPages().get(pdf.getPages().getCount()-1));
        File file = new File(imagePath);
        file.delete();


        //保存文档
        pdf.saveToFile(templatePdfPath);
        pdf.dispose();
    }
}
