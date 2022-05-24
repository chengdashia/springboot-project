package com.nongXingGang.service;

import com.nongXingGang.pojo.Certification;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nongXingGang.utils.result.R;
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
public interface CertificationService extends IService<Certification> {

    //认证
    R idAuthentication(String openid, String idNum, String realName, String idImgFrontPath, String idImgBackPath) throws Exception;
}
