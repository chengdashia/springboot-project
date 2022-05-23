package com.onlineExam.controller;


import com.onlineExam.service.AnswerInfoService;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.result.StatusType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 成大事
 * @since 2022-02-19
 */
@Api(tags = "答案")
@RestController
@RequestMapping("/answerInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AnswerInfoController {

    private final AnswerInfoService answerInfoService;

    @ApiOperation("保存  答案保存")
    @PostMapping("/saveAnswer")
    public R answerSave(@ApiParam(value = "试卷的uuid",required = true) @RequestParam("testUUId") @NotNull @NotBlank(message = "不能为空") String testUUId){
        int i = answerInfoService.testPaperSave(testUUId);
        if(i == StatusType.SUCCESS){
            return R.success();
        }else if (i == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.failure();
        }
    }

}

