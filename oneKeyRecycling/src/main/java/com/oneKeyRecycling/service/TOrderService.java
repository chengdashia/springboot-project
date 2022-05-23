package com.oneKeyRecycling.service;

import com.oneKeyRecycling.pojo.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
public interface TOrderService extends IService<TOrder> {

    //创建订单
    int createOrder(String uuId, String bId, String sName, int sQuantity, Float sWeight, String img1, String img2, String img3, String address, String remark, BigDecimal totalPrice);

    //查看订单 分页  还有根据状态
    Map<String,Object> getOrderPage(int status, int pageNum, int pageSize);
}
