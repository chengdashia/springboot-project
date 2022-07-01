package com.nongXingGang.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nongXingGang.mapper.BrowseRecordsMapper;
import com.nongXingGang.mapper.GoodsImgsMapper;
import com.nongXingGang.mapper.StoreMapper;
import com.nongXingGang.pojo.BrowseRecords;
import com.nongXingGang.pojo.Goods;
import com.nongXingGang.mapper.GoodsMapper;
import com.nongXingGang.pojo.GoodsImgs;
import com.nongXingGang.pojo.Store;
import com.nongXingGang.pojo.es.GoodEs;
import com.nongXingGang.pojo.request.GoodsBody;
import com.nongXingGang.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.service.es.GoodEsDao;
import com.nongXingGang.utils.file.FileUtil;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private GoodsImgsMapper goodsImgsMapper;

    @Resource
    private GoodEsDao goodEsDao;

    @Resource
    private BrowseRecordsMapper browseRecordsMapper;

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 获取首页数据
     * @param id                  用户id
     * @param status              状态 ，在售，预售
     * @param pageNum             页码
     * @param pageSize             页数
     * @return    R
     */
    @Override
    public Map<String, Object> getIndexGoods(String id, int status, int pageNum, int pageSize) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<>();
            Page<Map<String, Object>> mapPage = goodsMapper.selectMapsPage(new Page<>(pageNum + 1, pageSize,false)
                    , new QueryWrapper<Goods>()
                            .select("goods_uuid", "goods_status", "goods_varieties", "goods_price", "goods_production_area", "goods_main_img_url")
                            .eq("goods_status",status)
                            .ne("user_openid",id)
                            .orderByDesc("goods_create_time"));
            if(mapPage != null){
                map.put("status", StatusType.SUCCESS);
                map.put("data",mapPage);
            }
        } catch (Exception e) {
            map.put("status", StatusType.SQL_ERROR);
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取商品的详细数据
     * @param id          用户id
     * @param goodsUUId   商品的id
     * @return       R
     */
    @Override
    public R getGoodsDetails(String id, String goodsUUId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> maps = goodsMapper.selectMaps(new QueryWrapper<Goods>()
                    .select(Goods.class, i -> !i.getColumn().equals("goods_uuid") && !i.getColumn().equals("logical_deletion") && !i.getColumn().equals("user_openid"))
                    .eq("goods_uuid", goodsUUId));
            if (maps != null){
                try {
                    List<Map<String, Object>> goodsImgList = goodsImgsMapper.selectMaps(new QueryWrapper<GoodsImgs>()
                            .select("img_url","uuid")
                            .eq("goods_uuid", goodsUUId));
                    if(goodsImgList != null){
                        Store store = storeMapper.selectOne(new QueryWrapper<Store>()
                                .eq("user_openid", id)
                                .eq("thing_uuid", goodsUUId));
                        map.put("data",maps);
                        map.put("imgs",goodsImgList);
                        if (store == null){
                            map.put("colStatus",Constants.NOT_STORE);
                        }else {
                            map.put("colStatus",Constants.STORED);
                        }
                        Object goodsStatus = maps.get(0).get("goodsStatus");
                        addBrowseRecords(id,goodsUUId,goodsStatus);
                        return R.ok(map);
                    }
                    return R.notExists();
                } catch (Exception e) {
                    e.printStackTrace();
                    return R.sqlError();
                }
            }else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    /**
     * 添加商品
     * @param openid   用户id
     * @param goodsBody    商品
     * @return  R
     */
    @Override
    public R addGoods(String openid, GoodsBody goodsBody) {
        Goods goods = new Goods();
        String goodsUUId = IdUtil.simpleUUID();
        goods.setGoodsUuid(goodsUUId);
        goods.setUserOpenid(openid);
        getGoods(goods,goodsBody);
        int insert = goodsMapper.insert(goods);
        if(insert == 1){
            int result = 1;
            for(String url : goodsBody.getFileList()){
                GoodsImgs goodsImgs = getGoodsImgs(goodsUUId, url);
                int i = goodsImgsMapper.insert(goodsImgs);
                if(i == 1){
                    result++;
                }else {
                    return R.error();
                }
            }
            if(result == 1 + goodsBody.getFileList().size()){
                GoodEs goodEs = getGoodEs(goodsBody, goodsUUId);
                goodEsDao.save(goodEs);
                return R.ok();
            }else {
                return R.error();
            }
        }else {
            return R.error();
        }
    }

    /**
     * 修改商品
     * @param openid   用户id
     * @param goodsBody  商品
     * @return     R
     */
    @Override
    public R updateGoods(String openid, GoodsBody goodsBody) {
        String goodsUUId = goodsBody.getGoodsUUId();
        Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>()
                .eq("goods_uuid", goodsUUId));

        if (goods != null){
            //对商品体进行修改
            Goods updateGoods = getGoods(goods,goodsBody);

            int i = goodsMapper.updateById(updateGoods);

            //如果修改成功
            if(i == 1){
                //新的图片列表
                List<String> imgList = goodsBody.getFileList();

                //数据库的图片的对象
                List<GoodsImgs> goodsImgList = goodsImgsMapper.selectList(new QueryWrapper<GoodsImgs>()
                        .eq("goods_uuid", goodsUUId));

                //不能为空
                if(goodsImgList != null){

                    //将sql对象中的图片的地址提取出来
                    List<String> collect = goodsImgList.stream().map(GoodsImgs::getImgUrl).collect(toList());

                    //差集     如果新的图片列表中没有的进行插入
                    List<String> imgNewList = imgList.stream().filter(item -> !collect.contains(item)).collect(toList());
                    int j = 0;
                    for (String s : imgNewList) {
                        GoodsImgs goodsImgs = new GoodsImgs();
                        goodsImgs.setGoodsUuid(goodsUUId);
                        goodsImgs.setUuid(IdUtil.simpleUUID());
                        goodsImgs.setImgUrl(s);
                        goodsImgsMapper.insert(goodsImgs);
                        j++;
                    }
                    //全部插入之后 进行删除
                    if(j == imgNewList.size()){
                        //没有的   进行删除
                        List<String> imgDelList = collect.stream().filter(item -> !imgList.contains(item)).collect(toList());
                        int x = 0;
                        for (String s : imgDelList) {
                            goodsImgsMapper.delete(new QueryWrapper<GoodsImgs>().eq("img_url", s));

                            //将存储的图片地址 删除
                            FileUtil.delete(s);
                            x++;
                        }
                        if(x == imgDelList.size()){
                            Goods goods1 = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("goods_uuid", goodsUUId));
                            goodEsDao.save(getGoodEs(goods1));
                            return R.ok();
                        }else {
                            return R.error();
                        }
                    }else {
                        return R.error();
                    }
                }else {
                    return R.error();
                }
            }else {
                return R.error();
            }
        }else {
            for (String s : goodsBody.getFileList()) {
                FileUtil.delete(s);
            }
            return R.notExists();
        }

    }

    /**
     * 删除商品
     * @param id             用户id
     * @param goodsUUId      商品的id
     * @return R
     */
    @Override
    public R delGoods(String id, String goodsUUId) {
        int delete = 0;
        try {
            delete = goodsMapper.delete(new QueryWrapper<Goods>()
                    .eq("user_openid", id)
                    .eq("goods_uuid", goodsUUId));
            if (delete == 1){
                restTemplate.delete(goodsUUId, GoodEs.class);
                return R.ok();
            }else {
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    /**
     * 我的发布商品的数量
     * @return                 R
     */
    @Override
    public R getMyPublishGoodsNum(String id) {
        Integer count = goodsMapper.selectCount(new QueryWrapper<Goods>()
                .eq("user_openid", id));
        return R.ok(count);
    }


    /**
     * 添加浏览记录
     * @param id
     * @param goodsUUId
     * @param goodsStatus
     */
    public void addBrowseRecords(String id, String goodsUUId, Object goodsStatus){
        BrowseRecords one = browseRecordsMapper.selectOne(new QueryWrapper<BrowseRecords>()
                .eq("thing_uuid", goodsUUId)
                .eq("user_openid", id));
        if(one != null){
            one.setCreateTime(new Date());
            browseRecordsMapper.updateById(one);
        }else {
            Integer count = browseRecordsMapper.selectCount(new QueryWrapper<BrowseRecords>().eq("user_openid", id));
            if(count > 200){
                List<BrowseRecords> browseRecordsList = browseRecordsMapper.selectPage(new Page<>(200, count-200),
                        new QueryWrapper<BrowseRecords>()
                                .orderByAsc("create_time")).getRecords();
                for (BrowseRecords browseRecords : browseRecordsList) {
                    browseRecordsMapper.deleteById(browseRecords);
                }
            }else {
                BrowseRecords browseRecords = new BrowseRecords();
                browseRecords.setThingUuid(goodsUUId);
                browseRecords.setBrUuid(IdUtil.fastUUID());
                browseRecords.setUserOpenid(id);
                if (goodsStatus.equals(Constants.ON_GOODS)){
                    browseRecords.setThingType(Constants.ON_GOODS);
                }
                browseRecords.setThingType(Constants.PRE_GOODS);
                browseRecordsMapper.insert(browseRecords);
            }
        }

    }

    public static Goods getGoods(Goods goods,GoodsBody goodsBody){
        goods.setGoodsLiaisonMan(goodsBody.getLiaisonMan());
        goods.setGoodsType(goodsBody.getType());
        goods.setGoodsVarieties(goodsBody.getVarieties());
        goods.setGoodsPrice(goodsBody.getPrice());
        goods.setGoodsKilogram(goodsBody.getKilogram());
        goods.setGoodsProductionArea(goodsBody.getProductionArea());
        goods.setGoodsMainImgUrl(goodsBody.getFileList().get(0));
        goods.setRemark(goodsBody.getRemarks());
        goods.setGoodsStatus(goodsBody.getStatus());
        goods.setGoodsCreateTime(new Date());
        goods.setGoodsContactNumber(goodsBody.getContactNumber());
        goods.setGoodsNewTime(goodsBody.getGoodsNewTime());
        return goods;
    }

    public static GoodsImgs getGoodsImgs(String goodsUuid,String imgUrl){
        GoodsImgs goodsImgs = new GoodsImgs();
        goodsImgs.setGoodsUuid(goodsUuid);
        goodsImgs.setUuid(IdUtil.fastUUID());
        goodsImgs.setImgUrl(imgUrl);
        return goodsImgs;
    }

    //获取goodEs
    public static GoodEs getGoodEs(GoodsBody goods,String goodsUuid){
        GoodEs goodEs = new GoodEs();
        goodEs.setGoodsUuid(goodsUuid);
        goodEs.setGoodsVarieties(goods.getVarieties());
        goodEs.setGoodsType(goods.getType());
        goodEs.setGoodsPrice(goods.getPrice());
        goodEs.setGoodsKilogram(goods.getKilogram());
        goodEs.setGoodsProductionArea(goods.getProductionArea());
        goodEs.setGoodsMainImgUrl(goods.getFileList().get(0));
        goodEs.setRemark(goods.getRemarks());
        goodEs.setGoodsStatus(goods.getStatus());
        goodEs.setGoodsCreateTime(new Date());
        goodEs.setGoodsNewTime(goods.getGoodsNewTime());
        return goodEs;
    }

    public static GoodEs getGoodEs(Goods goods){
        GoodEs goodEs = new GoodEs();
        goodEs.setGoodsUuid(goods.getGoodsUuid());
        goodEs.setGoodsVarieties(goods.getGoodsVarieties());
        goodEs.setGoodsType(goods.getGoodsType());
        goodEs.setGoodsPrice(goods.getGoodsPrice());
        goodEs.setGoodsKilogram(goods.getGoodsKilogram());
        goodEs.setGoodsProductionArea(goods.getGoodsProductionArea());
        goodEs.setGoodsMainImgUrl(goods.getGoodsMainImgUrl());
        goodEs.setRemark(goods.getRemark());
        goodEs.setGoodsStatus(goods.getGoodsStatus());
        goodEs.setGoodsCreateTime(goods.getGoodsCreateTime());
        goodEs.setGoodsNewTime(goods.getGoodsNewTime());
        return goodEs;
    }

}
