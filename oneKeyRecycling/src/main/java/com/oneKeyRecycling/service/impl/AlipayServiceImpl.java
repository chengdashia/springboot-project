package com.oneKeyRecycling.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oneKeyRecycling.mapper.PayRecordMapper;
import com.oneKeyRecycling.mapper.TOrderMapper;
import com.oneKeyRecycling.pojo.PayRecord;
import com.oneKeyRecycling.pojo.TOrder;
import com.oneKeyRecycling.service.PayService;
import com.oneKeyRecycling.utils.OrderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 成大事
 * @since 2022/3/27 11:26
 */
@Service
public class AlipayServiceImpl implements PayService {

    @Resource
    private TOrderMapper orderMapper;

    // 支付成功后要跳转的页面
    @Value("${alipay.returnUrl}")
    private String returnUrl;

    // 支付宝前台手机网页支付中途取消跳转地址
    @Value("${alipay.errorUrl}")
    private String errorUrl;

    @Resource
    private PayRecordMapper payRecordMapper;

//    @Override
//    public String webPay(String subject, String total) {
//
//        try {
//            AlipayTradePagePayResponse response = Factory.Payment
//                    // 选择电脑网站
//                    .Page()
//                    // 调用支付方法(订单名称, 商家订单号, 金额, 成功页面)
//                    .pay(subject, OrderUtil.getOrderNo(), total, returnUrl);
//            return response.body;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    @Override
    public String webPay(String orderId) {
        TOrder order = orderMapper.selectOne(new QueryWrapper<TOrder>()
                        .select("s_name","total_price","u_id","b_id")
                .eq("order_id", orderId));
        try {
            AlipayTradePagePayResponse response = Factory.Payment
                    // 选择电脑网站
                    .Page()
                    // 调用支付方法(订单名称, 商家订单号, 金额, 成功页面)
                    .pay(order.getSName(), OrderUtil.getOrderNo(), String.valueOf(order.getTotalPrice()), returnUrl);
            PayRecord payRecord = new PayRecord();
            payRecord.setPayId(IdUtil.fastUUID());
            payRecord.setTotalPrice(order.getTotalPrice());
            payRecord.setUId(order.getUId());
            payRecord.setSId(order.getBId());
            payRecord.setOrderId(orderId);
            payRecordMapper.insert(payRecord);
            return response.body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String phonePay(String subject, String total) {

        try {
            AlipayTradeWapPayResponse response = Factory.Payment
                    //选择手机网站
                    .Wap()
                    // 调用支付方法(订单名称, 商家订单号, 金额, 中途退出页面, 成功页面)
                    .pay(subject, OrderUtil.getOrderNo(), total, errorUrl, returnUrl);

            return response.body;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String refund(String outTradeNo, String refundAmount) {
        try {
            AlipayTradeRefundResponse response = Factory.Payment
                    .Common()
                    // 调用交易退款(商家订单号, 退款金额)
                    .refund(outTradeNo, refundAmount);

            if (response.getMsg().equals("Success")) {
                return "退款成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "退款失败";
    }
}
