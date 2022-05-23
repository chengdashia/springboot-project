package com.onlineExam.utils.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 成大事
 * @date 2022/3/10 9:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MsgType对象", description="")
public class MsgType {

    //成功
    public static final String SUCCESS = "Success";

    //成功
    public static final String FAILED = "Failed";


    //数据库有问题
    public static final String SQL_ERROR = "数据库出问题了";

    //未知错误
    public static final String UNKNOWN_ERROR = "未知错误";

    //用户认证过了
    public static final String IS_CERTIFICATION = "用户认证过了";

    //图片上传问题
    public static final String FILE_UPLOAD_ERROR = "图片上传问题";

    //图片上传问题
    public static final String PWD_ERROR = "密码错误！";

    //用户不存在
    public static final String USER_NOT_EXISTS = "请先检查账号密码是否有误！或先申请添加信息！！";

    //用户已存在，请不要重复注册
    public static final String USER_EXISTS = "用户已存在，请不要重复注册！";

    //不存在
    public static final String NOT_EXISTS = "不存在！";

    //验证码不正确
    public static final String CAPTCHA_ERROR = "验证码不正确！";

    //验证码过期了
    public static final String CAPTCHA_EXPIRE = "验证码过期了!";


    //请耐心等待,不要重复发送
    public static final String PHONE_EXISTS = "请耐心等待,不要重复发送";


    //两次密码不正确
    public static final String TWICE_PWD_ERROR = "两次密码不正确!";


    //图片格式问题
    public static final String FILE_TYPE_ERROR = "图片格式问题！";


    //班级或学院选择错误
    public static final String CHOOSE_ERROR = "班级或学院选择错误！";


    //计算错误
    public static final String CALCULATE_ERROR = "计算错误！";







}
