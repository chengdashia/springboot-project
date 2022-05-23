package com.nongXingGang.service;

import com.nongXingGang.pojo.Demand;
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
public interface DemandService extends IService<Demand> {
    //获取需求数据
    Map<String ,Object> getDemandGoods(int pageNum, int pageSize);

    //获取需求的详细数据
    Map<String ,Object> getNeedDetails(String demandUUId);

    //添加需求
    int addDemand(String openid, Demand demand);

    //修改需求
    int updateDemand(String openid, Demand demand);

    //删除需求信息
    int delDemand(String id, String demandUUId);
}
