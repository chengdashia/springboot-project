package com.nongXingGang.utils.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 成大事
 * @date 2022/3/18 19:44
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlR {

    //状态码
    private Integer status;

    //数据
    private Object data;


    //无参数的成功
    public static SqlR build(int status,Object data){
        return new SqlR(status,data);
    }

//    //无参数的成功
//    public static SqlR error(Object data){
//        return new SqlR(StatusType.SUCCESS,data);
//    }

//    //有参数的成功
//    public static R ok(Object o){
//        return new R(CodeType.SUCCESS, MsgType.SUCCESS,o);
//    }
//
//    //无参数的失败
//    public static R error(){
//        return new R(CodeType.FAILED, MsgType.FAILED,null);
//    }
//
//    //有参数的失败
//    public static R error(Object msg){
//        return new R(CodeType.FAILED, msg,null);
//    }
//
//    //数据库有问题
//    public static R sqlError(){
//        return new R(CodeType.SQL_ERROR, MsgType.SQL_ERROR,null);
//    }
//
//    //不存在
//    public static R notExists(){
//        return new R(CodeType.NOT_EXISTS, MsgType.NOT_EXISTS,null);
//    }
}
