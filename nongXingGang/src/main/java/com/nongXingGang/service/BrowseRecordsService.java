package com.nongXingGang.service;

import com.nongXingGang.pojo.BrowseRecords;
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
public interface BrowseRecordsService extends IService<BrowseRecords> {

    /**
     * 查询浏览记录
     * @param id                 用户id
     * @param pageNum             页码
     * @param pageSize            数量
     * @return  R
     */
    R getBrowseRecords(String id,int pageNum,int pageSize);

    /**
     * 查询商品的浏览记录
     * @param id                 用户id
     * @param pageNum             页码
     * @param pageSize            数量
     * @return  R
     */
    R findMyBrowseGoodsList(String id, int pageNum, int pageSize);

    /**
     * 查询需求的浏览记录
     * @param id                 用户id
     * @param pageNum             页码
     * @param pageSize            数量
     * @return  R
     */
    R findMyBrowseDemandList(String id, int pageNum, int pageSize);
}
