package com.example.demo1.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo1.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 微信用户信息 Mapper 接口
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-27
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
