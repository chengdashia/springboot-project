package com.example.springbootjwt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootjwt.pojo.Jwtuser;
import com.example.springbootjwt.service.JwtuserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootJwtApplicationTests {

    @Autowired
    private JwtuserService jwtuserService;

    @Test
    void contextLoads() {
        QueryWrapper<Jwtuser> wrapper = new QueryWrapper<>();
        wrapper.eq("name","chengguo")
                .eq("password","chengdashi");
        Jwtuser one = jwtuserService.getOne(wrapper);
        System.out.println(one);
    }

}
