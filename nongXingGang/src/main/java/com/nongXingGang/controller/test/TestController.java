package com.nongXingGang.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 成大事
 * @since 2022/5/24 9:10
 */
@RestController
public class TestController {

    @GetMapping("/test/xss")
    public String testxss(String str){
        System.out.println(str);
        return str;
    }
}
