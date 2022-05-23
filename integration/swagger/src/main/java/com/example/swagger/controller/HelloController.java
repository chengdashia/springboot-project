package com.example.swagger.controller;

import com.example.swagger.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){

        return "helloswagger";
    }

    @PostMapping("/user")
    public User getUser(){
        return new User();
    }


}
