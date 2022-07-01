package com.nongXingGang;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.nongXingGang.mapper.*;
import com.nongXingGang.pojo.*;
import com.nongXingGang.pojo.es.DemandEs;
import com.nongXingGang.pojo.es.GoodEs;
import com.nongXingGang.service.BrowseRecordsService;
import com.nongXingGang.service.es.DemandEsDao;
import com.nongXingGang.service.es.GoodEsDao;
import com.nongXingGang.utils.random.RandomValue;
import com.nongXingGang.utils.result.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.Entry;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jsoup.helper.DataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
class NongXingGangApplicationTests {
    @Resource
    private ElasticsearchRestTemplate restTemplate;

    @Resource
    private GoodEsDao goodEsDao;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private BrowseRecordsMapper browseRecordsMapper;

    @Resource
    private BrowseRecordsService browseRecordsService;

    @Resource
    private DemandMapper demandMapper;

    @Resource
    private GoodsImgsMapper goodsImgsMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private DemandEsDao demandEsDao;

    @Autowired
    private ContractOrderMapper contractOrderMapper;
//
//
//    @Test
//    void testOrderCount(){
//        String id = "oDiMW5Uov6rAYjjCH3r6jr7Nok7I";
//        List<Map<String, Object>> maps = contractOrderMapper.selectMaps(new QueryWrapper<ContractOrder>()
//                        .select("order_status","count(1)")
//                .groupBy("order_status")
//                .eq("buyer_openid", id));
//        log.info("count 是"+maps);
//    }
//
//    @Test
//    void testCount(){
//        String id = "oDiMW5WNZLBmK1vPsqE4QbawW0Ng";
//        Integer count = goodsMapper.selectCount(new QueryWrapper<Goods>()
//                .eq("user_openid", id));
//        log.info("count 是"+count);
//    }
//
//
//    @Test
//    void testUser(){
//        userMapper.selectList(null).forEach(System.out::println);
//    }
//
    @Test
    void testInsertGoods(){
        String url = "http://img.chengdashi.cn/img/VCG211302132441.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211220753552.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211187346219.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211126687743.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157615977.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157430678.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211339906728.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157382451.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41155431404.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41135951345.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21400234004.jpg\n" +
                "http://img.chengdashi.cn/img/VCG216886b7b08.jpg\n" +
                "http://img.chengdashi.cn/img/VCG219aeeac577.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41N1127568449.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41N932526888.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157294659.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic20008681.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic19862276.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic13805698.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic12621194.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic12621194.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21fc115ebf4.jpg\n" +
                "http://img.chengdashi.cn/img/1VCG211196608472.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41N910118840.jpg";

        List<String> collect = Arrays.stream(url.split("\n")).collect(Collectors.toList());
        String fruits = "苹果，梨，葡萄，红提，枣，柑橘，柚，桃，西瓜，杏，甜瓜，香瓜，荔枝，甘蔗，柿，柠檬，香蕉，芒果，菠萝，哈密瓜，李，石榴，枸杞，山楂，椰子，桑葚，荸荠，柚子，草莓，沙糖桔，木瓜，橙，圣女果，龙眼，黄瓜，枇杷，山竹，红毛丹，无花果，布朗，杨桃，人参果，猕猴桃，芭乐，杨梅，乌梅，蓝莓，西梅，释迦，百花果，黄皮，樱桃，雪莲果，榴莲，火龙果，番石榴，菠萝蜜，百香果，罗汉果，莲雾";
        List<String> list = Arrays.asList(fruits.split("，"));
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            for (int i = 0; i < 10; i++) {
                Goods goods = new Goods();
                goods.setGoodsUuid(IdUtil.fastUUID());
                goods.setUserOpenid(user.getUserOpenid());
                goods.setGoodsLiaisonMan(RandomValue.getChineseName());
                goods.setGoodsContactNumber(RandomValue.getTel());
                goods.setGoodsType(list.get(RandomUtil.randomInt(0,list.size() - 1)));
                goods.setGoodsVarieties(list.get(RandomUtil.randomInt(0,list.size() - 1)));
                goods.setGoodsPrice(new BigDecimal(RandomUtil.randomInt(0,1000)));
                goods.setGoodsKilogram(new BigDecimal(RandomUtil.randomInt(0,1000)));
                goods.setGoodsProductionArea(RandomValue.getRoad());
                goods.setGoodsMainImgUrl(collect.get(RandomUtil.randomInt(0,collect.size() - 1)));
                goods.setRemark("小赵");
                goods.setGoodsStatus(0);
                //goods.setGoodsNewTime(DateUtil.nextMonth());
                goodsMapper.insert(goods);

                for(int j = 0;j < RandomUtil.randomInt(3,6);j++){
                    GoodsImgs goodsImgs = new GoodsImgs();
                    goodsImgs.setUuid(IdUtil.fastUUID());
                    goodsImgs.setGoodsUuid(goods.getGoodsUuid());
                    goodsImgs.setImgUrl(collect.get(RandomUtil.randomInt(0,collect.size() - 1)));
                    goodsImgsMapper.insert(goodsImgs);
                }


                GoodEs goodEs = new GoodEs();
                goodEs.setGoodsUuid(goods.getGoodsUuid());
                goodEs.setUserOpenid(goods.getUserOpenid());
                goodEs.setGoodsVarieties(goods.getGoodsVarieties());
                goodEs.setGoodsType(goods.getGoodsType());
                goodEs.setGoodsPrice(goods.getGoodsPrice());
                goodEs.setGoodsKilogram(goods.getGoodsKilogram());
                goodEs.setGoodsProductionArea(goods.getGoodsProductionArea());
                goodEs.setGoodsMainImgUrl(goods.getGoodsMainImgUrl());
                goodEs.setRemark(goods.getRemark());
                goodEs.setGoodsStatus(goods.getGoodsStatus());
                goodEs.setGoodsCreateTime(new Date());
                goodEs.setGoodsNewTime(goods.getGoodsNewTime());
//                goodEs.setGoodsNewTime(DateUtil.nextMonth());

                goodEs.setSuggest(new Completion(Arrays.asList(goods.getGoodsType(),goods.getGoodsVarieties())));

                goodEsDao.save(goodEs);
            }
        }


    }

    @Test
    void insertDemand(){
        String url = "http://img.chengdashi.cn/img/VCG211302132441.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211220753552.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211187346219.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211126687743.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157615977.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157430678.jpg\n" +
                "http://img.chengdashi.cn/img/VCG211339906728.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157382451.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41155431404.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41135951345.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21400234004.jpg\n" +
                "http://img.chengdashi.cn/img/VCG216886b7b08.jpg\n" +
                "http://img.chengdashi.cn/img/VCG219aeeac577.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41N1127568449.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41N932526888.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41157294659.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic20008681.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic19862276.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic13805698.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic12621194.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21gic12621194.jpg\n" +
                "http://img.chengdashi.cn/img/VCG21fc115ebf4.jpg\n" +
                "http://img.chengdashi.cn/img/1VCG211196608472.jpg\n" +
                "http://img.chengdashi.cn/img/VCG41N910118840.jpg";

        List<String> collect = Arrays.stream(url.split("\n")).collect(Collectors.toList());
        String fruits = "苹果，梨，葡萄，红提，枣，柑橘，柚，桃，西瓜，杏，甜瓜，香瓜，荔枝，甘蔗，柿，柠檬，香蕉，芒果，菠萝，哈密瓜，李，石榴，枸杞，山楂，椰子，桑葚，荸荠，柚子，草莓，沙糖桔，木瓜，橙，圣女果，龙眼，黄瓜，枇杷，山竹，红毛丹，无花果，布朗，杨桃，人参果，猕猴桃，芭乐，杨梅，乌梅，蓝莓，西梅，释迦，百花果，黄皮，樱桃，雪莲果，榴莲，火龙果，番石榴，菠萝蜜，百香果，罗汉果，莲雾";
        List<String> list = Arrays.asList(fruits.split("，"));
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            for (int i = 0; i < 10; i++) {

                Demand demand = new Demand();
                demand.setDemandUuid(IdUtil.fastUUID());
                demand.setUserOpenid(user.getUserOpenid());
                demand.setDemandVarieties(list.get(RandomUtil.randomInt(0,list.size() - 1)));
                demand.setDemandType(list.get(RandomUtil.randomInt(0,list.size() - 1)));
                demand.setDemandKilogram(new BigDecimal(RandomUtil.randomInt(0,1000)));
                demand.setDemandPrice(new BigDecimal(RandomUtil.randomInt(0,1000)));
                demand.setDemandLisisonMan(RandomValue.getChineseName());
                demand.setDemandContactNumber(RandomValue.getTel());
                demand.setDetailedAddress(RandomValue.getRoad());
                demand.setDemandImgUrl(collect.get(RandomUtil.randomInt(0,collect.size() - 1)));
                demand.setRemarks("小赵需求");
                demand.setDeadline(DateUtil.offsetMonth(DateUtil.date(),10));

                demandMapper.insert(demand);

                DemandEs demandEs = new DemandEs();
                demandEs.setDemandUuid(demand.getDemandUuid());
                demandEs.setDemandVarieties(demand.getDemandVarieties());
                demandEs.setDemandType(demand.getDemandType());
                demandEs.setDemandKilogram(demand.getDemandKilogram());
                demandEs.setDemandPrice(demand.getDemandPrice());
                demandEs.setDetailedAddress(demand.getDetailedAddress());
                demandEs.setDemandImgUrl(demand.getDemandImgUrl());
                demandEs.setRemarks(demand.getRemarks());
                demandEs.setCreateTime(new Date());
                demandEs.setDeadline(demand.getDeadline());

                demandEs.setSuggest(new Completion(Arrays.asList(demand.getDemandType(),demand.getDemandVarieties())));

                demandEsDao.save(demandEs);



            }
        }

    }
//
//
//    //    @Test
////    void testInsertGoodsImgs(){
////        String url = "https://gitee.com/CH-Serendipity/images/raw/master/VCG211302132441-2022-5-6-21:10:46.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG211339906728-2022-5-6-21:10:44.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG211187346219-2022-5-6-21:10:39.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG211126687743-2022-5-6-21:10:37.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41157615977-2022-5-6-21:10:35.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41157430678-2022-5-6-21:10:31.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41157294659-2022-5-6-21:10:25.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41155431404-2022-5-6-21:10:22.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG211220753552-2022-5-6-21:10:20.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG211220753552-2022-5-6-21:10:20.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21400234004-2022-5-6-21:10:05.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG216886b7b08-2022-5-6-21:10:01.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG219aeeac577-2022-5-6-21:09:59.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41N1127568449-2022-5-6-21:09:57.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41N932526888-2022-5-6-21:09:56.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41N910118840-2022-5-6-21:09:53.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG41135951345-2022-5-6-21:09:51.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21gic12621194-2022-5-6-21:09:36.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21gic13805698-2022-5-6-21:09:38.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21gic10679056-2022-5-6-21:09:35.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21fc115ebf4-2022-5-6-21:09:31.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/1VCG211196608472-2022-5-6-21:09:29.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21gic19862276-2022-5-6-21:09:25.jpg\n" +
////                "https://gitee.com/CH-Serendipity/images/raw/master/VCG21gic12621194-2022-5-6-21:09:36.jpg";
////
////        List<String> collect = Arrays.stream(url.split("\n")).collect(Collectors.toList());
////        List<Goods> goodsList = goodsMapper.selectList(null);
////        for (Goods goods : goodsList) {
////            for (int i = 0; i < 3; i++) {
////                GoodsImgs goodsImgs = new GoodsImgs();
////                goodsImgs.setUuid(IdUtil.fastUUID());
////                goodsImgs.setGoodsUuid(goods.getGoodsUuid());
////                goodsImgs.setImgUrl(collect.get(RandomUtil.randomInt(0,collect.size() - 1)));
////                goodsImgsMapper.insert(goodsImgs);
////            }
////
////        }
////    }
////
//
////    @Test
////    void testSearchGoodEs(){
////
////        NativeSearchQuery build = new NativeSearchQueryBuilder()
////                .withQuery(QueryBuilders.queryStringQuery("1").defaultField("goodsStatus"))
////                .withQuery(QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("userOpenid","oDiMW5WNZLBmK1vPsqE4QbawW0Ng")))
////                .withPageable(PageRequest.of(1, 8))
////                .withSorts(SortBuilders.fieldSort("goodsCreateTime").order(SortOrder.DESC))
////                .withSourceFilter(new FetchSourceFilterBuilder().withExcludes("userOpenid","remark","goodsStatus","goodsStatus","goodsNewTime","goodsCreateTime").build())
////                .build();
////
////        List<GoodEs> temp = new ArrayList<>();
////        //搜索
////        SearchHits<GoodEs> search = restTemplate.search(build, GoodEs.class);
////
////        for (SearchHit<GoodEs> searchHit : search.getSearchHits()) {
////            GoodEs content = searchHit.getContent();
////            temp.add(content);
////        }
////        List<Map<String, Object>> result = new ArrayList<>();
////        for (GoodEs goodEs : temp) {
////            Map<String, Object> resultMap = JSON.parseObject(JSON.toJSONString(goodEs), new TypeReference<Map<String, Object>>() {});
////            resultMap.remove("userOpenid");
////            resultMap.remove("remark");
////            resultMap.remove("goodsStatus");
////            resultMap.remove("goodsNewTime");
////            resultMap.remove("goodsCreateTime");
////
////            result.add(resultMap);
////        }
////        for (Map<String, Object> map : result) {
////            System.out.println(map.toString());
////        }
////
////    }
//
//    @Test
//    void testSearch(){
//        int status = 0;
//        int pageNum = 1;
//        int pageSize = 10;
//        String id = "oDiMW5bHOPfTkzDiMSK5wEClGv8k";
//
//        //构建查询条件构造器
//        NativeSearchQuery build = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.boolQuery()
//                        .mustNot(QueryBuilders.termQuery("userOpenid",id))
//                        .must(QueryBuilders.termQuery("goodsStatus",status)))
//                .withPageable(PageRequest.of(pageNum, pageSize))
//                .withSorts(SortBuilders.fieldSort("goodsCreateTime").order(SortOrder.DESC))
//                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("goodsUuid", "goodsStatus", "goodsVarieties", "goodsPrice", "goodsProductionArea", "goodsMainImgUrl").build())
//                .build();
//
////        List<GoodEs> temp = new ArrayList<>();
//        //搜索
//        SearchHits<GoodEs> search = restTemplate.search(build, GoodEs.class);
//        for (SearchHit<GoodEs> searchHit : search.getSearchHits()) {
//            GoodEs content = searchHit.getContent();
//            System.out.println(content);
////            temp.add(content);
//        }
//    }
//
//    @Test
//    void insertStore(){
//        String id = "oDiMW5WNZLBmK1vPsqE4QbawW0Ng";
//        List<Goods> goodsList = goodsMapper.selectList(new QueryWrapper<Goods>().select("goods_uuid","goods_status"));
//        Map<Integer, List<Goods>> collect = goodsList.stream().collect(Collectors.groupingBy(Goods::getGoodsStatus));
//        List<Goods> goodsList1 = collect.get(0);
//        List<Goods> goodsList2 = collect.get(1);
//        List<Demand> demandList = demandMapper.selectList(new QueryWrapper<Demand>().select("demand_uuid"));
//        for (int i = 0; i < 10; i++) {
//            Store store = new Store();
//            store.setColUuid(IdUtil.fastUUID());
//            store.setThingUuid(goodsList1.get(i).getGoodsUuid());
//            store.setUserOpenid(id);
//            store.setThingType(Constants.ON_GOODS);
//            storeMapper.insert(store);
//
//
//            Store store1 = new Store();
//            store1.setColUuid(IdUtil.fastUUID());
//            store1.setThingUuid(goodsList2.get(i).getGoodsUuid());
//            store1.setUserOpenid(id);
//            store1.setThingType(Constants.PRE_GOODS);
//            storeMapper.insert(store1);
//
//            Store store2 = new Store();
//            store2.setColUuid(IdUtil.fastUUID());
//            store2.setThingUuid(demandList.get(i).getDemandUuid());
//            store2.setUserOpenid(id);
//            store2.setThingType(Constants.DEMANDS);
//            storeMapper.insert(store2);
//        }
//
//    }
//
//    @Test
//    void getStoreList(){
//        String id = "oDiMW5WNZLBmK1vPsqE4QbawW0Ng";
//        List<Map<String, Object>> goodsList = storeMapper.selectJoinMaps(new MPJLambdaWrapper<>()
//                .select(Store::getColUuid,Store::getThingType,Store::getColTime)
//                .selectAs(Goods::getGoodsPrice,"thingPrice")
//                .selectAs(Goods::getGoodsMainImgUrl,"imgUrl")
//                .selectAs(Goods::getGoodsVarieties,"varieties")
//                .leftJoin(Goods.class, Goods::getGoodsUuid, Store::getThingUuid)
////                .orderByDesc(Store::getColTime)
//                .eq(Store::getUserOpenid, id)
//                        .ne(Store::getThingType,Constants.DEMANDS)
//        );
//        Map<Object, List<Map<String, Object>>> thingType = goodsList.stream().collect(Collectors.groupingBy(e -> e.get("thingType")));
//
//
//        List<Map<String, Object>> demandList = storeMapper.selectJoinMaps(new MPJLambdaWrapper<>()
//                .select(Store::getColUuid,Store::getThingType,Store::getColTime)
//                        .selectAs(Demand::getDemandPrice,"thingPrice")
//                        .selectAs(Demand::getDemandImgUrl,"imgUrl")
//                        .selectAs(Demand::getDemandVarieties,"varieties")
//                .leftJoin(Demand.class, Demand::getDemandUuid, Store::getThingUuid)
////                .orderByDesc(Store::getColTime)
//                .eq(Store::getUserOpenid, id)
//                .eq(Store::getThingType,Constants.DEMANDS));
//
////        List<Map<String, Object>> goodsOn = thingType.get(0);
////        List<Map<String, Object>> goodsPre = thingType.get(1);
//
////        //需求
////        System.out.println("================需求========================");
////        for (Map<String, Object> map : demandList) {
////            System.out.println(map);
////        }
////
////        System.out.println("================在售========================");
////        for (Map<String, Object> map : goodsOn) {
////            System.out.println(map);
////        }
////
////        System.out.println("================预售========================");
////        for (Map<String, Object> map : goodsPre) {
////            System.out.println(map);
////        }
//
//        List<Map<String, Object>> result = new ArrayList<>();
////        Stream.of(goodsOn,goodsPre,demandList).forEach(result::addAll);
//
//        List<Map<String, Object>> collect = Stream.of(goodsList, demandList)
//                .flatMap(Collection::stream)
//                .sorted((o1,o2) -> {
//                    Date colTime1 = (Date) o1.get("colTime");
//                    Date colTime2 = (Date) o2.get("colTime");
////                    String substring = colTime1.substring(0, colTime1.lastIndexOf("."));
////                    String substring2 = colTime2.substring(0, colTime1.lastIndexOf("."));
//                    return DateUtil.compare(colTime1,colTime2);
//                })
//                .collect(Collectors.toList());
//
////        List<Map<String, Object>> collect = result.stream().sorted(((o1, o2) -> {
////
//////                DateTime coTime = DateUtil.parse((String) o1.get("coTime"));
//////                DateTime coTime2 = DateUtil.parse((String) o1.get("coTime"));
////            String coTime = (String) o1.get("coTime");
////            String coTime2 = (String) o2.get("coTime");
////            return coTime.compareTo(coTime2);
////
////        })).collect(Collectors.toList());
//
////        result.sort(((o1, o2) -> {
////
////                DateTime coTime = DateUtil.parse((String) o1.get("coTime"));
////                DateTime coTime2 = DateUtil.parse((String) o1.get("coTime"));
//////            String coTime = (String) o1.get("coTime");
//////            String coTime2 = (String) o2.get("coTime");
////            return DateUtil.compare(coTime,coTime2);
////        }
////        ));
//
////        for (Map<String, Object> map : collect) {
//////            System.out.println(map.get("colTime"));
////            System.out.println(map);
////        }
//        for (Map<String, Object> map : goodsList) {
//            System.out.println(map);
//        }
//
//
//    }
}

