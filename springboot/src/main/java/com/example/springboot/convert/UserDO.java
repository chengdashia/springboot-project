package com.example.springboot.convert;

import lombok.Data;

/**
 * @author 成大事
 * @since 2022/5/29 21:19
 */
@Data
public class UserDO {
    /** 用户编号 **/
    private Integer id;
    /** 用户名 **/
    private String username;
    /** 密码 **/
    private String pwd;
}
