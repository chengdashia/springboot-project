package com.nongXingGang.service;

import com.nongXingGang.pojo.Legal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Transactional
public interface LegalService extends IService<Legal> {

    //法人认证
    int certification(String openid, String legalNum, String imgPath);
}
