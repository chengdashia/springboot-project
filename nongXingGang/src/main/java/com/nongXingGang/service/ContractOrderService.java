package com.nongXingGang.service;

import com.nongXingGang.pojo.ContractOrder;
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
public interface ContractOrderService extends IService<ContractOrder> {

    //下订单
    int placeOrder(String openid,String signA, String goodsUUId, String addressUUId, String kilogram, String remark);

    //查看订单 不分页
    Map<String,Object> getOrderList(String openid, int uStatus);

    //查看订单 分页
    Map<String, Object> getOrderListPage(String openid, int uStatus, int pageNum, int pageSize);

    //买家  取消订单
    int buyerCancelOrder(String id, String orderUUId);

    //买家  取消订单
    int sellerCancelOrder(String id, String orderUUId);
}
