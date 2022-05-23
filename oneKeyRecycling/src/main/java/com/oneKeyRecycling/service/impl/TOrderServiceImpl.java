package com.oneKeyRecycling.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oneKeyRecycling.pojo.TOrder;
import com.oneKeyRecycling.mapper.TOrderMapper;
import com.oneKeyRecycling.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneKeyRecycling.utils.Constants.Constant;
import com.oneKeyRecycling.utils.globalResult.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Resource
    private TOrderMapper orderMapper;

    //创建订单
    @Override
    public int createOrder(String uuId, String bId, String sName, int sQuantity, Float sWeight, String img1, String img2, String img3, String address, String remark, BigDecimal totalPrice) {
        TOrder tOrder = new TOrder();
        if("".equals(bId)){
            tOrder.setOStatus(Constant.NOT_CARRY_BUSINESS);
        }else {
            tOrder.setOStatus(Constant.CARRY_BUSINESS);
        }
        tOrder.setOrderId(IdUtil.simpleUUID());
        tOrder.setUId(uuId);
        tOrder.setBId(bId);
        tOrder.setSName(sName);
        tOrder.setSQuantity(sQuantity);
        tOrder.setSWeight(sWeight);
        tOrder.setTotalPrice(totalPrice);
        tOrder.setEndTime(new Date());
        tOrder.setUAddress(address);
        tOrder.setSImgO(img1);
        tOrder.setSImgTw(img2);
        tOrder.setSImgTh(img3);
        try {
            int insert = orderMapper.insert(tOrder);
            if(insert == 1){
                return StatusType.SUCCESS;
            }else {
                return StatusType.ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return StatusType.SQL_ERROR;
        }
    }


    //查看订单 分页  还有根据状态
    @Override
    public Map<String, Object> getOrderPage(int status, int pageNum, int pageSize) {
        Map<String,Object> map = new HashMap<>();
        try {
            Page<Map<String, Object>> orderList = orderMapper.selectMapsPage(new Page<>(pageNum, pageSize),
                    new QueryWrapper<TOrder>()
                            .eq("o_status", status));
//            Page<TOrder> orderList = orderMapper.selectPage(new Page<>(pageNum, pageSize),
//                    new QueryWrapper<TOrder>()
//                            .eq("o_status", status));
            if(orderList != null){
                map.put("status",StatusType.SUCCESS);
                map.put("data",orderList);
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("status",StatusType.SQL_ERROR);
        }

        return map;
    }
}
