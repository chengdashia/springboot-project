package com.example.demo1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo1.mapper.UserMapper;
import com.example.demo1.pojo.User;
import com.example.demo1.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信用户信息 服务实现类
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
