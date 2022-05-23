package com.onlineExam.controller.baseInfo;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineExam.pojo.InstituteInfo;
import com.onlineExam.service.InstituteInfoService;
import com.onlineExam.utils.redis.RedisUtil;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.safe.SecureDESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-02-17
 */
@Api(tags = "学院信息")
@RestController
@RequestMapping("/instituteInfo")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InstituteInfoController {


    private final RedisUtil redisUtil;


    private final InstituteInfoService instituteInfoService;

    @Test
    public void InsertInfo() {
        String str = "机械工程学院、材料与能源工程学院、化学工程学院、大数据学院、人工智能与电气工程学院、食品药品制造工程学院、航空航天工程学院、矿业工程学院、资源与环境工程学院、土木工程学院、建筑与城市规划学院、交通工程学院、理学院、经济管理学院、马克思主义学院、体育学院、外国语学院、国际教育学院、继续教育学院";
        String[] institutes = str.split("、");
        for (int i = 0; i < institutes.length; i++) {
            InstituteInfo info = new InstituteInfo();
            info.setInstituteUuid(IdUtil.simpleUUID());
            info.setInstituteName(institutes[i]);
            info.setInstituteStudentNums(0);
            instituteInfoService.save(info);
//            System.out.println(institutes[i]);
        }

    }

    /**
     * 如果数据库中没有这个同学的学号。则直接让他选择学院添加
     * @return GlobalResult
     */
    @ApiOperation("获取学院的列表")
    @GetMapping("/getInstituteList")
    public R getInstituteList(){
        if(redisUtil.hasKey("instituteList")){
            return R.success(redisUtil.get("instituteList"));
        }
        try {
            List<Map<String, Object>> maps = instituteInfoService.listMaps(new QueryWrapper<InstituteInfo>()
                    .select("institute_uuid", "institute_name"));
            for(Map<String, Object> map : maps){
                map.put("instituteName", SecureDESUtil.decrypt(String.valueOf(map.get("instituteName"))));
            }
            redisUtil.set("instituteList",maps,60*60*60*12*10);
            return R.success(maps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.failure();
    }

}