package com.example.springboot.utils.result;

/**
 * @author 成大事
 * @date 2022/3/15 10:29
 */
public class Constants {

    //买家
    public static final int BUYER = 0;

    //卖家
    public static final int SELLER = 1;

    //商品
    public static final int GOODS = 1;

    //需求
    public static final int DEMAND = 0;


    //在售商品
    public static final int ON_GOODS = 0;

    //预售商品
    public static final int PRE_GOODS = 1;

    //需求
    public static final int DEMANDS = 2;





    //合作社
    public static final int COOP = 2;

    //超管
    public static final int ADMIN = 3;

    //未认证
    public static final int NOT_INITIALIZE = -1;

    //男
    public static final int MAN = 1;

    //女
    public static final int FEMALE = 0;


    //认证
    public static final int INITIALIZE = 1;


    //逻辑删除
    public static final int IS_DEL = 0;

    //逻辑删除  存在
    public static final int NOT_DEL = 1;


    //待签字
    public static final int TO_BE_SIGNED = 1;


    //已签字
    public static final int SIGNED = 2;

    //已收藏
    public static final int STORED = 1;

    //未收藏
    public static final int NOT_STORE = 0;

    //拒绝签字
    public static final int REFUSE_SIGNED = 3;


    //交易成功
    public static final int TRADE_SUCCESS = 4;


    //地址
    public static final String TEMP_Folder = "E:\\test\\pdf\\";

    /**
     * 合同模板的位置
     */
    public static final String CONTRACT_FilePath = "E:\\test\\pdf\\农产品购销合同.pdf";
}
