package com.oneKeyRecycling.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oneKeyRecycling.pojo.TShop;
import com.oneKeyRecycling.mapper.TShopMapper;
import com.oneKeyRecycling.pojo.TUser;
import com.oneKeyRecycling.service.TShopService;
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
public class TShopServiceImpl extends ServiceImpl<TShopMapper, TShop> implements TShopService {

    @Resource
    private TShopMapper shopMapper;

    @Override
    public Map<String, Object> getSellerList(int pageNum, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        try{
            IPage<Map<String, Object>> mapIPage = shopMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize),
                    new MPJLambdaWrapper<>()
                            .selectAll(TShop.class)
                            .leftJoin(TUser.class, TUser::getUid, TShop::getUId)
                            .eq(TUser::getUStatus, Constant.BUSINESS));
            if(mapIPage != null){
                map.put("status", StatusType.SUCCESS);
                map.put("data",mapIPage);
            }else {
                map.put("status", StatusType.NOT_EXISTS);
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);

        }
        return map;
    }
}
