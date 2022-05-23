package com.onlineExam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlineExam.mapper.StudentInfoMapper;
import com.onlineExam.pojo.StudentInfo;
import com.onlineExam.service.StudentInfoService;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.SecureDESUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-17
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements StudentInfoService {

    @Resource
    private StudentInfoMapper studentInfoMapper;

    //学号+姓名登录
    @Override
    public int login(String studentId, String studentName) {
            StudentInfo one = studentInfoMapper.selectOne(new QueryWrapper<StudentInfo>()
                    .eq("student_id", studentId)
                    .eq("student_name", studentName));
            if(one == null){
                //不存在
                return StatusType.NOT_EXISTS;
            }
            //成功
            return StatusType.SUCCESS;

    }

    @Override
    public int addLogin(String studentId, String studentName, String instituteUUId, String classUUId) {

        StudentInfo info = studentInfoMapper.selectOne(new QueryWrapper<StudentInfo>().eq("student_id", studentId));
        if(info == null){
            info = new StudentInfo();
            info.setStudentId(studentId);
            info.setStudentName(studentName);
            info.setStudentInstitute(instituteUUId);
            info.setStudentClass(classUUId);
                int result = studentInfoMapper.insert(info);
                if(result == 1){
                    //添加成功
                    return StatusType.SUCCESS;
                }
                //未知报错
                return StatusType.ERROR;
        }
        //学生信息存在 不要重复添加
        return StatusType.EXISTS;
    }

    //学号+密码登录
    @Override
    public Map<String, Object> LoginByStuPwd(String stuNo, String pwd) {
        Map<String, Object> map = new HashMap<>();
        StudentInfo studentInfo = studentInfoMapper.selectOne(new QueryWrapper<StudentInfo>()
                .select("student_pwd","student_name")
                .eq("student_id", stuNo));
        if(studentInfo != null){
            if(studentInfo.getStudentPwd().equals(pwd)){
                //密码正确
                map.put("status",StatusType.SUCCESS);
                map.put("data",SecureDESUtil.decrypt(studentInfo.getStudentName()));
            }else {
                //密码错误
                map.put("status",StatusType.PWD_ERROR);
            }
        } else {
            //用户不存在
            map.put("status",StatusType.NOT_EXISTS);
        }
        return map;
    }

    //修改密码
    @Override
    public int updateStuPwd(String stuNo, String pwd) {
            int update = studentInfoMapper.update(null,
                    new LambdaUpdateWrapper<StudentInfo>()
                            .set(StudentInfo::getStudentPwd, SecureDESUtil.encrypt(pwd))
                            .eq(StudentInfo::getStudentId, stuNo));
            if(update == 1){
                return StatusType.SUCCESS;
            }else {
                return StatusType.ERROR;
            }
    }

    //重置密码
    @Override
    public int reSetPwd(String instituteUUId, String classUUId, String studentId, String studentName, String newPwd) {

            StudentInfo studentInfo = studentInfoMapper.selectOne(new QueryWrapper<StudentInfo>()
                    .eq("student_id", studentId));
            if(studentInfo != null){
                if(!studentInfo.getStudentClass().equals(classUUId) || !studentInfo.getStudentInstitute().equals(instituteUUId)){
                    return StatusType.CHOOSE_ERROR;
                }else {
                    studentInfo.setStudentPwd(newPwd);
                    int i = studentInfoMapper.updateById(studentInfo);
                    if(i == 1){
                        return StatusType.SUCCESS;
                    }else {
                        return StatusType.ERROR;
                    }
                }
            }
            else {
                return StatusType.NOT_EXISTS;
            }
    }

}
