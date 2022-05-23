package com.manke.server;

import org.springframework.stereotype.Service;

/**
 * @author 成大事
 * @since 2022/3/24 17:33
 */
@Service
public interface PayService {
    /**
     * @description: 支付宝电脑网页支付
     * @param subject: 订单名称
     * @param total: 金额
     * @date: 2020/11/3
     * @return java.lang.String
     */
    String webPay(String subject, String total);

    /**
     * @description: 支付宝手机网页支付
     * @param subject: 订单名称
     * @param total: 金额
     * @date: 2020/11/3
     * @return java.lang.String
     */
    String phonePay(String subject, String total);

    /**
     * @description: 支付宝退款
     * @param outTradeNo: 商家订单号
     * @param refundAmount: 退款金额(不能大于交易金额)
     * @date: 2020/11/3
     * @return java.lang.String
     */
    String refund(String outTradeNo, String refundAmount);







}
