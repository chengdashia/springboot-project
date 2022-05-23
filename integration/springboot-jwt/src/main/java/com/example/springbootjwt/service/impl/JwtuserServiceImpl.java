package com.example.springbootjwt.service.impl;

import com.example.springbootjwt.pojo.Jwtuser;
import com.example.springbootjwt.mapper.JwtuserMapper;
import com.example.springbootjwt.service.JwtuserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-26
 */
@Service
public class JwtuserServiceImpl extends ServiceImpl<JwtuserMapper, Jwtuser> implements JwtuserService {

}
