package com.cloudDisk.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudDisk.pojo.FileInfo;
import com.cloudDisk.pojo.LabelInfo;
import com.cloudDisk.service.LabelInfoService;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.globalResult.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 成大事
 * @since 2022-04-14
 */
@Api(tags = "标签信息")
@RestController
@RequestMapping("/labelInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LabelInfoController {

    private final LabelInfoService labelInfoService;

    /**
     * 随机获取二十条标签信息
     * @return  R
     */
    @ApiOperation("随机获取二十条标签信息")
    @PostMapping("/getLabelInfoRandom20")
    public R getLabelInfoRandom20(){

        try {
            int count = labelInfoService.count();
            int random = RandomUtil.randomInt(0, count - 20);
            Page<Map<String, Object>> mapPage = labelInfoService.getBaseMapper().selectMapsPage(new Page<>(random, 20),
                    new QueryWrapper<LabelInfo>()
                            .select("interest_label_id", "label_name"));
            if(mapPage != null){
                return R.ok(mapPage);
            }else {
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }



}

