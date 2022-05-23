package com.example.mapper;

import com.example.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
