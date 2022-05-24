package com.nongXingGang.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nongXingGang.config.identity.IdCardDistinguish;
import com.nongXingGang.mapper.UserMapper;
import com.nongXingGang.pojo.Certification;
import com.nongXingGang.mapper.CertificationMapper;
import com.nongXingGang.pojo.User;
import com.nongXingGang.service.CertificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.utils.result.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class CertificationServiceImpl extends ServiceImpl<CertificationMapper, Certification> implements CertificationService {

    @Resource
    private CertificationMapper certificationMapper;

    @Resource
    private UserMapper userMapper;

    //认证
    @Override
    public R idAuthentication(String openid, String idNum, String realName, String idImgFrontPath, String idImgBackPath) throws RuntimeException {

        Map<String, String> idCardInfo;
        try {
            idCardInfo = IdCardDistinguish.getIdCardInfo(idImgFrontPath);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(CodeType.ID_CARD_ERROR, MsgType.ID_CARD_ERROR);
        }
//        System.out.println("idCardNum:  "+ idCardInfo.get("idCardNumber"));
//        System.out.println("realName:  "+ idCardInfo.get("realName"));
            //首先判断身份证图片是否正确
            if (!idCardInfo.isEmpty()) {
                //然后判断 传过来的姓名和身份证号是否一致。
                if (!idCardInfo.get("idCardNumber").equals(idNum) || !idCardInfo.get("realName").equals(realName)) {
                    //身份信息错误
                    return R.error(CodeType.ID_INFO_ERROR, MsgType.ID_INFO_ERROR);
                } else {
                        Certification certification = certificationMapper.selectOne(new QueryWrapper<Certification>().eq("user_openid", openid));
                        if (certification == null) {
                            certification = new Certification();
                            certification.setUserName(realName);
                            certification.setUserIdNum(idNum);
                            certification.setUserOpenid(openid);
                            certification.setUserIdFrontImg(idImgFrontPath);
                            certification.setUserIdBackImg(idImgBackPath);
                            certificationMapper.insert(certification);
                                int result = userMapper.update(null,new UpdateWrapper<User>()
                                        .set("user_status", Constants.BUYER)
                                        .eq("user_openid",openid));
                                if (result == 1) {
                                    //成功
                                    return R.ok();
                                }
                        }

                    //错误
                    return R.error();
                }
            }
            //如果图片格式不对。请重新上传。
            FileUtil.del(idImgFrontPath);
            FileUtil.del(idImgBackPath);
        return R.error(CodeType.ID_CARD_ERROR, MsgType.ID_CARD_ERROR);
    }
}
