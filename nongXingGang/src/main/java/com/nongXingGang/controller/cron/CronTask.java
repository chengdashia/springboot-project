package com.nongXingGang.controller.cron;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nongXingGang.pojo.BrowseRecords;
import com.nongXingGang.pojo.Store;
import com.nongXingGang.service.BrowseRecordsService;
import com.nongXingGang.service.StoreService;
import com.nongXingGang.utils.others.ApplicationContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author 成大事
 * @since 2022/5/7 15:30
 */
@Slf4j
@Api(tags = "定时任务")
public class CronTask {


    //注入方式不用@Autowired，改成下面的代码注入
    private final BrowseRecordsService browseRecordsService= ApplicationContextUtil.getBean(BrowseRecordsService.class);

    private final StoreService storeService= ApplicationContextUtil.getBean(StoreService.class);



    @ApiOperation("清除过期的浏览记录")
    public void removeExpireBrowseRecords() {

        log.info("清楚过期的浏览记录：");
        browseRecordsService.getBaseMapper().delete(new QueryWrapper<BrowseRecords>()
                .lt("create_time", DateUtil.lastMonth()));
    }


    @ApiOperation("清除过期的收藏记录")
    public void removeExpireStoreRecords() {
        storeService.getBaseMapper().delete(new QueryWrapper<Store>()
                        .lt("col_time", DateUtil.offsetMonth(DateUtil.date(), -12)));
    }

}