package com.cloudDisk.service;

import com.cloudDisk.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-10
 */
@Transactional
public interface UserInfoService extends IService<UserInfo> {

    //根据token查询用户信息
    Map<String, Object> getUserInfo(String uuId, int uInit);

    //注册
    int register(String phone, String pwd);
}
