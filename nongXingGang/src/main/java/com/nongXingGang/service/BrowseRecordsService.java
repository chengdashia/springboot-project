package com.nongXingGang.service;

import com.nongXingGang.pojo.BrowseRecords;
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
public interface BrowseRecordsService extends IService<BrowseRecords> {

    //查询浏览记录
    Map<String,Object> getBrowseRecords(String id);
}
