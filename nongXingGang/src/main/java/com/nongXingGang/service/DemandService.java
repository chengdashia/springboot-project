package com.nongXingGang.service;

import com.nongXingGang.pojo.Demand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nongXingGang.utils.result.R;
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
public interface DemandService extends IService<Demand> {
    //获取需求数据
    R getDemandGoods(int pageNum, int pageSize);

    //获取需求的详细数据
    R getNeedDetails(String demandUUId);

    //添加需求
    R addDemand(String openid, Demand demand);

    //修改需求
    R updateDemand(String openid, Demand demand);

    //删除需求信息
    R delDemand(String id, String demandUUId);
}
