package com.example.controller;


import com.example.pojo.Student;
import com.example.service.StudentService;
import com.example.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-21
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/get")
    public String getStudent(){
        Student student = studentService.getById(1);
        String stuName = student.getStuName();
        return stuName;
    }


    @GetMapping("/hello")
    public String test(){

//        redisUtils.set("name2","chengguo");
//        String name = (String) redisUtils.get("name2");

        return "hello world";
    }

    @GetMapping("/setRedis")
    public String testRedis(){
//        redisUtils.set("name","chengguo");
        String name = (String) redisUtils.get("name");
        return name;
    }

}

