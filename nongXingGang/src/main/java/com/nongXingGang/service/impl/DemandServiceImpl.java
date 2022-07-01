package com.nongXingGang.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nongXingGang.mapper.BrowseRecordsMapper;
import com.nongXingGang.mapper.StoreMapper;
import com.nongXingGang.pojo.BrowseRecords;
import com.nongXingGang.pojo.Demand;
import com.nongXingGang.mapper.DemandMapper;
import com.nongXingGang.pojo.Store;
import com.nongXingGang.pojo.es.DemandEs;
import com.nongXingGang.service.DemandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.service.es.DemandEsDao;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
@Slf4j
@Service
public class DemandServiceImpl extends ServiceImpl<DemandMapper, Demand> implements DemandService {

    @Resource
    private DemandMapper demandMapper;

    @Resource
    private DemandEsDao demandEsDao;

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    @Resource
    private StoreMapper storeMapper;

    /**
     * 获取需求数据
     * @param pageNum              页码
     * @param pageSize             页数
     * @return                     R
     */
    @Override
    public R getDemandGoods(int pageNum, int pageSize) {
        log.info("获取需求：{}",pageNum * pageSize);
        log.info("获取需求{}",pageSize);
        try {
            Page<Map<String, Object>> mapPage = demandMapper.selectMapsPage(new Page<>(pageNum + 1, pageSize,false), new QueryWrapper<Demand>()
                    .select("demand_uuid", "demand_type", "demand_varieties", "demand_kilogram", "demand_img_url", "deadline","detailed_address")
                    .orderByDesc("create_time"));
            if(mapPage != null){
                return R.ok(mapPage);
            }else {
               return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     *  获取需求的详细数据
     * @param demandUUId  需求的uuid
     * @return R
     */
    @Override
    public R getNeedDetails(String demandUUId) {
        String id = (String) StpUtil.getLoginId();
        Map<String, Object> map = new HashMap<>();
        try {
            Demand demand = demandMapper.selectOne(new QueryWrapper<Demand>()
                    .eq("demand_uuid", demandUUId));
            if(demand != null){
                Store store = storeMapper.selectOne(new QueryWrapper<Store>()
                        .eq("user_openid", id)
                        .eq("thing_uuid", demandUUId));
                if(store != null){
                    map.put("colStatus", Constants.STORED);
                }else {
                    map.put("colStatus", Constants.NOT_STORE);
                }
                map.put("data",demand);
                addBrowseRecords(id,demandUUId);
                return R.ok(map);
            }
            else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 添加需求
     * @param openid                 用户id
     * @param demand                  需求
     * @return                        R
     */
    @Override
    public R addDemand(String openid, Demand demand) {
        demand.setDemandUuid(IdUtil.simpleUUID());
        demand.setUserOpenid(openid);
        demand.setCreateTime(new Date());
        int insert = demandMapper.insert(demand);
        if(insert == 1){
            DemandEs demandEs = getDemandEs(demand);
            demandEsDao.save(demandEs);
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改需求
     * @param openid                   用户id
     * @param demand                   需求
     * @return                           R
     */
    @Override
    public R updateDemand(String openid, Demand demand) {
            demand.setUserOpenid(openid);
            int i = demandMapper.updateById(demand);
            if(i == 1){
                DemandEs demandEs = getDemandEs(demand);
                demandEsDao.save(demandEs);
                return R.ok();
            }
            return R.error();
    }

    /**
     * 删除需求信息
     * @param id                     用户id
     * @param demandUUId             需求的uuid
     * @return                         R
     */
    @Override
    public R delDemand(String id, String demandUUId) {
        try {
            int delete = demandMapper.delete(new QueryWrapper<Demand>()
                    .eq("user_openid", id)
                    .eq("demand_uuid", demandUUId));
            if (delete == 1){
                restTemplate.delete(demandUUId,DemandEs.class);
                return R.ok();
            }
            return R.notExists();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }


    //将Demand 实体类转成 DemandES
    public static DemandEs getDemandEs(Demand demand){
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
        return demandEs;

    }


    @Resource
    private BrowseRecordsMapper browseRecordsMapper;

    /**
     * 添加浏览记录
     * @param id               用户id
     * @param demandUUId       需求的uuid
     */
    public void addBrowseRecords(String id, String demandUUId){
        BrowseRecords one = browseRecordsMapper.selectOne(new QueryWrapper<BrowseRecords>()
                .eq("thing_uuid", demandUUId)
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
                browseRecords.setThingUuid(demandUUId);
                browseRecords.setBrUuid(IdUtil.fastUUID());
                browseRecords.setUserOpenid(id);
                browseRecords.setThingType(Constants.DEMANDS);
                browseRecordsMapper.insert(browseRecords);
            }
        }

    }
}
