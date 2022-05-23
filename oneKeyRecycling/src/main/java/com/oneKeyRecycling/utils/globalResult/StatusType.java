package com.oneKeyRecycling.utils.globalResult;

/**
 * @author 成大事
 * @date 2022/3/10 9:59
 */
public class StatusType {

    //成功
    public static final int SUCCESS = 1;

    //失败
    public static final int ERROR = 0;

    //密码错误
    public static final int PWD_ERROR = -1;

    //数据查询失败失败
    public static final int SQL_ERROR = -2;

    //不存在
    public static final int NOT_EXISTS = -3;

    //不存在 但是注册了
    public static final int NOT_EXISTS_REGISTER = -4;

    //身份信息错误
    public static final int ID_INFO_ERROR = -5;

    //身份证图片错误
    public static final int ID_CARD_ERROR = -6;

    //没做更改
    public static final int NOT_CHANGE = -7;

    //图片上传问题
    public static final int FILE_UPLOAD_ERROR = -5;


    //图片格式问题
    public static final int FILE_TYPE_ERROR = -6;

    //图片上传成功
    public static final int FILE_UPLOAD_SUCCESS = 1;





}
