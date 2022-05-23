package com.example.springbootspire.test;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.fields.PdfField;
import com.spire.pdf.widget.PdfFormFieldWidgetCollection;
import com.spire.pdf.widget.PdfFormWidget;
import com.spire.pdf.widget.PdfTextBoxFieldWidget;

/**
 * @author 成大事
 * @date 2021/12/31 14:45
 */
public class FillFormField {
    public static void main(String[] args) {
        //创建PdfDocument对象
        PdfDocument doc = new PdfDocument();

        //加载一个测试文档
        doc.loadFromFile("G:\\java-workspace\\Spire.Pdf\\Forms.pdf");

        //获取文档中的域
        PdfFormWidget form = (PdfFormWidget) doc.getForm();

        //获取域控件集合
        PdfFormFieldWidgetCollection forms = form.getFieldsWidget();

        //遍历域控件并填充数据
        for (int i = 0; i < forms.getCount(); i++) {
            PdfField field = forms.get(i);
            if (field instanceof PdfTextBoxFieldWidget) {
                PdfTextBoxFieldWidget textBoxField = (PdfTextBoxFieldWidget) field;
                textBoxField.setText("吴敏");
            }

        }

        //保存文档
        doc.saveToFile("FillFormFields.pdf", FileFormat.PDF);
    }
    }

