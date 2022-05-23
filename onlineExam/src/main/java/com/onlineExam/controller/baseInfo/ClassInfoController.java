package com.onlineExam.controller.baseInfo;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineExam.pojo.ClassInfo;
import com.onlineExam.service.ClassInfoService;
import com.onlineExam.utils.redis.RedisUtil;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.safe.SecureDESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
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
@Api(tags = "班级信息")
@RestController
@RequestMapping("/classInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ClassInfoController {


    private final ClassInfoService classInfoService;


    private final RedisUtil redisUtil;


    @GetMapping("/addClass")
    public void addClass(@RequestParam("instituteUUId") String instituteUUId){
        for(int i = 1;i < 5;i++){
            ClassInfo classInfo = new ClassInfo();
            classInfo.setClassUuid(IdUtil.simpleUUID());
            classInfo.setClassName("大数据19"+i);
            classInfo.setInstituteUuid(instituteUUId);
            classInfo.setClassStudentsNum(0);
            classInfoService.save(classInfo);
        }
    }


    /**
     * 通过学院的id 获取该学院的班级信息
     * @param instituteUUId  学院的uuid
     * @return GlobalResult
     */
    @ApiOperation("根据学院获取班级信息")
    @PostMapping("/getClass")
    public R getClass(@RequestParam("instituteUUId") String instituteUUId){
        if(redisUtil.hasKey(instituteUUId)){
            return R.success(redisUtil.get(instituteUUId));
        }
        try {
            List<Map<String, Object>> maps = classInfoService.listMaps(new QueryWrapper<ClassInfo>()
                    .select("class_uuid", "class_name")
                    .eq("institute_uuid", instituteUUId));
            for(Map<String,Object> map : maps){
                map.put("className", SecureDESUtil.decrypt(String.valueOf(map.get("className"))));
            }
            Collections.sort(maps, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    return String.valueOf(o1.get("className")).compareTo(String.valueOf(o2.get("className")));
                }
            });
            System.out.println(maps);
            redisUtil.set(instituteUUId,maps,60*60*60*12*10);
            return R.success(maps);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

}

