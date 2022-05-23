package com.nongXingGang.service;

import com.nongXingGang.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nongXingGang.pojo.request.GoodsBody;
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
public interface GoodsService extends IService<Goods> {

    //获取首页数据
    Map<String ,Object> getIndexGoods(String id,int status, int pageNum, int pageSize);

    //获取商品的详细数据
    Map<String, Object> getGoodsDetails(String id, String goodsUUId);

    //添加商品
    int addGoods(String openid,GoodsBody goods);

    //修改商品
    int updateGoods(String openid, GoodsBody goods);

    //删除商品信息
    int delGoods(String id, String goodsUUId);
}
