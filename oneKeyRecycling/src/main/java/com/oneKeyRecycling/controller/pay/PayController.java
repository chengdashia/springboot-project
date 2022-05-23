package com.oneKeyRecycling.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.oneKeyRecycling.config.AlipayConfig;
import com.oneKeyRecycling.service.PayService;
import com.oneKeyRecycling.utils.globalResult.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 成大事
 * @since 2022/3/24 17:34
 */
@Api("支付接口")
@RestController
@RequestMapping(value = "/pay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayController {

    private final PayService payService;


    /**
     * 支付宝电脑网页支付
     * //     * @param subject: 订单名称
     * //     * @param total:   金额
     *
     * @return java.lang.String
     */
    @PostMapping(value = "/webPay", produces = "text/html;charset=UTF-8")
    public String page(String orderId) {

//        subject = "测试支付";
//        total = "1000";

        return payService.webPay(orderId);
    }

    /**
     * 支付宝手机网页支付
     *
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
    @RequestMapping("/notify_url")
    public String notify_url(HttpServletRequest request) throws Exception {

        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                System.out.println(name + " = " + request.getParameter(name));
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
        System.out.println("进入了回调！");

        return "success";
    }

    @RequestMapping("/success")
    public R paySuccess(@RequestParam("seller_id") String seller_id) {
        return R.success();
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


//    @RequestMapping(value = "notifyAliPay", method = RequestMethod.POST)
//    public String notifyAliPay(HttpServletRequest request) {
//        //将异步通知中收到的待验证所有参数都存放到map中
//        final Map<String, String> params = convertRequestParamsToMap(request);
//        final String paramsJson = JSON.toJSONString(params);
//        System.out.println("支付宝回调：" + paramsJson);
//        try {
//            // 调用SDK验证签名
//            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY,
//                    AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
//            if (signVerified) {
//                System.out.println("支付宝回调签名认证成功");
//                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验
//                // 校验成功后在response中返回success，校验失败返回failure
//                this.check(params);
//                AlipayNotifyParam param = buildAlipayNotifyParam(params);
//                String trade_status = param.getTradeStatus();
//                // 支付成功
//                if (trade_status.equals("TRADE_SUCCESS")
//                        || trade_status.equals(“TRADE_FINISHED”)) {
//                    // 处理支付成功逻辑
//                    try {
//                        //此处做自己的业务处理
//                    } catch (Exception e) {
//                        System.out.println("支付宝回调业务处理报错,params:" + paramsJson);
//                    }
//                } else {
//                    System.out.println("没有处理支付宝回调业务，支付宝交易状态：" + trade_status + "params:" + paramsJson);
//                }
//                // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
//                // 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
//                return "success";
//            } else {
//                System.out.println("支付宝回调签名认证失败，signVerified=false, paramsJson:" + paramsJson);
//                return "failure";
//            }
//        } catch (AlipayApiException e) {
//            System.out.println("支付宝回调签名认证失败,paramsJson:" + paramsJson + ",errorMsg:" + e.getMessage());
//            return "failure";
//        }
//
//
//    }

    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }
}