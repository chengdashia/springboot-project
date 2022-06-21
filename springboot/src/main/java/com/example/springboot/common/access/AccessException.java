package com.example.springboot.common.access;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author 成大事
 * @since 2022/6/13 18:25
 */
@ToString
@AllArgsConstructor
public class AccessException extends Exception{

    private int detail;
    
    private String msg;
}
