package com.nongXingGang.service;

import com.nongXingGang.pojo.Store;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface StoreService extends IService<Store> {

    //查询收藏记录
    Map<String,Object> getMyStoreList(String id,int pageNum,int pageSize);

    //添加收藏
    int addStore(String id, String thingUUId, int thingType);

    //查询商品的收藏记录
    Map<String, Object> findMyStoreGoodsList(String id, int pageNum, int pageSize);

    //查询需求的收藏记录
    Map<String, Object> findMyStoreDemandList(String id, int pageNum, int pageSize);
}
