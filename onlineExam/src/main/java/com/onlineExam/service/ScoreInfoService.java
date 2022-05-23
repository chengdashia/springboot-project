package com.onlineExam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onlineExam.pojo.ScoreInfo;
import com.onlineExam.pojo.question.RequestAnswerBody;
import com.onlineExam.pojo.question.StuAnswer;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-21
 */
@Transactional
public interface ScoreInfoService extends IService<ScoreInfo> {

    //数据导出
    void export(String testUUId,HttpServletResponse response);

    //添加成绩
    int addScore(String studentId, RequestAnswerBody requestAnswerBody);

    //获得成绩列表 分页
    List<Map<String, Object>> getScoreListPage(String testUUId, Integer pageNum, Integer pageSize);


    //获得成绩列表  不分页
    List<Map<String, Object>> getScoreList(String testUUId);

    //新的添加成绩
    int newAddScore(String studentId, StuAnswer answerList);
}
