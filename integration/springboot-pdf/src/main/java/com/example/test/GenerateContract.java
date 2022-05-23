package com.example.test;


import com.example.pojo.PdfForm;
import com.example.utils.Constants;
import com.example.utils.ImageUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 生成合同 的工具类
 */
public class GenerateContract {

    public static String wordsFillTemplate(Map<String, String> pdfWordMap) {


        // 生成的新文件路径
        String newPdfPath = Constants.tempFolder + System.currentTimeMillis() + ".pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPdfPath);
            reader = new PdfReader(Constants.contractFilePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            //获取有哪些字段
            AcroFields form = stamper.getAcroFields();
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            //添加替换字体
            form.addSubstitutionFont(base);
            //处理文字内容
            while (it.hasNext()) {
                String name = it.next();
                form.setField(name, pdfWordMap.get(name));
            }

            //true代表生成的PDF文件不可编辑 sprie.pdf 还是可以编辑的。
            stamper.setFormFlattening(true);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return newPdfPath;

    }


    public static String wordsFillTemplate(PdfForm pdfForm) {

        Map<String, Object> pdfWordMap = convertBeanToMap(pdfForm);
        // 生成的新文件路径
        String newPdfPath = Constants.tempFolder + System.currentTimeMillis() + ".pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPdfPath);
            reader = new PdfReader(Constants.contractFilePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            //获取有哪些字段
            AcroFields form = stamper.getAcroFields();
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
//            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            BaseFont base = BaseFont.createFont("C:\\Users\\Serendipity\\Desktop\\互联网+\\font\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont base = BaseFont.createFont("src/main/resources/static/font/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            //添加替换字体
            form.addSubstitutionFont(base);
            //处理文字内容
            while (it.hasNext()) {
                String name = it.next();
                form.setField(name, String.valueOf(pdfWordMap.get(name)));
            }

            //true代表生成的PDF文件不可编辑 sprie.pdf 还是可以编辑的。
            stamper.setFormFlattening(true);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i );
                copy.addPage(importPage);
            }

            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return newPdfPath;

    }

    /**
     * 买家签名
     * @param imagePath
     */
    public static void signAFillTemplate(String templatePath,String imagePath) throws IOException, DocumentException {
        // 生成的文件路径
        String targetPath = Constants.tempFolder + System.currentTimeMillis() + ".pdf";
        // 书签名
        String fieldName = "buyerSign";

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

        // 读图片
        Image image = Image.getInstance(imagePath);
        // 获取操作的页面
        PdfContentByte under = stamper.getOverContent(pageNo);
        // 根据域的大小缩放图片
        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
        // 添加图片
        image.setAbsolutePosition(x, y);
        under.addImage(image);

        stamper.close();
        reader.close();


        ImageUtil.isDelete(imagePath);
    }

    /**
     * 将对象转成map
     * @param object  对象
     * @return         map
     */
    public static Map<String, Object> convertBeanToMap(Object object)
    {
        if(object == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(object);
                    map.put(key, String.valueOf(value));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }




}
