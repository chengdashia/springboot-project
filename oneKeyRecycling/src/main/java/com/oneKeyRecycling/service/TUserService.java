package com.oneKeyRecycling.service;

import com.oneKeyRecycling.pojo.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
public interface TUserService extends IService<TUser> {

    //使用手机号+密码登录
    Map<String,Object> publicLogin(String phone, String pwd);

    //注册
    int register(String phone);

    //短信登录+注册
    Map<String,Object> SMSLogin(String phone);

    //认证
    int idAuthentication(String uuid,String idNum, String realName, String idImgFrontPath, String idImgBackPath);

    //修改个人信息
    int updateMyInfo(String uuid,String address, String nickName, String avatar, String pwd);
}
