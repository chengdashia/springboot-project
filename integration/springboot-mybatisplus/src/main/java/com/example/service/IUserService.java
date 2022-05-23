package com.example.service;

import com.example.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author astupidcoder
 * @since 2021-11-20
 */
public interface IUserService extends IService<User> {
    List<User> findAllUser();
}
