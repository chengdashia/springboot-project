package com.manke.controller;

import com.alipay.easysdk.factory.Factory;
import com.manke.server.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022/3/24 17:34
 */
@RestController
@RequestMapping(value = "/pay")
public class PayController {

    private final PayService payService;

    @Autowired
    public PayController(PayService payService) {
        this.payService = payService;
    }

    /**
     * 支付宝电脑网页支付
     * @param subject: 订单名称
     * @param total:   金额
     * @return java.lang.String
     */
    @PostMapping(value = "/webPay",produces = "text/html;charset=UTF-8")
    public String page(String subject, String total) {

        subject = "测试支付";
        total = "1000";

        return payService.webPay(subject, total);
    }

    /**
     * 支付宝手机网页支付
     * @param subject: 订单名称
     * @param total:   金额
     * @return java.lang.String
     */
    @PostMapping("/phonePay")
    public String wap(String subject, String total) {
        subject = "测试支付";
        total = "100000";

        return payService.phonePay(subject, total);
    }

    /**
     * @param request: 请求
     * @return java.lang.String
     * @description: 支付宝异步回调
     * @date: 2020/11/3
     */
    @PostMapping("/notify_url")
    public String notify_url(HttpServletRequest request) throws Exception {

        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
            }
        }

        return "success";
    }

    /**
     * @param outTradeNo:   商家订单号
     * @param refundAmount: 退款金额(不能大于交易金额)
     * @return java.lang.String
     * @description: 支付宝退款
     * @date: 2020/11/3
     */
    @PostMapping("/refund")
    public String refund(String outTradeNo, String refundAmount) {
        return payService.refund(outTradeNo, refundAmount);
    }


}
