package com.onlineExam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlineExam.pojo.TestPaper;
import org.springframework.transaction.annotation.Transactional;

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
public interface TestPaperService extends IService<TestPaper> {

    //添加试卷
    Map<String, Object> addTest(String testName, String testDescription, String testFounder);

    //学生获取试卷体
    Map<String, Object> studentGetTestPaperBody(String studentId, String testUUId);

    //更改成绩
    int updateScore(String testUUId, int questionType, int score);

    //管理员获取试卷体
    Map<String, Object> adminGetTestPaperBody(String testUUId);

    //逻辑删除试卷
    int delTestPaper(String testUUId);

    //学生获取试卷列表
    Map<String, Object> stuGetTest(String studentId);

    //通过testUUId 更改试卷状态
    int updateTestPaperStatus(String testUUId);

    //新版的  学生获取试卷体
    Map<String, Object> studentNewGetTestPaperBody(String studentId, String testUUId);

    //发布试卷
    int publishTestPaper(String testUUId, int singleNum, int multipleNum, int judgmentNum, int totalScore, int pDoNum);
}
