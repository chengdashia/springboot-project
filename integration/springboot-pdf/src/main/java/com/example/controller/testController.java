package com.example.controller;

import com.example.pojo.PdfData;
import com.example.test.GenerateContract;
import com.example.utils.AddAutograph;
import com.example.utils.Base64Utils;
import com.example.utils.Constants;
import com.itextpdf.text.DocumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class testController {

    @PostMapping("/Base64Toimg2")
    public Boolean Base64Toimg2(@RequestBody String base64str) throws IOException {
        String s = Base64Utils.GenerateImage(base64str);
        return true;
    }

    /**
     * 下单
     * @param buyerName 买家姓名
     * @param sellerName 卖家姓名
     * @param buyerPhone  买家手机号
     * @param sellerPhone   卖家手机号
     * @param buyerAddress  买家地址
     * @param sellerAddress 卖家地址
     * @param weight  重量
     * @param goodsName 商品名称
     * @param remarks 备注
     * @return
     */
    @PostMapping("/placeOrder")
    public Map<String, String> fillTemplateWords(@RequestParam("buyerName") String buyerName,
                                                 @RequestParam("sellerName") String sellerName,
                                                 @RequestParam("buyerPhone") String buyerPhone,
                                                 @RequestParam("sellerPhone") String sellerPhone,
                                                 @RequestParam("buyerAddress") String buyerAddress,
                                                 @RequestParam("sellerAddress") String sellerAddress,
                                                 @RequestParam("weight") String weight,
                                                 @RequestParam("goodsName") String goodsName,
                                                 @RequestParam("remarks") String remarks) {
        Map<String, String> result = new HashMap<>();

        PdfData pdfData = new PdfData();
        Field[] fields = pdfData.getClass().getDeclaredFields();
        List<String> pdfDatas = new LinkedList<>();
        pdfDatas.add(buyerName);
        pdfDatas.add(sellerName);
        pdfDatas.add(buyerPhone);
        pdfDatas.add(sellerPhone);
        pdfDatas.add(buyerAddress);
        pdfDatas.add(sellerAddress);
        pdfDatas.add(weight);
        pdfDatas.add(goodsName);
        pdfDatas.add(buyerName);
        pdfDatas.add(buyerName);
        Map<String, String> pdfWordMap = new HashMap<>();

        System.out.println(pdfDatas);
        System.out.println(pdfDatas.size());
        System.out.println("remarks:"+remarks);
        //将pdf模板中每个位置与值对应放到map中
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                System.out.println("属性名：" + fields[i].getName() + pdfDatas.get(i));
                pdfWordMap.put(fields[i].getName(), pdfDatas.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Constants.tempNewPath = GenerateContract.wordsFillTemplate(pdfWordMap);

        result.put("status","1");
        result.put("msg","Success");
        return result;
    }



    @PostMapping("/fillTemplateWordsJson")
    public Map<String, String> fillTemplateWordsJson(@RequestBody PdfData pdfData) {
        Map<String, String> map = convertBeanToMap(pdfData);

        for(String key : map.keySet()){
            System.out.println("key:"+key +"  value: "+ map.get(key));
        }
        Map<String, String> result = new HashMap<>();


        Constants.tempNewPath = GenerateContract.wordsFillTemplate(map);

        result.put("status","1");
        result.put("msg","Success");
        return result;
    }


    public static Map<String, String> convertBeanToMap(Object object)
    {
        if(object == null){
            return null;
        }
        Map<String, String> map = new HashMap<>();
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






    //买家签名
    @PostMapping("/signA")
    public boolean addSignA(@RequestBody String base64str) throws DocumentException, IOException {
        System.out.println("base64str  :" +base64str);
        try {
            String imagePath = Base64Utils.GenerateImage(base64str);
            if(imagePath != null){
                String path = "E:\\test\\pdf\\1648609184211.pdf";
                AddAutograph.AddSignA(imagePath,path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    //买家签名
    @PostMapping("/signATest")
    public boolean addSignATest(@RequestBody String base64str) throws DocumentException, IOException {
//        String signA = Constants.tempFolder + System.currentTimeMillis() + ".jpg";

        String signA = "E:\\test\\pdf\\111.png";
        System.out.println("signA:"+signA);
        System.out.println(base64str);

        String path = "E:\\test\\pdf\\1648604916280.pdf";
        AddAutograph.AddSignA(signA,path);

        return true;
    }


    //卖家签名
    @PostMapping("/signB")
    public boolean addSignB(@RequestBody String base64str) throws DocumentException, IOException {
        System.out.println("base64str  :" +base64str);
        try {
            String imagePath = Base64Utils.GenerateImage(base64str);
            if(imagePath != null){
                String path = "E:\\test\\pdf\\1648609184211.pdf";
                AddAutograph.AddSignA(imagePath,path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

//    @PostMapping("/signA")
//    public boolean addSignA(@RequestBody String base64str) throws DocumentException, IOException {
//        String signA = Constants.tempFolder + System.currentTimeMillis() + ".jpg";
//        System.out.println("signA:"+signA);
//        boolean isSuccess = false;
//        try {
//            isSuccess = Base64Utils.GenerateImage(base64str,signA);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (isSuccess){
//            System.out.println("signA----Constants.tempNewPath:"+Constants.tempNewPath);
//            GenerateContract.signAFillTemplate(Constants.tempNewPath,signA);
//
//        }
//        return true;
//    }

}
