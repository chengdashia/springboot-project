package com.nongXingGang.utils.result;

/**
 * @author 成大事
 * @date 2022/3/10 9:29
 */
public class CodeType {

    //常规的成功
    public static final int SUCCESS = 200;

    //常规的失败
    public static final int FAILED = 501;

    //数据库的报错
    public static final int SQL_ERROR = 502;

    //未知的报错
    public static final int UNKNOWN_ERROR = 503;

    //不存在
    public static final int NOT_EXISTS = 504;

    //文件上传失败
    public static final int FILE_UPLOAD_ERROR = 505;

    //验证码不正确
    public static final int CAPTCHA_ERROR = 506;

    //验证码过期了
    public static final int CAPTCHA_EXPIRE = 507;


    //身份信息有误
    public static final int ID_INFO_ERROR = 508;

    //身份证图片有误
    public static final int ID_CARD_ERROR = 509;

    //图片格式问题
    public static final int FILE_TYPE_ERROR = 510;



    //密码错误
    public static final int PWD_ERROR = 201;

    //用户存在
    public static final int ID_EXISTS = 202;

    //用户不存在
    public static final int ID_NOT_EXISTS = 203;

    //请耐心等待,不要重复发送
    public static final int PHONE_EXISTS = 204;

    //两次密码不正确
    public static final int TWICE_PWD_ERROR = 205;



}
