package com.onlineExam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineExam.pojo.ScoreInfo;
import com.onlineExam.pojo.question.RequestAnswerBody;
import com.onlineExam.pojo.question.StuAnswer;
import com.onlineExam.service.ScoreInfoService;
import com.onlineExam.utils.date.ConvertorTime;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-02-21
 */
@Api(tags = "成绩")
@RestController
@RequestMapping("/scoreInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ScoreInfoController {

    private final ScoreInfoService scoreInfoService;

    @ApiOperation("导出成绩")
    @GetMapping("/export")
    public void export(@ApiParam(value = "试卷的uuid",required = true) @RequestParam("testUUId") @NotBlank(message = "不能为空") @NotNull String testUUId,
                       HttpServletResponse response){
        scoreInfoService.export(testUUId,response);
    }

    @ApiOperation("提交答案")
    @PostMapping("/submitAnswer")
    public R submitAnswer(HttpServletRequest request,
                          @RequestBody @Validated RequestAnswerBody requestAnswerBody){
        String studentId = JWTUtils.getEncryptStuNo(request);
        int i = scoreInfoService.addScore(studentId, requestAnswerBody);
        if(i == StatusType.SUCCESS){
            return R.success();
        }else if(i == StatusType.NOT_EXISTS){
            return R.notExists();
        }else {
            return R.failure();
        }
    }



    @ApiOperation("新版  提交答案")
    @PostMapping("/newSubmitAnswer")
    public R newSubmitAnswer(HttpServletRequest request,
                          @RequestBody @Validated StuAnswer stuAnswer){
        String studentId = JWTUtils.getEncryptStuNo(request);
        int i = scoreInfoService.newAddScore(studentId, stuAnswer);
        if(i == StatusType.SUCCESS){
            return R.success();
        }else if(i == StatusType.NOT_EXISTS){
            return R.notExists();
        }  else {
            return R.failure();
        }
    }




    @ApiOperation("获取成绩列表分页")
    @PostMapping("/getScoreListPage")
    public R getScoreList(
            @ApiParam(value = "试卷的uuid",required = true) @RequestParam("testUUId") @NotBlank(message = "不能为空") @NotNull String testUUId,
            @ApiParam(value = "页数",required = true) @RequestParam("pageNum") @NotNull Integer pageNum,
            @ApiParam(value = "数量",required = true) @RequestParam("pageSize") @NotNull Integer pageSize){
        List<Map<String, Object>> scoreListPage = scoreInfoService.getScoreListPage(testUUId, pageNum, pageSize);
        if (scoreListPage != null){
            return R.success(scoreListPage);
        }else {
            return R.failure();
        }
    }

    @ApiOperation("获取成绩列表不分页")
    @PostMapping("/getScoreList")
    public R getScoreList(@ApiParam(value = "试卷的uuid",required = true) @RequestParam("testUUId") @NotBlank(message = "不能为空") String testUUId){
        List<Map<String, Object>> scoreList = scoreInfoService.getScoreList(testUUId);
        if (scoreList != null){
            return R.success(scoreList);
        }else {
            return R.failure();
        }
    }

    @ApiOperation("查看成绩")
    @PostMapping("/lookMyScore")
    public R lookMyScore(
            HttpServletRequest request
    ){
        String encryptStuNo = JWTUtils.getEncryptStuNo(request);
        try {
            Map<String, Object> map = scoreInfoService.getMap(new QueryWrapper<ScoreInfo>()
                    .select("score", "completion_time","create_time")
                    .eq("student_id", encryptStuNo));
            if(map != null){
                map.put("completionTime", ConvertorTime.secToTime((Integer) map.get("completionTime")));
                return R.success(map);
            }else {
                return R.failure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }




}

