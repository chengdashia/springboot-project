package com.onlineExam.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.onlineExam.mapper.AnswerInfoMapper;
import com.onlineExam.mapper.QuestionInfoMapper;
import com.onlineExam.mapper.ScoreInfoMapper;
import com.onlineExam.mapper.TestPaperMapper;
import com.onlineExam.pojo.*;
import com.onlineExam.pojo.question.AnswerSubject;
import com.onlineExam.pojo.question.RequestAnswerBody;
import com.onlineExam.pojo.question.StuAnswer;
import com.onlineExam.service.ScoreInfoService;
import com.onlineExam.utils.constants.QuestionCode;
import com.onlineExam.utils.date.ConvertorTime;
import com.onlineExam.utils.redis.RedisUtil;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.SecureDESUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-21
 */
@Service
public class ScoreInfoServiceImpl extends ServiceImpl<ScoreInfoMapper, ScoreInfo> implements ScoreInfoService {

    @Resource
    private ScoreInfoMapper scoreInfoMapper;

    @Resource
    private AnswerInfoMapper answerInfoMapper;

    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private QuestionInfoMapper questionInfoMapper;


    @Resource
    private RedisUtil redisUtil;

    //导出数据
    @Override
    public void export(String testUUId, HttpServletResponse response) {

//        List<Map<String, Object>> maps = scoreInfoMapper.selectJoinMaps(new MPJLambdaWrapper<>()
//                .select(ScoreInfo::getScore,
//                        ScoreInfo::getCreateTime,
//                        ScoreInfo::getStudentId)
//                .select(InstituteInfo::getInstituteName)
//                .select(ClassInfo::getClassName)
//                .select(StudentInfo::getStudentName)
//                .leftJoin(StudentInfo.class, StudentInfo::getStudentId, ScoreInfo::getStudentId)
//                .leftJoin(InstituteInfo.class, InstituteInfo::getInstituteUuid, StudentInfo::getStudentInstitute)
//                .leftJoin(ClassInfo.class, ClassInfo::getClassUuid, StudentInfo::getStudentClass)
//                .eq(ScoreInfo::getTestUuid, testUUId));
//
////        System.out.println(maps);
//        for(Map<String,Object> map : maps){
//            for(String s : map.keySet()){
//                if("instituteName".equals(s) || "className".equals(s) || "studentName".equals(s) || "studentId".equals(s)){
//                    map.put(s, SecureDESUtil.decrypt((String) map.get(s)));
//                }
////                System.out.println(s);
//            }
////            System.out.println(map);
//        }

        List<Map<String, Object>> maps = getScoreListFun(testUUId);

        // 准备将数据集合封装成Excel对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 通过工具类创建writer并且进行别名
        writer.addHeaderAlias("studentId", "学号");
        writer.addHeaderAlias("studentName", "姓名");
        writer.addHeaderAlias("score", "分数");
        writer.addHeaderAlias("createTime", "提交时间");
        writer.addHeaderAlias("className", "班级");
        writer.addHeaderAlias("instituteName", "学院");
        writer.addHeaderAlias("completionTime", "用时");

        // 准备将对象写入我们的 List
        writer.write(maps, true);
        try {
            // 获取我们的输出流
            final OutputStream output = response.getOutputStream();
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("stu.xlsx", "UTF-8"));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            writer.flush(output, true);
            writer.close();
            // 这里可以自行关闭资源或者写一个关闭资源的工具类
            IoUtil.close(output);
        } catch (IOException e) {
            log.error("EducationServiceImpl [export] 输出到响应流失败", e);
            e.printStackTrace();
        }
    }

    //添加成绩
    @Override
    public int addScore(String studentId, RequestAnswerBody requestAnswerBody) throws RuntimeException{
        try{
            AnswerInfo answerInfo = answerInfoMapper.selectOne(new QueryWrapper<AnswerInfo>()
                    .eq("test_uuid", requestAnswerBody.getTestUuid()));
            if(answerInfo != null){
                TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>()
                        .select("single_score", "multiple_score", "judgment_score")
                        .eq("test_uuid", requestAnswerBody.getTestUuid()));

                Integer singleScore = testPaper.getSingleScore();
                Integer multipleScore = testPaper.getMultipleScore();
                Integer judgmentScore = testPaper.getJudgmentScore();

                List<String> requestSingleList = requestAnswerBody.getSingleAnswers();
                List<String> requestMultipleList = requestAnswerBody.getMultipleAnswers();
                List<String> requestJudgmentList = requestAnswerBody.getJudgmentAnswers();


                int singleNum = 0,multipleNum = 0,judgmentNum = 0,total;
                String[] singleSplit = answerInfo.getSingleAnswer().split("-");
                String[] multipleSplit = null;
                String[] judgmentSplit = null;
                if(answerInfo.getMultipleAnswer() != null){
                    multipleSplit = answerInfo.getMultipleAnswer().split("-");

                    for (int i = 0; i < requestMultipleList.size(); i++) {
                        if(requestMultipleList.get(i).equals(multipleSplit[i])){
                            multipleNum++;
                        }
                    }
                }
                if (answerInfo.getJudgmentAnswer() != null){
                    judgmentSplit = answerInfo.getJudgmentAnswer().split("-");

                    for (int i = 0; i < requestJudgmentList.size(); i++) {
                        if(requestJudgmentList.get(i).equals(judgmentSplit[i])){
                            judgmentNum++;
                        }
                    }
                }


                for (int i = 0; i < requestSingleList.size(); i++) {
                    if(requestSingleList.get(i).equals(singleSplit[i])){
                        singleNum++;
                    }
                }


                total = singleNum * singleScore + multipleNum * multipleScore + judgmentNum * judgmentScore;
                ScoreInfo scoreInfo = new ScoreInfo();
                scoreInfo.setScoreUuid(IdUtil.simpleUUID());
                scoreInfo.setStudentId(studentId);
                scoreInfo.setTestUuid(requestAnswerBody.getTestUuid());
                scoreInfo.setScore(total);
                scoreInfo.setCreateTime(new Date());
                scoreInfo.setCompletionTime(requestAnswerBody.getCompletionTime());
                int insert = scoreInfoMapper.insert(scoreInfo);
                if(insert == 1){
                    return StatusType.SUCCESS;
                }else {
                    return StatusType.ERROR;
                }
            }
            else {
                return StatusType.NOT_EXISTS;
            }
        }catch (Exception e){
            e.printStackTrace();
            return StatusType.SQL_ERROR;
        }

    }

    //获取成绩列表  分页
    @Override
    public List<Map<String, Object>> getScoreListPage(String testUUId, Integer pageNum, Integer pageSize) {
        List<Map<String, Object>> scoreListPageFun = getScoreListPageFun(testUUId, pageNum, pageSize);
        System.out.println(scoreListPageFun);
        for(Map<String,Object> map : scoreListPageFun){
            System.out.println(map);
        }
        return scoreListPageFun;

//        return getScoreListPageFun(testUUId, pageNum, pageSize);
    }


    //获取成绩列表  不分页
    @Override
    public List<Map<String, Object>> getScoreList(String testUUId) {

        List<Map<String, Object>> scoreListFun = getScoreListFun(testUUId);
        System.out.println(scoreListFun);

        for(Map<String,Object> map : scoreListFun){
            System.out.println(map);
        }
        return scoreListFun;
    }

    //新的添加成绩
    @Override
    public int newAddScore(String studentId, StuAnswer answerList) {
        String testUuid = answerList.getTestUuid();
        //从数据库中获取该试卷的各种题型的分值
        TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>()
                .select("single_score", "multiple_score", "judgment_score", "p_do_num")
                .eq("test_uuid", testUuid));


        if (testPaper != null) {

            Integer singleScore = testPaper.getSingleScore();
            Integer multipleScore = testPaper.getMultipleScore();
            Integer judgmentScore = testPaper.getJudgmentScore();


            ScoreInfo scoreInfoOrigin = scoreInfoMapper.selectOne(new QueryWrapper<ScoreInfo>()
                    .eq("student_id", studentId)
                    .eq("test_uuid", testUuid));


            if (scoreInfoOrigin == null) {
                //没有成绩，直接添加
                //计算分数
                int score = 0;
                //如果redis 有
                List<AnswerSubject> answerSubjectList = answerList.getAnswerSubjectList();
                if (redisUtil.hasKey(testUuid)) {

                    Map<String, String> answerMap = (Map<String, String>) redisUtil.get(testUuid);

                    for (AnswerSubject answerSubject : answerSubjectList) {
                        if (answerSubject.getRightKey().equals(answerMap.get(answerSubject.getQuestionUuid()))) {
                            if (answerSubject.getQuestionType() == QuestionCode.singleChoice) {
                                score += singleScore;
                            }
                            if (answerSubject.getQuestionType() == QuestionCode.multipleChoice) {
                                score += multipleScore;
                            }
                            if (answerSubject.getQuestionType() == QuestionCode.judgmentQuestion) {
                                score += judgmentScore;
                            }
                        }
                    }
                    //如果redis 没有
                } else {
                    //从数据库中获取该试卷的uuid和答案
                    List<QuestionInfo> questionInfoList = questionInfoMapper.selectList(new QueryWrapper<QuestionInfo>()
                            .select("question_uuid", "right_key")
                            .eq("test_uuid", testUuid));

                    //将试题信息封装到一个map中
                    Map<String, String> answerMap = new HashMap<>();
                    for (QuestionInfo questionInfo : questionInfoList) {
                        answerMap.put(questionInfo.getQuestionUuid(), questionInfo.getRightKey());
                    }
                    redisUtil.set(testUuid, answerMap);

                    for (AnswerSubject answerSubject : answerSubjectList) {
                        if (answerSubject.getRightKey().equals(answerMap.get(answerSubject.getQuestionUuid()))) {
                            if (answerSubject.getQuestionType() == QuestionCode.singleChoice) {
                                score += singleScore;
                            }
                            if (answerSubject.getQuestionType() == QuestionCode.multipleChoice) {
                                score += multipleScore;
                            }
                            if (answerSubject.getQuestionType() == QuestionCode.judgmentQuestion) {
                                score += judgmentScore;
                            }
                        }
                    }
                }
                //将成绩信息插入到数据库中
                ScoreInfo scoreInfo = new ScoreInfo();
                scoreInfo.setScoreUuid(IdUtil.simpleUUID());
                scoreInfo.setStudentId(studentId);
                scoreInfo.setTestUuid(testUuid);
                scoreInfo.setScore(score);
                scoreInfo.setCreateTime(new Date());
                scoreInfo.setCompletionTime(answerList.getCompletionTime());
                int insert = scoreInfoMapper.insert(scoreInfo);
                if(insert == 1){
                    return StatusType.SUCCESS;
                }
                else {
                    return StatusType.ERROR;
                }

            } else {

                //学生做该试卷的次数
                Integer stuFrequency = scoreInfoOrigin.getFrequency();

                //该试卷的可以做的次数
                Integer pDoNum = testPaper.getPDoNum();

                if (stuFrequency < pDoNum) {
                    //计算分数
                    int score = 0;
                    //如果redis 有
                    List<AnswerSubject> answerSubjectList = answerList.getAnswerSubjectList();
                    if (redisUtil.hasKey(testUuid)) {

                        Map<String, String> answerMap = (Map<String, String>) redisUtil.get(testUuid);

                        for (AnswerSubject answerSubject : answerSubjectList) {
                            if (answerSubject.getRightKey().equals(answerMap.get(answerSubject.getQuestionUuid()))) {
                                if (answerSubject.getQuestionType() == QuestionCode.singleChoice) {
                                    score += singleScore;
                                }
                                if (answerSubject.getQuestionType() == QuestionCode.multipleChoice) {
                                    score += multipleScore;
                                }
                                if (answerSubject.getQuestionType() == QuestionCode.judgmentQuestion) {
                                    score += judgmentScore;
                                }
                            }
                        }
                        //如果redis 没有
                    } else {
                        //从数据库中获取该试卷的uuid和答案
                        List<QuestionInfo> questionInfoList = questionInfoMapper.selectList(new QueryWrapper<QuestionInfo>()
                                .select("question_uuid", "right_key")
                                .eq("test_uuid", testUuid));

                        //将试题信息封装到一个map中
                        Map<String, String> answerMap = new HashMap<>();
                        for (QuestionInfo questionInfo : questionInfoList) {
                            answerMap.put(questionInfo.getQuestionUuid(), questionInfo.getRightKey());
                        }
                        redisUtil.set(testUuid, answerMap);

                        for (AnswerSubject answerSubject : answerSubjectList) {
                            if (answerSubject.getRightKey().equals(answerMap.get(answerSubject.getQuestionUuid()))) {
                                if (answerSubject.getQuestionType() == QuestionCode.singleChoice) {
                                    score += singleScore;
                                }
                                if (answerSubject.getQuestionType() == QuestionCode.multipleChoice) {
                                    score += multipleScore;
                                }
                                if (answerSubject.getQuestionType() == QuestionCode.judgmentQuestion) {
                                    score += judgmentScore;
                                }
                            }
                        }
                    }

                    //更新试卷的分数
                    Integer originScore = scoreInfoOrigin.getScore();
                    if (originScore > score) {
                        return StatusType.SUCCESS;
                    }else {
                        //将学生的答题情况存入数据库
                        int update = scoreInfoMapper.update(null, new UpdateWrapper<ScoreInfo>()
                                .set("score", score)
                                .set("frequency", scoreInfoOrigin.getFrequency() + 1)
                                .eq("test_uuid", testUuid)
                                .eq("student_uuid", studentId));

                        if (update == 1) {
                            return StatusType.SUCCESS;
                        } else {
                            return StatusType.ERROR;
                        }
                    }

                } else {
                    //次数已经用完
                    return StatusType.NOT_DO;
                }
            }


        } else {
            return StatusType.NOT_EXISTS;
        }

    }

    //通过testUUid获取成绩情况 不分页
    public List<Map<String, Object>> getScoreListFun(String testUUId){
        List<Map<String, Object>> maps = scoreInfoMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(ScoreInfo::getScore,
                            ScoreInfo::getCreateTime,
                            ScoreInfo::getStudentId,
                            ScoreInfo::getCompletionTime)
                    .select(InstituteInfo::getInstituteName)
                    .select(ClassInfo::getClassName)
                    .select(StudentInfo::getStudentName)
                    .leftJoin(StudentInfo.class, StudentInfo::getStudentId, ScoreInfo::getStudentId)
                    .leftJoin(InstituteInfo.class, InstituteInfo::getInstituteUuid, StudentInfo::getStudentInstitute)
                    .leftJoin(ClassInfo.class, ClassInfo::getClassUuid, StudentInfo::getStudentClass)
                    .orderByDesc(ScoreInfo::getScore)
                    .orderByAsc(ScoreInfo::getCompletionTime)

                    .eq(ScoreInfo::getTestUuid, testUUId));

        return getDecryptData(maps);
    }


    //通过testUUid获取成绩情况  分页
    public List<Map<String, Object>> getScoreListPageFun(String testUUId,int PageNum,int pageSize){
        IPage<Map<String, Object>> mapIPage = scoreInfoMapper.selectJoinMapsPage(new Page<>(PageNum, pageSize), new MPJLambdaWrapper<>()
                .select(ScoreInfo::getScore,
                        ScoreInfo::getCreateTime,
                        ScoreInfo::getStudentId,
                        ScoreInfo::getCompletionTime)
                .select(InstituteInfo::getInstituteName)
                .select(ClassInfo::getClassName)
                .select(StudentInfo::getStudentName)
                .leftJoin(StudentInfo.class, StudentInfo::getStudentId, ScoreInfo::getStudentId)
                .leftJoin(InstituteInfo.class, InstituteInfo::getInstituteUuid, StudentInfo::getStudentInstitute)
                .leftJoin(ClassInfo.class, ClassInfo::getClassUuid, StudentInfo::getStudentClass)
                .eq(ScoreInfo::getTestUuid, testUUId));

        List<Map<String, Object>> maps = mapIPage.getRecords();
        return getDecryptData(maps);
    }

    //获取解密的数据
    public List<Map<String, Object>> getDecryptData(List<Map<String, Object>> maps){
        for(Map<String,Object> map : maps){
            for(String s : map.keySet()){
                if("instituteName".equals(s) || "className".equals(s) || "studentName".equals(s) || "studentId".equals(s)){
                    map.put(s, SecureDESUtil.decrypt((String) map.get(s)));
                }
                if("completionTime".equals(s)){
                    map.put(s,ConvertorTime.secToTime((Integer) map.get(s)));
                }
//                System.out.println(s);
            }
//            System.out.println(map);
        }
        return maps;
    }



}
