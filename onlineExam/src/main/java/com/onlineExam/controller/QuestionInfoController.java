package com.onlineExam.controller;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.CellEditor;
import com.onlineExam.pojo.question.ExcelQuestionBody;
import com.onlineExam.pojo.question.RequestQuestion;
import com.onlineExam.service.QuestionInfoService;
import com.onlineExam.utils.constants.QuestionCode;
import com.onlineExam.utils.file.FileUtil;
import com.onlineExam.utils.result.MsgType;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
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
@Api(tags = "问题")
@RestController
@RequestMapping("/questionInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionInfoController {


    private final QuestionInfoService questionInfoService;

    /**
     * 添加问题
     *
     * @param requestQuestion 接收json数据
     * @return GlobalResult
     */
    @ApiOperation("添加问题")
    @PostMapping("/addQuestion")
    public R addQuestion(@Valid @RequestBody RequestQuestion requestQuestion) {
        System.out.println(requestQuestion);
        Map<String, Object> map = questionInfoService.addQuestion(requestQuestion);
        int result = (int) map.get("status");
        if (result == StatusType.SUCCESS) {                   //成功
            return R.success(map.get("data"));
        } else {
            return R.failure();                       //有问题
        }
    }

    /**
     * 修改问题
     *
     * @param requestQuestion 接收json数据
     * @return GlobalResult
     */
    @ApiOperation("修改问题")
    @PostMapping("/updateQuestion")
    public R updateQuestion(@Validated @RequestBody RequestQuestion requestQuestion) {
        int result = questionInfoService.updateQuestion(requestQuestion);
        if (result == StatusType.SUCCESS) {     //成功
            return R.success();
        } else if (result == StatusType.NOT_EXISTS) {
            return R.failure(MsgType.NOT_EXISTS);
        } else {
            return R.failure();    //有问题
        }
    }

    /**
     * 通过模板上传来添加试题
     *
     * @param request  获取token
     * @param testUUId 试卷的uuid
//     * @param file     excel文件
     * @return R
     */
    @ApiOperation("通过模板上传来添加试题")
    @PostMapping("/uploadExcel")
    public R uploadExcel(HttpServletRequest request,
                         @ApiParam("试卷的uuid") @NotBlank(message = "试卷的uuid不能为空") @RequestParam("testUUId") String testUUId
//                         @ApiParam("excel文件") @RequestParam("file") MultipartFile file
    ) {

        String uuId = JWTUtils.getEncryptUUId(request);
//        Map<String, Object> map = FileUtil.saveFile(file, uuId);
//        int status = (int) map.get("status");

//        System.out.println("testUUId "+testUUId);
//        System.out.println("status "+status);
//        System.out.println("controller   filePath:"+ map.get("filePath"));


        if (1 == StatusType.SUCCESS) {
//            File excelFile = (File) map.get("filePath");
//            String path = excelFile.getAbsolutePath();
            String path = "E:\\anwer\\国家安全题库.xlsx";


            System.out.println("testUUId "+testUUId);
            System.out.println("filePath:"+path);

            ExcelReader reader = ExcelUtil.getReader(path);
            reader.addHeaderAlias("题型", "type");
            reader.addHeaderAlias("序号", "serialNum");
            reader.addHeaderAlias("题目", "title");
            reader.addHeaderAlias("选项1", "option1");
            reader.addHeaderAlias("选项2", "option2");
            reader.addHeaderAlias("选项3", "option3");
            reader.addHeaderAlias("选项4", "option4");
            reader.addHeaderAlias("选项5", "option5");
            reader.addHeaderAlias("正确答案", "rightKey");
            reader.addHeaderAlias("答案解析", "answerAnalysis");
            reader.addHeaderAlias("分值", "score");

            //对excel中的值进行转换
            reader.setCellEditor(new CellEditor() {
                @Override
                public Object edit(Cell cell, Object o) {
                    if (cell.getColumnIndex() == 0 && cell.getStringCellValue() != null) {
                        if (cell.getStringCellValue().equals("单选题")) o = QuestionCode.singleChoice;
                        if (cell.getStringCellValue().equals("多选题")) o = QuestionCode.multipleChoice;
                        if (cell.getStringCellValue().equals("判断题")) o = QuestionCode.judgmentQuestion;
                        return o;
                    }
                    return o;
                }
            });

            List<ExcelQuestionBody> read = reader.read(2, 2, ExcelQuestionBody.class);

//            System.out.println(read);

            int result = questionInfoService.batchAddQuestion(testUUId, read);
            if (result == StatusType.SUCCESS) {
                return R.success();
            }  else {
                return R.failure();
            }

        }else {
            return R.failure();
        }

    }


    @ApiOperation("如果删除题目。改变题目的序号")
    @PostMapping("/delAndUpdateSerialNum")
    public R delAndUpdateSerialNum(
            @ApiParam(value = "testUUId",required = true) @NotBlank(message = "不能为空") @NotNull @RequestParam("testUUId") String testUUId,
            @ApiParam(value = "questionType",required = true) @NotNull @RequestParam("questionType") int questionType,
            @ApiParam(value = "serialNum",required = true) @NotNull @RequestParam("serialNum") int serialNum){
        System.out.println("testUUId :"+testUUId);
        System.out.println("questionType :"+questionType);
        System.out.println("serialNum :"+serialNum);

        int i = questionInfoService.delAndUpdateSerialNum(testUUId, serialNum,questionType);
        if(i == StatusType.SUCCESS){
            return R.success();
        }else {
            return R.failure();
        }

    }


}

