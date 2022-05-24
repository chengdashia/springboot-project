package com.nongXingGang.service;

import com.nongXingGang.pojo.ReceivingAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nongXingGang.utils.result.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Transactional
public interface ReceivingAddressService extends IService<ReceivingAddress> {

    //获取收货地址
    R getSelfAddress(String openid);

    //添加收货地址
    R addAddress(String openid, String userRealName, String userTel, String userDetailedAddress);

}
