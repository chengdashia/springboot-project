import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by 成大事
 * Date:2021-12-08
 * Time 17:19
 */
public class AddIamge {
    public static void main(String[] args) throws DocumentException, IOException {

        //创建文件
        Document document = new Document();
        //建立一个书写器
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:/test/pdf/addImage1.pdf"));
        //打开文件
        document.open();
        //添加内容
        document.add(new Paragraph("HD content here"));

        //图片1
        Image image1 = Image.getInstance("D:/file/total/壁纸/1.jpg");
        //设置图片位置的x轴和y轴
        image1.setAbsolutePosition(100, 700);
        //设置图片的宽度和高度
        image1.scaleAbsolute(50, 50);
        //将图片1添加到pdf文件中
        document.add(image1);

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
    }
}
