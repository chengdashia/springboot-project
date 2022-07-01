package com.nongXingGang.service;

import com.nongXingGang.pojo.Store;
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
public interface StoreService extends IService<Store> {

    /**
     * 查询收藏记录
     * @param id                  用户id
     * @param pageNum             页码
     * @param pageSize             页数
     * @return                   R
     */
    R getMyStoreList(String id, int pageNum, int pageSize);

    /**
     * 添加收藏
     * @param id           用户id
     * @param thingUUId     事情的uuid
     * @param thingType      事情的类型
     * @return
     */
    R addStore(String id, String thingUUId, int thingType);

    /**
     * 查询商品的收藏记录
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    R findMyStoreGoodsList(String id, int pageNum, int pageSize);

    /**
     * 查询需求的收藏记录
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
   R findMyStoreDemandList(String id, int pageNum, int pageSize);
}
