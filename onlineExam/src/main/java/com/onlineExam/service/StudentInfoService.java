package com.onlineExam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlineExam.pojo.StudentInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-17
 */
@Transactional
public interface StudentInfoService extends IService<StudentInfo> {

    //学号+姓名登录
    int login(String studentId, String studentName);


    //添加学生
    int addLogin(String studentId, String studentName, String instituteUUId, String classUUId);

    //学号+密码登录
    Map<String,Object> LoginByStuPwd(String stuNo, String pwd);

    //修改密码
    int updateStuPwd(String stuNo, String pwd);

    //重置密码
    int reSetPwd(String instituteUUId, String classUUId, String studentId, String studentName, String newPwd);
}
