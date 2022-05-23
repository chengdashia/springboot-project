package com.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pojo.Account;
import com.example.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootShrioDemoApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    void contextLoads() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username","ww");
        Account one = accountService.getOne(wrapper);
        System.out.println(one);
    }

}
