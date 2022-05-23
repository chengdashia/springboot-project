package com.nongXingGang.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.nongXingGang.pojo.ReceivingAddress;
import com.nongXingGang.mapper.ReceivingAddressMapper;
import com.nongXingGang.pojo.User;
import com.nongXingGang.service.ReceivingAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.utils.result.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Service
public class ReceivingAddressServiceImpl extends ServiceImpl<ReceivingAddressMapper, ReceivingAddress> implements ReceivingAddressService {

    @Resource
    private ReceivingAddressMapper receivingAddressMapper;

    //获取收货地址
    @Override
    public Map<String, Object> getSelfAddress(String openid) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> userAddressList = receivingAddressMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .selectAll(ReceivingAddress.class)
                    .select(User::getUserStatus)
                    .leftJoin(User.class, User::getUserOpenid, ReceivingAddress::getUserOpenid)
                    .eq(ReceivingAddress::getUserOpenid, openid));
//            List<Map<String, Object>> userAddressList = receivingAddressMapper.selectMaps(new QueryWrapper<ReceivingAddress>()
//                    .eq("user_openid", openid));
            if(userAddressList != null){
                map.put("status", StatusType.SUCCESS);
                map.put("data",userAddressList);
            }else {
                map.put("status",StatusType.NOT_EXISTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",StatusType.SQL_ERROR);
        }
        return map;
    }

    //添加地址
    @Override
    public int addAddress(String openid, String userRealName, String userTel, String userDetailedAddress) {
        try {
            ReceivingAddress receivingAddress = new ReceivingAddress();
            receivingAddress.setUuid(IdUtil.simpleUUID());
            receivingAddress.setUserOpenid(openid);
            receivingAddress.setUserRealName(userRealName);
            receivingAddress.setUserTel(userTel);
            receivingAddress.setUserDetailedAddress(userDetailedAddress);
            int insert = receivingAddressMapper.insert(receivingAddress);
            if(insert == 1){
                return StatusType.SUCCESS;
            }else {
                return StatusType.ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return StatusType.SQL_ERROR;
        }
    }
}
