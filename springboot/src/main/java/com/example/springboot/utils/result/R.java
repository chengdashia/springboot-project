package com.example.springboot.utils.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 成大事
 * @date 2022/1/2 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {

    //状态码
    private Integer code;

    //信息
    private Object msg;

    //数据
    private Object data;


    //无参数的成功
    public static R ok(){
        return new R(CodeType.SUCCESS, MsgType.SUCCESS,null);
    }

    //有参数的成功
    public static R ok(Object o){
        return new R(CodeType.SUCCESS, MsgType.SUCCESS,o);
    }

    //无参数的失败
    public static R error(){
        return new R(CodeType.FAILED, MsgType.FAILED,null);
    }

    //有参数的失败
    public static R error(Object msg){
        return new R(CodeType.FAILED, msg,null);
    }

    //有参数的失败
    public static R error(Integer code, Object msg){
        return new R(code,msg,null);
    }

    //数据库有问题
    public static R sqlError(){
        return new R(CodeType.SQL_ERROR, MsgType.SQL_ERROR,null);
    }

    //文件上传失败
    public static R fileUploadError(){
        return new R(CodeType.FILE_UPLOAD_ERROR, MsgType.FILE_UPLOAD_ERROR,null);
    }

    //不存在
    public static R notExists(){
        return new R(CodeType.NOT_EXISTS, MsgType.NOT_EXISTS,null);
    }

}
