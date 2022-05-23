package com.example.springbootspire.test;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;

/**
 * @author 成大事
 * @date 2021/12/31 14:51
 */
public class AddImage {
    public static void main(String[] args) {
        //创建文档
        PdfDocument pdf = new PdfDocument();
        //
        pdf.loadFromFile("E:\\test\\pdf\\1640935378423.pdf");

        //添加一个空白页，目的为了删除jar包添加的水印，后面再移除这一页
        pdf.getPages().add();

        //获取第一页
        PdfPageBase page = pdf.getPages().get(0);

        //加载图片，并获取图片高宽
        PdfImage image = PdfImage.fromFile("E:\\test\\pdf\\1640935384450.jpg");
        int width = image.getWidth()/2;
        int height = image.getHeight()/2;

        //绘制图片到PDF
        page.getCanvas().drawImage(image,160,710,width / 6, height / 6);

        //移除第一个页
        pdf.getPages().remove(pdf.getPages().get(pdf.getPages().getCount()-1));

        //保存文档
        pdf.saveToFile("E:\\test\\pdf\\1640935378423.pdf");
        pdf.dispose();
    }
}
