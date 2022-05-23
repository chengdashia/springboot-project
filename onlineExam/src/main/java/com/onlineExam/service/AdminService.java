package com.onlineExam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlineExam.pojo.Admin;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-08
 */
@Transactional
public interface AdminService extends IService<Admin> {

}
