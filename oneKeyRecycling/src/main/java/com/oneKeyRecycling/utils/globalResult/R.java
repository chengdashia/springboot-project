package com.oneKeyRecycling.utils.globalResult;

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
    public static R success(){
        return new R(CodeType.SUCCESS, MsgType.SUCCESS,null);
    }

    //有参数的成功
    public static R success(Object o){
        return new R(CodeType.SUCCESS, MsgType.SUCCESS,o);
    }

    //无参数的失败
    public static R failure(){
        return new R(CodeType.FAILED, MsgType.FAILED,null);
    }

    //有参数的失败
    public static R failure(Object o){
        return new R(CodeType.FAILED, MsgType.FAILED,o);
    }

    //有参数的失败
    public static R failure(Integer code,Object msg){
        return new R(code,msg,null);
    }

    //数据库有问题
    public static R sqlError(){
        return new R(CodeType.SQL_ERROR, MsgType.SQL_ERROR,null);
    }

    //不存在
    public static R notExists(){
        return new R(CodeType.NOT_EXISTS, MsgType.NOT_EXISTS,null);
    }



}
