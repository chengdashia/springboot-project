package com.onlineExam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineExam.pojo.TestPaper;
import com.onlineExam.service.TestPaperService;
import com.onlineExam.utils.constants.QuestionCode;
import com.onlineExam.utils.result.CodeType;
import com.onlineExam.utils.result.MsgType;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
 * @since 2022-02-19
 */
@Api(tags = "试卷")
@RestController
@RequestMapping("/testPaper")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestPaperController {


    private final TestPaperService testPaperService;

    /**
     * 获取试卷列表
     * @return GlobalResult
     */
    @ApiOperation(value = "获取试卷的列表")
    @GetMapping("/allTest")
    public R getAllTest(){
        try {
            List<TestPaper> list = testPaperService.list(new QueryWrapper<TestPaper>().eq("is_del",QuestionCode.IS_DEL_NOT).orderByAsc("create_time"));
            return R.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 学生获取试卷列表
     * @return R
     * @param request  获取token
     */
    @ApiOperation(value = "学生获取试卷的列表")
    @GetMapping("/stuGetTest")
    public R stuGetTest(HttpServletRequest request){
        String studentId = JWTUtils.getEncryptStuNo(request);
        Map<String, Object> map = testPaperService.stuGetTest(studentId);
        int status = (int) map.get("status");
        if(status == StatusType.SUCCESS){
            return R.success(map.get("data"));
        }else if(status == StatusType.SQL_ERROR){
            return R.failure();
        }else {
            return R.success(map);
        }

    }

    /**
     *  试卷添加
     * @param testName                               试卷的名称
     * @param testDescription                        试卷的描述
     * @param testFounder                            出题人
     * @return     GlobalResult->  该试卷的uuid
     */
    @ApiOperation(value = "创建试卷，添加试卷")
    @PostMapping("/addTest")
    public R addTest(@ApiParam("试卷名称")@RequestParam("testName") String testName,
                     @ApiParam ("试卷的描述")@NotBlank(message = "试卷的描述不能为空")  @RequestParam("testDescription") String testDescription,
                     @ApiParam ("出题人") @NotBlank(message = "出题人不能为空") @RequestParam("testFounder") String testFounder){
        Map<String, Object> map = testPaperService.addTest(testName, testDescription, testFounder);
        int result = (int) map.get("status");
        if(result == StatusType.SUCCESS){
            return R.success(map.get("data"));
        }else if(result == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.failure();
        }

    }



    /**
     *  题型分值的修改  添加
     * @param testUUId                          试卷的名称
     * @param questionType                     试卷的描述
     * @param score                            出题人
     * @return     GlobalResult->  该试卷的uuid
     */
    @ApiOperation(value = "修改题型的分值")
    @GetMapping("/updateScore")
    public R updateScore(@NotNull @ApiParam("试卷uuid") @RequestParam("testUUId") String testUUId,
                         @Range(min = 0,max = 2,message = "必须在1-3之间") @ApiParam ("题型") @RequestParam("questionType") int questionType,
                         @Range(min = 1,max = 10,message = "必须在1-10之间") @ApiParam ("要修改的分值") @RequestParam("score") int score){
        int result = testPaperService.updateScore(testUUId, questionType, score);
        if(result == StatusType.SUCCESS){
            return R.success();
        }else if(result == StatusType.QUESTION_TYPE_ERROR){
            return R.failure("题目类型有问题");
        }else {
            return R.failure();
        }
    }



    /**
     * 通过testUUId 获取整张试卷
     * @param testUUId  试卷的uuid
     * @return  GlobalResult
     */
    @ApiOperation(value = "学生获取试卷体")
    @GetMapping("/studentGetTestPaperBody")
    public R studentGetTestPaperBody(HttpServletRequest request,
                                     @NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId){
        String studentId = JWTUtils.getEncryptStuNo(request);
        Map<String, Object> map = testPaperService.studentGetTestPaperBody(studentId, testUUId);
        int status = (int) map.get("status");
        if(status == StatusType.SUCCESS){
            return R.success(map.get("data"));
        }else{
            return R.failure();
        }

    }

    /**
     * 通过testUUId 获取整张试卷
     * @param testUUId  试卷的uuid
     * @return  GlobalResult
     */
    @ApiOperation(value = "新的   学生获取试卷体")
    @GetMapping("/studentNewGetTestPaperBody")
    public R studentNewGetTestPaperBody(HttpServletRequest request,
                                     @NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId){
        String studentId = JWTUtils.getEncryptStuNo(request);
        Map<String, Object> map = testPaperService.studentNewGetTestPaperBody(studentId, testUUId);
        int status = (int) map.get("status");
        if(status == StatusType.SUCCESS){
            return R.success(map.get("data"));

        }else{
            return R.failure();
        }

    }


    /**
     * 通过testUUId 获取整张试卷
     * @param testUUId  试卷的uuid
     * @return  GlobalResult
     */
    @ApiOperation(value = "获取试卷体")
    @GetMapping("/adminGetTestPaperBody")
    public R adminGetTestPaperBody(@NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId){
        Map<String, Object> map = testPaperService.adminGetTestPaperBody(testUUId);
        int status = (int) map.get("status");
        if(status == StatusType.SUCCESS){
            return R.success(map.get("data"));
        }else{
            return R.failure();
        }
    }


    /**
     * 通过testUUId 逻辑删除试卷
     * @param testUUId  试卷的uuid
     * @return  GlobalResult
     */
    @ApiOperation(value = "通过testUUId 逻辑删除试卷")
    @PostMapping("/delTestPaper")
    public R updateTestPaper(@NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId){
        int result = testPaperService.delTestPaper(testUUId);
        if(result == StatusType.SUCCESS){
            return R.success();
        }else{
            return R.failure();
        }
    }


    /**
     * 通过testUUId 更改试卷状态
     * @param testUUId  试卷的uuid
     * @return  GlobalResult
     */
    @ApiOperation(value = "通过testUUId 更改试卷状态")
    @PostMapping("/updateTestPaperStatus")
    public R updateTestPaperStatus(@NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId){
        int result = testPaperService.updateTestPaperStatus(testUUId);
        if(result == StatusType.SUCCESS){
            return R.success();
        }else{
            return R.failure();
        }
    }


    /**
     * 通过testUUId 获取试卷的题目数量和分值
     * @param testUUId  试卷的uuid
     * @return  R
     */
    @ApiOperation(value = "通过testUUId 获取试卷的题目数量和分值")
    @GetMapping("/getTestPaperQuestionNumAndScore")
    public R getTestPaperQuestionNumAndScore(@NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId){
        try {
            Map<String, Object> map = testPaperService.getMap(new QueryWrapper<TestPaper>()
                    .select("single_num", "single_score", "multiple_num", "multiple_score", "judgment_num", "judgment_score")
                    .eq("test_uuid", testUUId));
            if(map != null){
                return R.success(map);
            }else {
                return R.failure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }

    }

    /**
     * 通过testUUId 发布 获取发布的试题的数量
     * @param testUUId  试卷的uuid
     * @param singleNum   发布的单选题的数量
     * @param multipleNum   发布的多选题的数量
     * @param judgmentNum  发布的判断题的数量
     * @param totalScore  发布的试题的总分值
     * @return  R
     */
    @ApiOperation(value = "通过testUUId 发布试卷")
    @GetMapping("/publishTestPaper")
    public R publishTestPaper(
            @NotNull @NotBlank(message = "试卷的uuid不能为空") @ApiParam ("试卷的UUId") @RequestParam("testUUId") String testUUId,
            @NotNull @ApiParam (value = "发布的单选题的数量",required = true) @Min(value = 0,message = "不能小于0") @Max(value = 50,message = "不能大于2")@RequestParam("singleNum") int singleNum,
            @NotNull @ApiParam (value = "发布的多选题的数量",required = true) @Min(value = 0,message = "不能小于0") @Max(value = 50,message = "不能大于2")@RequestParam("multipleNum") int multipleNum,
            @NotNull @ApiParam (value = "发布的判断题的数量",required = true) @Min(value = 0,message = "不能小于0") @Max(value = 50,message = "不能大于2")@RequestParam("judgmentNum") int judgmentNum,
            @NotNull @ApiParam (value = "发布的试题的总分值",required = true) @Min(value = 0,message = "不能小于0") @Max(value = 200,message = "不能大于2")@RequestParam("totalScore") int totalScore,
            @NotNull @ApiParam ("发布的试题可以做几次") @Min(value = 0,message = "不能小于0") @Max(value = 100,message = "不能大于100")@RequestParam(value = "pDoNum",required = false,defaultValue = "1") int pDoNum
    ){
        int result = testPaperService.publishTestPaper(testUUId, singleNum, multipleNum, judgmentNum, totalScore,pDoNum);
        if (result == StatusType.SUCCESS){
            return R.success();
        }else if (result == StatusType.CALCULATE_ERROR){
            return R.failure(CodeType.CALCULATE_ERROR, MsgType.CALCULATE_ERROR);
        }else if(result == StatusType.NOT_EXISTS){
            return R.notExists();
        }else {
            return R.failure();
        }
    }










}

