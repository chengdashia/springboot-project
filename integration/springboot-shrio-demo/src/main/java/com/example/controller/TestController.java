package com.example.controller;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @PostMapping("/login")
    public String login(String username, String password, Model model) {
    }
    
}
