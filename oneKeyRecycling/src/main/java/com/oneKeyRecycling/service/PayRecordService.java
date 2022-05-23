package com.oneKeyRecycling.service;

import com.oneKeyRecycling.pojo.PayRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-25
 */
@Transactional
public interface PayRecordService extends IService<PayRecord> {

}
