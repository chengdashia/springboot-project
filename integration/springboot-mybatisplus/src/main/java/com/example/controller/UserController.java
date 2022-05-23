package com.example.controller;


import com.example.pojo.User;
import com.example.service.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author astupidcoder
 * @since 2021-11-20
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping("/getUser")
    public User getUser(){
        return userService.getById(1);
    }

    @PostMapping("/findAllUser")
    public List<User> findAllUser(){
        return userService.findAllUser();
    }

    @PostMapping("/insertUser")
    public void testinsertUser(){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("userName", "成果");

        User user = new User();
        user.setId(4);
        user.setUserName("jjlin");
        user.setPassword("16545");
        int insert = userService.getBaseMapper().insert(user);
        System.out.println(insert);
    }

    @PostMapping("/findUser")
    public void testfindUser(){
        HashMap<String, Object> map = new HashMap<>();
//        map.put("user_name", "成果");
//        map.put("id")


        User user = userService.getById(1);
        System.out.println(user);
    }




}
