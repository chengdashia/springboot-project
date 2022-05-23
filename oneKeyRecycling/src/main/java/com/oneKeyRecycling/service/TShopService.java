package com.oneKeyRecycling.service;

import com.oneKeyRecycling.pojo.TShop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
public interface TShopService extends IService<TShop> {

    //商家列表
    Map<String,Object> getSellerList(int pageNum, int pageSize);

}
