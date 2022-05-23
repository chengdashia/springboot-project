package com.onlineExam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlineExam.mapper.AdminMapper;
import com.onlineExam.pojo.Admin;
import com.onlineExam.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
