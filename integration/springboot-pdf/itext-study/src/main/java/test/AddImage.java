package test;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by 成大事
 * Date:2021-12-08
 * Time 20:02
 */
public class AddImage {
    public static void main(String[] args) throws Exception{
        // 模板路径
        String templatePath = "E:\\test\\pdf\\购买协议合同书.pdf";

        // 生成的新文件路径
        String newPdfPath = "E:\\test\\pdf\\hhhh.pdf";

        //图片地址
        String imagePath = "E:\\test\\pdf\\1640870810998.jpg";

        String fieldName = "buyerSign";

        //读取模板文件
        FileInputStream inputStream = new FileInputStream(templatePath);
        PdfReader reader = new PdfReader(inputStream);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(newPdfPath));

        //提取pdf中的表单
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        
        //通过域名获取所在页的和坐标，左下角为起点
        int pageNo = form.getFieldPositions(fieldName).get(0).page;
        Rectangle position = form.getFieldPositions(fieldName).get(0).position;
        float x = position.getLeft();
        float y = position.getBottom();
        System.out.println("x:"+x+" y:"+y);

        //读图片
        Image image = Image.getInstance(imagePath);
        //获取操作的页面
        PdfContentByte content = stamper.getOverContent(pageNo);
        //获取域的大小缩放图片
        image.scaleToFit(position.getWidth(),position.getHeight());
        System.out.println("width:"+position.getWidth()+" height:"+position.getHeight());
        //添加图片
        image.setAbsolutePosition(x ,y+10);
        content.addImage(image);

        stamper.close();
        reader.close();


    }
}
