package com.nongXingGang.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nongXingGang.mapper.UserMapper;
import com.nongXingGang.pojo.Legal;
import com.nongXingGang.mapper.LegalMapper;
import com.nongXingGang.pojo.User;
import com.nongXingGang.service.LegalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Service
public class LegalServiceImpl extends ServiceImpl<LegalMapper, Legal> implements LegalService {

    @Resource
    private LegalMapper legalMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public R certification(String openid, String legalNum, String imgPath) {
        Legal legal = new Legal();
        legal.setUserOpenid(openid);
        legal.setUuid(IdUtil.simpleUUID());
        legal.setRegistrationNum(legalNum);
        legal.setBusinessLicenseImg(imgPath);

        int insert = legalMapper.insert(legal);
        if(insert == 1){
            User user = userMapper.selectOne(new QueryWrapper<User>()
                            .select("user_status")
                    .eq("user_openid", openid));

            if(user.getUserStatus() == Constants.BUYER){
                userMapper.update(null,new LambdaUpdateWrapper<User>()
                        .set(User::getUserStatus, Constants.SELLER)
                        .eq(User::getUserOpenid,openid));
                return R.ok();
            }else {
                return R.error("请先认证身份");
            }

        }else {
            return R.error();
        }
    }
}
