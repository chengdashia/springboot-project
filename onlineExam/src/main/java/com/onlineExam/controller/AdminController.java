package com.onlineExam.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineExam.config.validation.Mobile;
import com.onlineExam.mapper.TestPaperMapper;
import com.onlineExam.pojo.*;
import com.onlineExam.service.*;
import com.onlineExam.utils.result.CodeType;
import com.onlineExam.utils.result.MsgType;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.safe.JWTUtils;
import com.onlineExam.utils.safe.SecureDESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.websocket.server.PathParam;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-08
 */
@Api(tags = "管理员操作")
@RestController
@RequestMapping("/admin")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminController {


    private final AdminService adminService;


    private final TestPaperService testPaperService;

    private final TestPaperMapper testPaperMapper;


    private final QuestionInfoService questionInfoService;


    private final InstituteInfoService instituteInfoService;


    private final ScoreInfoService scoreInfoService;


    @ApiOperation("手机号+密码登录")
    @PostMapping("/login")
    public R login(@ApiParam(value = "手机号",required = true) @Mobile @RequestParam("phone") String phone,
                   @ApiParam(value = "密码",required = true) @NotBlank(message = "密码不能为空！") @RequestParam("pwd") String pwd){

        String encryptPhone = SecureDESUtil.encrypt(phone);
        String encryptPwd = SecureDESUtil.encrypt(pwd);
        try {
            Admin one = adminService.getOne(new QueryWrapper<Admin>()
                    .select("admin_uuid","admin_pwd")
                    .eq("admin_phone", encryptPhone));
            if(one != null){
                //密码正确
                if(one.getAdminPwd().equals(encryptPwd)){
                    Map<String, String> map = new HashMap<>();
                    map.put("uuid", one.getAdminUuid());
                    map.put("phone",encryptPhone);
                    String token = JWTUtils.getToken(map);
                    System.out.println("token:" +token);
                    return R.success(token);
                }
                //密码错误
                return new R(CodeType.PWD_ERROR, MsgType.PWD_ERROR,null);
            }else{
                //用户不存在
                return new R(CodeType.NOT_EXISTS,MsgType.USER_NOT_EXISTS,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failure();
        }
    }


    @ApiOperation("添加新的管理员")
    @PostMapping("/addNewAdmin")
    public R addNewAdmin(@ApiParam(value = "手机号",required = true) @Mobile @RequestParam("phone") String phone,
                         @ApiParam(value = "密码",required = true) @PathParam("pwd") String pwd,
                         @ApiParam(value = "姓名",required = true) @PathParam("name") String name){
        try {
            Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("admin_phone", phone)
                    .eq("admin_pwd", pwd));
            if(admin == null){
                admin = new Admin();
                admin.setAdminUuid(IdUtil.simpleUUID());
                admin.setAdminPhone(SecureDESUtil.encrypt(phone));
                admin.setAdminPwd(SecureDESUtil.encrypt(pwd));
                admin.setAdminName(SecureDESUtil.encrypt(name));
                admin.setAdminCreateTime(new Date());
                admin.setAdminLastTime(new Date());
                try {
                    boolean save = adminService.save(admin);
                    if(save){
                        return R.success();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new R(CodeType.UNKNOWN_ERROR,"未知错误",null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new R(CodeType.UNKNOWN_ERROR,"未知错误",null);

    }


    @ApiOperation("首页数据展示")
    @PostMapping("/dataShowUp")
    public R dataShowUp(){
        List<Object> resultMap = new ArrayList<>();
        Map<String, Object> map;
        Map<String, Object> map1;
        Map<String, Object> map2;
        Map<String, Object> map3;
        try {
            map = instituteInfoService.getMap(new QueryWrapper<InstituteInfo>()
                    .select("sum(institute_student_nums) as total_num"));


            map1 = testPaperService.getMap(new QueryWrapper<TestPaper>()
                    .select("count(*) as testPaperNum","test_uuid")
            );

            map2 = questionInfoService.getMap(new QueryWrapper<QuestionInfo>()
                    .select("count(*) as questionNum")
//                    .in("test_uuid",map1.get("testUuid").toString())
            );

            map3 = scoreInfoService.getMap(new QueryWrapper<ScoreInfo>()
                    .select("count(*) as scoreNum")
//                    .in("test_uuid",map1.get("testUuid").toString())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }

        resultMap.add(map);
        resultMap.add(map1);
        resultMap.add(map2);
        resultMap.add(map3);

        return R.success(resultMap);

    }


    @ApiOperation("首页数据展示 显示学院的学生人数")
    @PostMapping("/dataShowDown")
    public R dataShowDown(){
        List<InstituteInfo> instituteInfos = null;
        try {
            instituteInfos = instituteInfoService.getBaseMapper().selectList(null);
            for(InstituteInfo instituteInfo : instituteInfos){
                instituteInfo.setInstituteName(SecureDESUtil.decrypt(instituteInfo.getInstituteName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(instituteInfos);

    }




}

