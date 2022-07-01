package com.nongXingGang.service.impl;

import cn.dev33.satoken.stp.StpUtil;
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
import com.nongXingGang.utils.result.R;
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
    @Override
    public R placeOrder(String openid, String signA, String goodsUUId, String addressUUId, String kilogram, String remark) {
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
                pdfData.setPartA(goods.getGoodsLiaisonMan());
                pdfData.setPartB(address.getUserRealName());
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

                //添加买家签名
                AddAutograph.AddSignA(signA,fillTemplate);


                String pdfFilePath = getWebFileUrl(fillTemplate.substring(fillTemplate.lastIndexOf("/") + 1));


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

                //order.setContractAddress(fillTemplate);
                order.setContractAddress(pdfFilePath);

                order.setUpdateTime(new Date());

                int insert = orderMapper.insert(order);
                if(insert == 1){
                    return R.ok();
                }else {
                    return R.error();
                }
            }else {
                return R.error();
            }
    }

    /**
     * 获取订单列表  不分页
     * @param openid
     * @param uStatus
     * @return
     */
    @Override
    public R getOrderList(String openid, int uStatus) {
        List<Map<String, Object>> maps;
        if(uStatus == Constants.BUYER){
            maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                    .select(ContractOrder.class, i -> ! "contract_address".equals(i.getColumn()))
                    .eq("buyer_openid", openid));
            if(maps != null){
                return R.ok(maps);
            }else {
                return R.error();
            }
        }else if(uStatus == Constants.SELLER){

                maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                        .select(ContractOrder.class, i -> !"contract_address".equals(i.getColumn()))
                        .eq("seller_openid", openid));
                if(maps != null){
                    return R.ok(maps);
                }else {
                    return R.error();
                }
        }else {
            return R.error();
        }
    }

    /**
     * 获取订单列表  分页
     * @param openid
     * @param uStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public R getOrderListPage(String openid, int uStatus, int pageNum, int pageSize) {
        IPage<Map<String, Object>> mapPage;
        if(uStatus == Constants.BUYER){
            mapPage = goodsMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                .select(Goods::getGoodsVarieties, Goods::getGoodsType,Goods::getGoodsPrice,Goods::getGoodsMainImgUrl)
                .select(ContractOrder::getOrderUuid, ContractOrder::getOrderStatus,ContractOrder::getOrderCreateTime,ContractOrder::getOrderKilogram)
                .leftJoin(ContractOrder.class, ContractOrder::getGoodsUuid, Goods::getGoodsUuid)
                .eq(ContractOrder::getBuyerOpenid, openid));
            if(mapPage != null){
                return R.ok(mapPage);
            }else {
               return R.error();
            }
        }else if(uStatus == Constants.SELLER){

            mapPage = goodsMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(Goods::getGoodsVarieties, Goods::getGoodsType,Goods::getGoodsPrice,Goods::getGoodsMainImgUrl)
                    .select(ContractOrder::getOrderUuid, ContractOrder::getOrderStatus,ContractOrder::getOrderCreateTime,ContractOrder::getOrderKilogram)
                    .leftJoin(ContractOrder.class, ContractOrder::getGoodsUuid, Goods::getGoodsUuid)
                    .eq(ContractOrder::getSellerOpenid, openid));
                if(mapPage != null){
                    return R.ok(mapPage);
                }else {
                    return R.error();
                }
        }else {
            return R.error();
        }
    }

    /**
     * 买家  取消订单
     * @param id
     * @param orderUUId
     * @return
     */
    @Override
    public R buyerCancelOrder(String id, String orderUUId) {
        try {
            int delete = orderMapper.delete(new QueryWrapper<ContractOrder>()
                    .eq("buyer_openid", id)
                    .eq("order_uuid", orderUUId));
            if(delete == 1){
                return R.ok();
            }
            return R.error();
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    /**
     * 卖家 取消订单
     * @param id
     * @param orderUuId
     * @return
     */
    @Override
    public R sellerCancelOrder(String id, String orderUuId) {
        try {
            int update = orderMapper.update(null, new UpdateWrapper<ContractOrder>()
                    .set("order_status", Constants.REFUSE_SIGNED)
                    .eq("seller_openid", id)
                    .eq("order_uuid", orderUuId));
            if(update == 1){
                return R.ok();
            }
            return R.error();
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }

    }

    /**
     * 查看各种订单的数量
     * @return R
     */
    @Override
    public R getAllKindsOfNums() {
        String id = (String) StpUtil.getLoginId();
        List<Map<String, Object>> maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                .select("order_status","count(1) as num")
                .groupBy("order_status")
                .eq("seller_openid", id));
        return R.ok(maps);
    }

    /**
     * 卖家确认
     * @param imagePath 签名的图片的地址
     * @param orderId      订单id
     * @return  R
     */
    @Override
    public R sellerConfirm(String imagePath, String orderId) {
        ContractOrder order = orderMapper.selectOne(new QueryWrapper<ContractOrder>().select("contract_address")
                .eq("order_uuid",orderId));
        //添加卖家签名
        AddAutograph.AddSignB(imagePath,getDockerFileUrl(order.getContractAddress()));
        int update = orderMapper.update(null, new UpdateWrapper<ContractOrder>()
                .set("order_status", Constants.SIGNED)
                .set("update_time", new Date())
                .eq("order_uuid", orderId));
        if (update == 1){
            return R.ok();
        }
        return R.error();
    }

    /**
     * 卖家查看不同的订单  分页
     * @param oStatus         订单状态
     * @param pageNum           页码
     * @param pageSize           数量
     * @return               R
     */
    @Override
    public R sellerGetOrderPageByStatus(int oStatus, int pageNum, int pageSize) {
        String id = (String) StpUtil.getLoginId();
        IPage<Map<String, Object>> maps = goodsMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize, false), new MPJLambdaWrapper<>()
                .select(Goods::getGoodsVarieties, Goods::getGoodsType, Goods::getGoodsPrice, Goods::getGoodsMainImgUrl)
                .select(ContractOrder::getOrderUuid, ContractOrder::getOrderStatus, ContractOrder::getOrderCreateTime, ContractOrder::getOrderKilogram)
                .leftJoin(ContractOrder.class, ContractOrder::getGoodsUuid, Goods::getGoodsUuid)
                .eq(ContractOrder::getSellerOpenid, id)
                .eq(ContractOrder::getOrderStatus, oStatus));
        return R.ok(maps);
    }

    /**
     * 查看订单
     * @param orderId         订单id
     * @param uStatus         用户状态 0:买家  1 卖家
     * @return                R
     */
    @Override
    public R viewContract(int uStatus,String orderId) {
        String id = (String) StpUtil.getLoginId();
        List<Map<String, Object>> maps = null;
        if(uStatus == Constants.BUYER){
            maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                            .select("contract_address")
                    .eq("order_uuid", orderId)
                    .eq("buyer_openid", id));
        }else if(uStatus == Constants.SELLER){
            maps = orderMapper.selectMaps(new QueryWrapper<ContractOrder>()
                    .select("contract_address")
                    .eq("order_uuid", orderId)
                    .eq("seller_openid", id));
        }else {
            return R.error();
        }
        return R.ok(maps);
    }


    //获取网站的文件地址
    public static String getWebFileUrl(String fileName){
        return "https://chengdashi.cn/file/"+fileName;

    }

    //获取docker部署下的的文件地址
    public static String getDockerFileUrl(String filePath){
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        return "/home/file/" + fileName;

    }
}
