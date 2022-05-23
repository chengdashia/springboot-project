package com.nongXingGang.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.nongXingGang.mapper.GoodsMapper;
import com.nongXingGang.mapper.ReceivingAddressMapper;
import com.nongXingGang.pojo.ContractOrder;
import com.nongXingGang.mapper.ContractOrderMapper;
import com.nongXingGang.pojo.Goods;
import com.nongXingGang.pojo.PdfData;
import com.nongXingGang.pojo.ReceivingAddress;
import com.nongXingGang.service.ContractOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.utils.calculation.BigDecimalUtil;
import com.nongXingGang.utils.calculation.NumberUtil;
import com.nongXingGang.utils.pdf.AddAutograph;
import com.nongXingGang.utils.pdf.GenerateContract;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Service
public class ContractOrderServiceImpl extends ServiceImpl<ContractOrderMapper, ContractOrder> implements ContractOrderService {

    @Resource
    private ContractOrderMapper orderMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private ReceivingAddressMapper addressMapper;

    //下订单
    @Override
    public int placeOrder(String openid, String signA, String goodsUUId, String addressUUId, String kilogram, String remark) {
            Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>()
                    .select("user_openid", "goods_kilogram","goods_liaison_man","goods_contact_number","goods_production_area","goods_type","goods_varieties","goods_price")
                    .eq("goods_uuid", goodsUUId));
            //如果能查到这个商品
            if(goods != null){
                //创建一个pdf的模板
                PdfData pdfData = new PdfData();

                //查询地址表的买家的详细信息
                ReceivingAddress address = addressMapper.selectOne(new QueryWrapper<ReceivingAddress>().eq("uuid", addressUUId));

                //向pdfData  添加数据
                pdfData.setPartA(address.getUserRealName());
                pdfData.setPartB(goods.getGoodsLiaisonMan());
                pdfData.setPartAAddress(address.getUserDetailedAddress());
                pdfData.setPartBAddress(goods.getGoodsProductionArea());
                pdfData.setPartAPhone(address.getUserTel());
                pdfData.setPartBPhone(goods.getGoodsContactNumber());
                pdfData.setNums(kilogram);
                pdfData.setVarieties(goods.getGoodsVarieties());
                pdfData.setType(goods.getGoodsType());
                pdfData.setPrice(String.valueOf(goods.getGoodsPrice()));
                pdfData.setTotalPrice(String.valueOf(BigDecimalUtil.mul(String.valueOf(goods.getGoodsPrice()), kilogram)));
                pdfData.setTotal(NumberUtil.digitCapital(goods.getGoodsPrice().doubleValue()));
                pdfData.setYear(String.valueOf(DateUtil.year(DateUtil.date())));
                pdfData.setMonth(String.valueOf(DateUtil.month(DateUtil.date()) + 1));
                pdfData.setDay(String.valueOf(DateUtil.dayOfMonth(DateUtil.date())));

                //获得合同模板
                String fillTemplate = GenerateContract.wordsFillTemplate(pdfData);

                //添加卖家签名
                AddAutograph.AddSignA(signA,fillTemplate);


                //创建订单。添加到数据库
                String sellerOpenid = goods.getUserOpenid();
                ContractOrder order = new ContractOrder();
                order.setOrderUuid(IdUtil.simpleUUID());
                order.setBuyerOpenid(openid);
                order.setGoodsUuid(goodsUUId);
                order.setSellerOpenid(sellerOpenid);
                order.setReceivingAddressUuid(addressUUId);
                order.setOrderCreateTime(new Date());
                BigDecimal kilogramS = new BigDecimal(kilogram);
                order.setOrderKilogram(kilogramS);
                order.setOrderRemarks(remark);
                order.setContractAddress(fillTemplate);

                int insert = orderMapper.insert(order);
                if(insert == 1){
                    return StatusType.SUCCESS;
                }else {
                    return StatusType.ERROR;
                }
            }else {
                return StatusType.NOT_EXISTS;
            }
    }

    //获取订单列表  不分页
    @Override
    public Map<String, Object> getOrderList(String openid, int uStatus) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> maps;
        if(uStatus == Constants.BUYER){

                maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                        .select(ContractOrder.class, i -> !i.getColumn().equals("contract_address"))
                        .eq("buyer_openid", openid));
                if(maps != null){
                    map.put("status",StatusType.SUCCESS);
                    map.put("data",maps);
                }else {
                    map.put("status",StatusType.ERROR);
                }
        }else if(uStatus == Constants.SELLER){

                maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                        .select(ContractOrder.class, i -> !i.getColumn().equals("contract_address"))
                        .eq("seller_openid", openid));
                if(maps != null){
                    map.put("status",StatusType.SUCCESS);
                    map.put("data",maps);
                }else {
                    map.put("status",StatusType.ERROR);
                }
        }else {
            map.put("status",StatusType.ERROR);
        }

        return map;
    }

    //获取订单列表  分页
    @Override
    public Map<String, Object> getOrderListPage(String openid, int uStatus, int pageNum, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        IPage<Map<String, Object>> mapPage;
        int start = (pageNum - 1) * pageSize;
        if(uStatus == Constants.BUYER){
//
//                mapPage = orderMapper.selectMapsPage(new Page<>(pageNum, pageSize), new QueryWrapper<ContractOrder>()
//                        .select(ContractOrder.class, i -> !i.getColumn().equals("contract_address"))
//                        .eq("buyer_openid", openid));

                mapPage = goodsMapper.selectJoinMapsPage(new Page<>(start, pageSize), new MPJLambdaWrapper<>()
                    .select(Goods::getGoodsVarieties, Goods::getGoodsType,Goods::getGoodsPrice,Goods::getGoodsMainImgUrl)
                    .select(ContractOrder::getOrderUuid, ContractOrder::getOrderStatus,ContractOrder::getOrderCreateTime)
                    .leftJoin(ContractOrder.class, ContractOrder::getGoodsUuid, Goods::getGoodsUuid)
                    .eq(ContractOrder::getBuyerOpenid, openid));
            if(mapPage != null){
                    map.put("status",StatusType.SUCCESS);
                    map.put("data", mapPage);
                }else {
                    map.put("status",StatusType.ERROR);
                }
        }else if(uStatus == Constants.SELLER){

//                mapPage = orderMapper.selectMapsPage(new Page<>(pageNum,pageSize),new QueryWrapper<ContractOrder>()
//                        .select(ContractOrder.class, i -> !i.getColumn().equals("contract_address"))
//                        .eq("seller_openid", openid));
            mapPage = goodsMapper.selectJoinMapsPage(new Page<>(start, pageSize), new MPJLambdaWrapper<>()
                    .select(Goods::getGoodsVarieties, Goods::getGoodsType,Goods::getGoodsPrice,Goods::getGoodsMainImgUrl)
                    .select(ContractOrder::getOrderUuid, ContractOrder::getOrderStatus,ContractOrder::getOrderCreateTime)
                    .leftJoin(ContractOrder.class, ContractOrder::getGoodsUuid, Goods::getGoodsUuid)
                    .eq(ContractOrder::getSellerOpenid, openid));
                if(mapPage != null){
                    map.put("status",StatusType.SUCCESS);
                    map.put("data", mapPage);
                }else {
                    map.put("status",StatusType.ERROR);
                }
        }else {
            map.put("status",StatusType.ERROR);
        }

        return map;
    }

    //买家  取消订单
    @Override
    public int buyerCancelOrder(String id, String orderUUId) {
        try {
            int delete = orderMapper.delete(new QueryWrapper<ContractOrder>()
                    .eq("buyer_openid", id)
                    .eq("order_uuid", orderUUId));
            if(delete == 1){
                return StatusType.SUCCESS;
            }
            return StatusType.ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return StatusType.SQL_ERROR;
        }
    }

    //卖家 取消订单
    @Override
    public int sellerCancelOrder(String id, String orderUUId) {
        try {
            int update = orderMapper.update(null, new UpdateWrapper<ContractOrder>()
                    .set("order_status", Constants.REFUSE_SIGNED)
                    .eq("seller_openid", id)
                    .eq("order_uuid", orderUUId));
            if(update == 1){
                return StatusType.SUCCESS;
            }
            return StatusType.ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return StatusType.SQL_ERROR;
        }

    }
}
