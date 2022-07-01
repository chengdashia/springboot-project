package com.nongXingGang.service;

import com.nongXingGang.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nongXingGang.pojo.request.GoodsBody;
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
public interface GoodsService extends IService<Goods> {

    /**
     * 获取首页数据
     * @param id                  用户id
     * @param status              状态 ，在售，预售
     * @param pageNum             页码
     * @param pageSize             页数
     * @return                      R
     */
    Map<String ,Object> getIndexGoods(String id,int status, int pageNum, int pageSize);

    /**
     * 获取商品的详细数据
     * @param id          用户id
     * @param goodsUUId   商品的id
     * @return   R
     */
    R getGoodsDetails(String id, String goodsUUId);

    /**
     * 添加商品
     * @param openid   用户id
     * @param goods    商品
     * @return            R
     */
    R addGoods(String openid,GoodsBody goods);

    /**
     * 修改商品
     * @param openid   用户id
     * @param goods  商品
     * @return            R
     */
    R updateGoods(String openid, GoodsBody goods);

    /**
     * 删除商品信息
     * @param id             用户id
     * @param goodsUUId      商品的id
     * @return                 R
     */
    R delGoods(String id, String goodsUUId);

    /**
     * 我的发布商品的数量
     * @return                 R
     */
    R getMyPublishGoodsNum(String id);
}
