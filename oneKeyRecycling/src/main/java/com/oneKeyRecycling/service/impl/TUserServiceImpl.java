package com.oneKeyRecycling.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oneKeyRecycling.config.identity.IdCardDistinguish;
import com.oneKeyRecycling.pojo.TUser;
import com.oneKeyRecycling.mapper.TUserMapper;
import com.oneKeyRecycling.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneKeyRecycling.utils.Constants.Constant;
import com.oneKeyRecycling.utils.globalResult.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

    @Resource
    private TUserMapper tUserMapper;

    //密码+手机号登录
    @Override
    public Map<String, Object> publicLogin(String phone, String pwd) {
        Map<String, Object> map = new HashMap<>();
        try {
            TUser tUser = tUserMapper.selectOne(new QueryWrapper<TUser>()
                            .select("pwd","uid")
                    .eq("phone", phone));
            if(tUser == null){
                //用户不存在
                map.put("status",StatusType.NOT_EXISTS);
            }else {
                if(tUser.getPwd().equals(pwd)){
                    //账号密码正确
                    map.put("status",StatusType.SUCCESS);
                    map.put("uuid",tUser.getUid());
                }else {
                    //密码错误
                    map.put("status",StatusType.PWD_ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //数据库错误
            map.put("status",StatusType.SQL_ERROR);
        }
        return map;
    }

    @Override
    public int register(String phone) {

        return 0;
    }

    //短信登录+注册
    @Override
    public Map<String, Object> SMSLogin(String phone) {
        Map<String, Object> map = new HashMap<>();
        TUser tUser;
        try {
            tUser = tUserMapper.selectOne(new QueryWrapper<TUser>()
                    .select("pwd","uid")
                    .eq("phone", phone));
            if(tUser == null){
                tUser = new TUser();
                tUser.setUid(IdUtil.simpleUUID());
                tUser.setPhone(phone);
                tUser.setNickName(IdUtil.fastSimpleUUID());
                tUser.setIsDel(Constant.NOT_DEL);
                tUser.setUStatus(Constant.PUBLIC_USER);
                try {
                    int one = tUserMapper.insert(tUser);
                    if(one != 1){
                        //数据库错误
                        map.put("status",StatusType.SQL_ERROR);
                        return map;
                    }
                    //注册成功正确
                    map.put("status",StatusType.NOT_EXISTS_REGISTER);
                    map.put("uuid",tUser.getUid());
                    return map;
                } catch (Exception e) {
                    e.printStackTrace();
                    //数据库错误
                    map.put("status",StatusType.SQL_ERROR);
                    return map;
                }
            }
            //用户存在
            map.put("status",StatusType.SUCCESS);
            map.put("uuid",tUser.getUid());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            //数据库错误
            map.put("status",StatusType.SQL_ERROR);
            return map;
        }
    }

    //身份认证
    @Override
    public int idAuthentication(String uuid, String idNum, String realName, String idImgFrontPath, String idImgBackPath) {

        try {
            Map<String, String> idCardInfo = IdCardDistinguish.getIdCardInfo(idImgFrontPath);
            //首先判断身份证图片是否正确
            if (idCardInfo != null) {
                //然后判断 传过来的姓名和身份证号是否一致。
                if (!idCardInfo.get("idCardNum").equals(idNum) || !idCardInfo.get("realName").equals(realName)) {
                    //身份信息错误
                    return StatusType.ID_INFO_ERROR;
                } else {
                    try {
                        TUser tUser = tUserMapper.selectOne(new QueryWrapper<TUser>().eq("uid", uuid));
                        if (tUser != null) {
                            tUser.setRName(realName);
                            tUser.setIdNumber(idNum);
                            tUser.setIdObverse(idImgFrontPath);
                            tUser.setIdReverse(idImgBackPath);
                            try {
                                int result = tUserMapper.updateById(tUser);
                                if (result == 1) {
                                    //成功
                                    return StatusType.SUCCESS;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //数据库错误
                                return StatusType.SQL_ERROR;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //数据库错误
                        return StatusType.SQL_ERROR;
                    }
                    //错误
                    return StatusType.ERROR;
                }
            }
            //如果图片格式不对。请重新上传。
            FileUtil.del(idImgFrontPath);
            FileUtil.del(idImgBackPath);
            return StatusType.ID_CARD_ERROR;


        } catch (Exception e) {
            e.printStackTrace();
            //数据库错误
            return StatusType.SQL_ERROR;
        }
    }


    //修改个人信息
    @Override
    public int updateMyInfo(String uuid, String address, String nickName, String avatar, String pwd) {
        try {
            TUser tUser = tUserMapper.selectOne(new QueryWrapper<TUser>().eq("uid", uuid));
            if (tUser != null){
                if(address == null && avatar == null && nickName == null && pwd == null){
                    //没做更改
                    return StatusType.NOT_CHANGE;
                }
                if(address != null) tUser.setAddress(address);
                if(nickName != null) tUser.setNickName(nickName);
                if(avatar != null) tUser.setUAvatar(avatar);
                if(pwd != null) tUser.setPwd(pwd);
                try {
                    int i = tUserMapper.updateById(tUser);
                    if(i == 1){
                        //成功
                        return StatusType.SUCCESS;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //数据库错误
                    return StatusType.SQL_ERROR;
                }
            }
            return StatusType.ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            //数据库错误
            return StatusType.SQL_ERROR;
        }
    }
}
