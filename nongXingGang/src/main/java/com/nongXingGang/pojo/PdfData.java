package com.nongXingGang.pojo;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PdfData {

    //甲方
    private String partA;

    //乙方
    private String partB;

    //种类
    private String type;

    //品种
    private String varieties;

    //数量
    private String nums;

    //单价
    private String price;

    //总价
    private String totalPrice;

    //总的
    private String total;

    //甲方地址
    private String partAAddress;

    //乙方地址
    private String partBAddress;

    //甲方的联系电话
    private String partAPhone;

    //乙方的联系电话、
    private String partBPhone;

    //年
    private String year;

    //月
    private String month;

    //日
    private String day;



}