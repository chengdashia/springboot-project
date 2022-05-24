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

    //查询浏览记录
    R getBrowseRecords(String id);
}
