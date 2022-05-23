package com.onlineExam.utils.result;

/**
 * @author 成大事
 * @date 2022/3/10 9:59
 */
public class StatusType {
    //类型错误
    public static final int QUESTION_TYPE_ERROR = -1;

    //成功
    public static final int SUCCESS = 1;

    //图片上传成功
    public static final int FILE_UPLOAD_SUCCESS = 1;

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

    //图片上传问题
    public static final int FILE_UPLOAD_ERROR = -5;

    //不存在
    public static final int EXISTS = -6;

    //未知错误
    public static final int UNKNOWN_ERROR = -7;

    //图片格式问题
    public static final int FILE_TYPE_ERROR = -8;

    //已完成
    public static final int IS_COMPLETE = -9;

    //班级或学院选择错误
    public static final int CHOOSE_ERROR = -10;


    //班级或学院选择错误
    public static final int CALCULATE_ERROR = -11;


    //次数已经够了 不能再做了
    public static final int NOT_DO = -12;
















}
