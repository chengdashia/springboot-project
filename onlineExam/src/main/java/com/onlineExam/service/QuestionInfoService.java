package com.onlineExam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlineExam.pojo.QuestionInfo;
import com.onlineExam.pojo.question.ExcelQuestionBody;
import com.onlineExam.pojo.question.RequestQuestion;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-19
 */
@Transactional
public interface QuestionInfoService extends IService<QuestionInfo> {

    //添加问题
    Map<String, Object> addQuestion(RequestQuestion singleChoiceQuestion);

    //修改问题
    int updateQuestion(RequestQuestion singleChoiceQuestion);

    //批量添加
    int batchAddQuestion(String testUUId,List<ExcelQuestionBody> read);

    //删除试题并更新序号
    int delAndUpdateSerialNum(String testUUId, int serialNum, int questionType);
}
