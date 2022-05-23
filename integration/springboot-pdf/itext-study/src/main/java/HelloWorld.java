import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by 成大事
 * Date:2021-12-08
 * Time 17:05
 */
public class HelloWorld {
    public static void main(String[] args)  throws FileNotFoundException, DocumentException {
        //创建文件
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter.getInstance(document,new FileOutputStream("E:/test/pdf/helloWord.pdf"));

        // 3.打开文档
        document.open();

        // 4.添加一个内容段落
        document.add(new Paragraph("Hello world!"));

        // 5.关闭文档
        document.close();
    }
}
