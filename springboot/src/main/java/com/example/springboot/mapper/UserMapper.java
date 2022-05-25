package com.example.springboot.mapper;

import com.example.springboot.pojo.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 成大事
 * @since 2022-05-25 19:05:52
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {

}
