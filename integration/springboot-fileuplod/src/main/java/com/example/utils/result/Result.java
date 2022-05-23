package com.example.utils.result;

import lombok.Data;

@Data
public class Result {

    private int code;

    private String msg = "success";

    private Object data;

    public Result setCode(ResultCode resultCode){
        this.code = resultCode.code;
        return this;
    }

    public Result setMessage(String message){
        this.msg = message;
        return this;
    }

    public Result setData(Object data){
        this.data = data;
        return this;
    }

}
