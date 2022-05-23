package com.cloudDisk.config.saToken;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.pojo.UserInfo;
import com.cloudDisk.service.UserInfoService;
import com.cloudDisk.utils.redis.RedisUtil;
import com.cloudDisk.utils.safe.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限验证接口扩展
 * @author 成大事
 * @since 2022/3/31 22:23
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    private final UserInfoService userService;
    private final RedisUtil redisUtil;

    @Autowired
    public StpInterfaceImpl(UserInfoService userService,RedisUtil redisUtil){
        this.userService = userService;
        this.redisUtil = redisUtil;
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object token, String loginType) {
        return null;
    }


    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object token, String loginType) {
        String uId = JWTUtil.getUUId((String) token);
        try{
            String uIdRoles = uId + ":roles";
            if(redisUtil.hasKey(uIdRoles)){
                List<Object> objects = redisUtil.lGet(uIdRoles, 0, -1);
                if(objects != null){
                    return objects.stream().map(Object::toString).collect(Collectors.toList());
                }
            }else {
                UserInfo user = userService.getOne(new QueryWrapper<UserInfo>()
                        .select("user_initialize")
                        .eq("user_id", uId));
                if(user != null){
                    List<String> list = new ArrayList<>();
                    Integer userInitialize = user.getUserInitialize();
                    list.add(String.valueOf(userInitialize));
                    redisUtil.lPush(uIdRoles,list);
                    return list;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
