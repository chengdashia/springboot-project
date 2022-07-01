package com.nongXingGang.service;

import com.nongXingGang.pojo.ContractOrder;
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
public interface ContractOrderService extends IService<ContractOrder> {

    /**
     * 下订单
     * @param openid
     * @param signA
     * @param goodsUUId
     * @param addressUUId
     * @param kilogram
     * @param remark
     * @return
     */
    R placeOrder(String openid, String signA, String goodsUUId, String addressUUId, String kilogram, String remark);

    /**
     * 查看订单 不分页
     * @param openid
     * @param uStatus
     * @return
     */
    R getOrderList(String openid, int uStatus);

    /**
     * 查看订单 分页
     * @param openid
     * @param uStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    R getOrderListPage(String openid, int uStatus, int pageNum, int pageSize);

    /**
     * 买家  取消订单
     * @param id
     * @param orderUUId
     * @return
     */
    R buyerCancelOrder(String id, String orderUUId);

    /**
     * 买家  取消订单
     * @param id
     * @param orderUUId
     * @return
     */
    R sellerCancelOrder(String id, String orderUUId);

    /**
     * 查看各种订单的数量
     * @return R
     */
    R getAllKindsOfNums();

    /**
     * 卖家确认
     * @param imagePath 签名的图片的地址
     * @param orderId      订单id
     * @return  R
     */
    R sellerConfirm(String imagePath, String orderId);

    /**
     * 卖家查看不同的订单  分页
     * @param oStatus         订单状态
     * @param pageNum           页码
     * @param pageSize           数量
     * @return               R
     */
    R sellerGetOrderPageByStatus(int oStatus, int pageNum, int pageSize);

    /**
     * 查看订单
     * @param orderId         订单id
     * @param uStatus         用户状态 0:买家  1 卖家
     * @return                R
     */
    R viewContract(int uStatus,String orderId);
}
