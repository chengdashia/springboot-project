package com.onlineExam.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.onlineExam.mapper.QuestionInfoMapper;
import com.onlineExam.mapper.ScoreInfoMapper;
import com.onlineExam.mapper.TestPaperMapper;
import com.onlineExam.pojo.OptionInfo;
import com.onlineExam.pojo.QuestionInfo;
import com.onlineExam.pojo.ScoreInfo;
import com.onlineExam.pojo.TestPaper;
import com.onlineExam.pojo.question.RequestQuestion;
import com.onlineExam.pojo.question.SqlQuestion;
import com.onlineExam.pojo.question.TestPaperBody;
import com.onlineExam.pojo.stu.StuTestPaper;
import com.onlineExam.service.TestPaperService;
import com.onlineExam.utils.NumberLetterUtil;
import com.onlineExam.utils.constants.Constants;
import com.onlineExam.utils.constants.QuestionCode;
import com.onlineExam.utils.random.RandomUtil;
import com.onlineExam.utils.result.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-02-19
 */
@Service
public class TestPaperServiceImpl extends ServiceImpl<TestPaperMapper, TestPaper> implements TestPaperService {


    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private QuestionInfoMapper questionInfoMapper;

    @Resource
    private ScoreInfoMapper scoreInfoMapper;

    //添加试卷
    @Override
    public Map<String, Object> addTest(String testName, String testDescription, String testFounder) {
        Map<String, Object> map = new HashMap<>();
        TestPaper testPaper = new TestPaper();
        String testUUId = IdUtil.simpleUUID();
        testPaper.setTestUuid(testUUId);
        testPaper.setTestName(testName);
        testPaper.setTestDescription(testDescription);
        testPaper.setTestFounder(testFounder);
        testPaper.setTestImgUrl(null);
        testPaper.setCreateTime(DateUtil.date());
        testPaper.setUpdateTime(DateUtil.date());
        int insert = testPaperMapper.insert(testPaper);
        if(insert == 1){
            map.put("status", StatusType.SUCCESS);
            map.put("data",testUUId);
            return map;
        }else {
            //有问题
            map.put("status", StatusType.ERROR);
        }
        return map;
    }

    //学生获得试卷体
    @Override
    public Map<String, Object> studentGetTestPaperBody(String studentId, String testUUId) throws RuntimeException{
        Map<String, Object> map = new HashMap<>();
        ScoreInfo scoreInfo;
        scoreInfo = scoreInfoMapper.selectOne(new QueryWrapper<ScoreInfo>()
                .eq("test_uuid", testUUId)
                .eq("student_id", studentId));

        //如果在成绩表中查不到这个人做过这套试卷。则让他做。
        if(scoreInfo == null){
            List<SqlQuestion> singleChoices = questionInfoMapper.selectJoinList(SqlQuestion.class,
                    new MPJLambdaWrapper<>()
                            .select(QuestionInfo::getSerialNum,QuestionInfo::getImgUrl,QuestionInfo::getQuestionType,QuestionInfo::getQuestionUuid, QuestionInfo::getCreateTime)
                            .selectAs(QuestionInfo::getQuestionTitle, SqlQuestion::getQuestionTitle)
                            .select(OptionInfo::getOptionContent, OptionInfo::getSerialLetter)
                            .leftJoin(OptionInfo.class, OptionInfo::getQuestionUuid, QuestionInfo::getQuestionUuid)
                            .eq(QuestionInfo::getTestUuid, testUUId));

            //根据序号排号
            Map<Integer, List<SqlQuestion>> collect = singleChoices.stream().collect(Collectors.groupingBy(SqlQuestion::getSerialNum));
            List<RequestQuestion> resultList = new LinkedList<>();

            for(Integer key : collect.keySet()){
                List<SqlQuestion> simpleQuestions = collect.get(key);
                Collections.sort(simpleQuestions, new Comparator<SqlQuestion>() {
                    @Override
                    public int compare(SqlQuestion o1, SqlQuestion o2) {
                        return o1.getSerialLetter().compareTo(o2.getSerialLetter());
                    }
                });
                List<String> optionList = simpleQuestions.stream().map(SqlQuestion::getOptionContent).collect(Collectors.toList());
                RequestQuestion question = new RequestQuestion();
                question.setOptions(optionList);
                question.setTestUuid(testUUId);
                question.setQuestionUuid(simpleQuestions.get(0).getQuestionUuid());
                question.setImgUrl(simpleQuestions.get(0).getImgUrl());
                question.setSerialNum(simpleQuestions.get(0).getSerialNum());
                question.setQuestionTitle(simpleQuestions.get(0).getQuestionTitle());
                question.setQuestionType(simpleQuestions.get(0).getQuestionType());
                question.setCreateTime(simpleQuestions.get(0).getCreateTime());
                resultList.add(question);
            }
//                String s = JSONUtil.toJsonStr(collect);
//                System.out.println(s);
            TestPaperBody testPaperBody = new TestPaperBody();
            testPaperBody.setQuestionList(resultList);
            //成功
            map.put("status", StatusType.SUCCESS);
            map.put("data",testPaperBody);
        }else {
            //已完成
            map.put("status", StatusType.IS_COMPLETE);
            map.put("data",scoreInfo.getScore());

        }
        return map;
    }

    //更改分值
    @Override
    public int updateScore(String testUUId, int questionType, int score) {
        int result;
        TestPaper test = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));
        if(test != null){
            //修改单选题
            if(QuestionCode.singleChoice == questionType){
                test.setSingleScore(score);

                result = testPaperMapper.updateById(test);
                if(result == 1){
                    //成功
                    updateTestPaper(test);
                    return StatusType.SUCCESS;
                }
                //修改单选题
            }else if(QuestionCode.multipleChoice == questionType){
                test.setMultipleScore(score);

                result = testPaperMapper.updateById(test);
                if(result == 1){
                    //成功
                    updateTestPaper(test);
                    return StatusType.SUCCESS;
                }
                //修改单选题
            }else if(QuestionCode.judgmentQuestion == questionType){
                test.setJudgmentScore(score);

                result = testPaperMapper.updateById(test);
                if(result == 1){
                    //成功
                    updateTestPaper(test);
                    return StatusType.SUCCESS;
                }
            }else {             //题型问题
                return StatusType.QUESTION_TYPE_ERROR;
            }
        }
        //失败
        return StatusType.ERROR;
    }

    //管理员获得试卷体
    @Override
    public Map<String, Object> adminGetTestPaperBody(String testUUId) {
        Map<String, Object> map = new HashMap<>();
            List<SqlQuestion> singleChoices = questionInfoMapper.selectJoinList(SqlQuestion.class,
                    new MPJLambdaWrapper<>()
                            .select(QuestionInfo::getSerialNum,QuestionInfo::getImgUrl,QuestionInfo::getQuestionType,QuestionInfo::getQuestionUuid,QuestionInfo::getRightKey,QuestionInfo::getAnswerAnalysis)
                            .selectAs(QuestionInfo::getQuestionTitle, SqlQuestion::getQuestionTitle)
                            .select(OptionInfo::getOptionContent, OptionInfo::getSerialLetter)
                            .leftJoin(OptionInfo.class, OptionInfo::getQuestionUuid, QuestionInfo::getQuestionUuid)
                            .eq(QuestionInfo::getTestUuid, testUUId));

            Map<Integer, List<SqlQuestion>> collect = singleChoices.stream().collect(Collectors.groupingBy(SqlQuestion::getSerialNum));
            List<RequestQuestion> resultList = new LinkedList<>();

            for(Integer key : collect.keySet()){
                List<SqlQuestion> simpleQuestions = collect.get(key);
                Collections.sort(simpleQuestions, new Comparator<SqlQuestion>() {
                    @Override
                    public int compare(SqlQuestion o1, SqlQuestion o2) {
                        return o1.getSerialLetter().compareTo(o2.getSerialLetter());
                    }
                });
                List<String> optionList = simpleQuestions.stream().map(SqlQuestion::getOptionContent).collect(Collectors.toList());
                RequestQuestion question = new RequestQuestion();
                question.setOptions(optionList);
                question.setTestUuid(testUUId);
                question.setQuestionUuid(simpleQuestions.get(0).getQuestionUuid());
                question.setImgUrl(simpleQuestions.get(0).getImgUrl());
                question.setSerialNum(simpleQuestions.get(0).getSerialNum());
                question.setQuestionTitle(simpleQuestions.get(0).getQuestionTitle());
                question.setQuestionType(simpleQuestions.get(0).getQuestionType());
                List<Integer> rightKeyList = NumberLetterUtil.getRightKeyList(simpleQuestions.get(0).getRightKey());
                question.setRightKey(rightKeyList);
                question.setAnswerAnalysis(simpleQuestions.get(0).getAnswerAnalysis());
                resultList.add(question);
            }
            TestPaperBody testPaperBody = new TestPaperBody();
            testPaperBody.setQuestionList(resultList);
            //成功
            map.put("status", StatusType.SUCCESS);
            map.put("data",testPaperBody);
            return map;
    }

    //逻辑删除
    @Override
    public int delTestPaper(String testUUId) {
        TestPaper one = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));
        if(one != null){
            one.setIsDel(QuestionCode.IS_DEL_IS);
            int result = testPaperMapper.deleteById(one);
            if(result == 1){
                return StatusType.SUCCESS;
            }
        }
        return StatusType.ERROR;
    }

    //学生获得试卷列表  新的
    @Override
    public Map<String, Object> stuGetTest(String studentId) {
        Map<String, Object> map = new HashMap<>();
        //从试卷表中查试卷
        List<TestPaper> testPaperList = testPaperMapper.selectList(new QueryWrapper<TestPaper>()
                .select("test_uuid", "test_name", "test_description", "test_founder", "p_total_score","create_time")
                .eq("status", Constants.PUSH)
                .orderByAsc("create_time"));

        if(testPaperList != null){
//                    List<Map<String, Object>> maps = testPaperMapper.selectJoinMaps(new MPJLambdaWrapper<>()
//                            .select(TestPaper::getTestUuid, TestPaper::getTestName, TestPaper::getTestDescription, TestPaper::getTestFounder, TestPaper::getTotalScore, TestPaper::getTestImgUrl)
//                            .select(ScoreInfo::getScore)
//                            .leftJoin(ScoreInfo.class, ScoreInfo::getTestUuid, TestPaper::getTestUuid)
//                            .eq(ScoreInfo::getStudentId, studentId));


            List<StuTestPaper> stuTestPaperList = testPaperMapper.selectJoinList(StuTestPaper.class, new MPJLambdaWrapper<>()
                    .select(TestPaper::getTestUuid, TestPaper::getTestName, TestPaper::getTestDescription, TestPaper::getTestFounder, TestPaper::getTotalScore, TestPaper::getTestImgUrl,TestPaper::getCreateTime)
                    .select(ScoreInfo::getScore)
                    .leftJoin(ScoreInfo.class, ScoreInfo::getTestUuid, TestPaper::getTestUuid)
                    .eq(ScoreInfo::getStudentId, studentId));
//                    map.put("data",maps);

            //试卷表中总共有这么多
            List<String> collect = testPaperList.stream().map(TestPaper::getTestUuid).collect(Collectors.toList());
//                    System.out.println("collect : "+collect.size());

            //连表查出来这么多条。
            List<String> collect1 = stuTestPaperList.stream().map(StuTestPaper::getTestUuid).collect(Collectors.toList());
//                    System.out.println("collect1 : "+collect1.size());

            //差集
            List<String> collect2 = collect.stream().filter(item -> !collect1.contains(item)).collect(Collectors.toList());
            for(String s : collect2){
                Optional<TestPaper> first = testPaperList.stream().filter(item -> item.getTestUuid().equals(s)).findFirst();
                if(first.isPresent()){
                    TestPaper testPaper = first.get();
                    StuTestPaper stuTestPaper = new StuTestPaper();
                    stuTestPaper.setTestUuid(testPaper.getTestUuid());
                    stuTestPaper.setTestName(testPaper.getTestName());
                    stuTestPaper.setTestDescription(testPaper.getTestDescription());
                    stuTestPaper.setTestFounder(testPaper.getTestFounder());
                    stuTestPaper.setTestImgUrl(testPaper.getTestImgUrl());
                    stuTestPaper.setPTotalScore(testPaper.getPTotalScore());
                    stuTestPaper.setScore(-1);
                    stuTestPaper.setCreateTime(testPaper.getCreateTime());
                    stuTestPaperList.add(stuTestPaper);
                }
            }
            map.put("status",StatusType.SUCCESS);
            map.put("data", stuTestPaperList);
        }
        return map;
    }

    //修改试卷状态
    @Override
    public int updateTestPaperStatus(String testUUId) {
        TestPaper one = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));
        if(one != null){
            one.setStatus(QuestionCode.TEST_PAPER_PUBLISHED);
            int result = testPaperMapper.updateById(one);
            if(result == 1){
                return StatusType.SUCCESS;
            }
        }
        return StatusType.ERROR;
    }


    //学生查看自己的试卷 新的
    @Override
    public Map<String, Object> studentNewGetTestPaperBody(String studentId, String testUUId) {
        Map<String, Object> map = new HashMap<>();

        //查询次数
        TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));
        ScoreInfo scoreInfo = scoreInfoMapper.selectOne(new QueryWrapper<ScoreInfo>()
                .eq("test_uuid", testUUId)
                .eq("student_id", studentId));
        if(testPaper != null){

            //获取题目数量
            Integer pSingleNum = testPaper.getPSingleNum();
            Integer pMultiNum = testPaper.getPMultipleNum();
            Integer pJudgeNum = testPaper.getPJudgmentNum();

            //连表查询获取问题 体
            List<SqlQuestion> questionList = questionInfoMapper.selectJoinList(SqlQuestion.class,
                    new MPJLambdaWrapper<>()
                            .select(QuestionInfo::getImgUrl,QuestionInfo::getQuestionType,QuestionInfo::getQuestionUuid, QuestionInfo::getCreateTime)
                            .selectAs(QuestionInfo::getQuestionTitle, SqlQuestion::getQuestionTitle)
                            .select(OptionInfo::getOptionContent, OptionInfo::getSerialLetter)
                            .leftJoin(OptionInfo.class, OptionInfo::getQuestionUuid, QuestionInfo::getQuestionUuid)
                            .eq(QuestionInfo::getTestUuid, testUUId));
            //根据问题的uuid分类
            Map<String, List<SqlQuestion>> collect = questionList.stream().collect(Collectors.groupingBy(SqlQuestion::getQuestionUuid));

            //
            List<RequestQuestion> tempList = new LinkedList<>();
            for (String entry : collect.keySet()) {

                //根据问题的uuid获取问题的列表
                List<SqlQuestion> sqlQuestionList = collect.get(entry);

                //将选项排序
                Collections.sort(sqlQuestionList, new Comparator<SqlQuestion>() {
                    @Override
                    public int compare(SqlQuestion o1, SqlQuestion o2) {
                        return o1.getSerialLetter().compareTo(o2.getSerialLetter());
                    }
                });

                //将问题转换成列表
                List<String> optionList = sqlQuestionList.stream().map(SqlQuestion::getOptionContent).collect(Collectors.toList());
                RequestQuestion question = new RequestQuestion();
                question.setQuestionUuid(entry);
                question.setQuestionType(sqlQuestionList.get(0).getQuestionType());
                question.setQuestionTitle(sqlQuestionList.get(0).getQuestionTitle());
                question.setOptions(optionList);
                question.setRightKey(null);
                question.setCreateTime(sqlQuestionList.get(0).getCreateTime());
                question.setImgUrl(sqlQuestionList.get(0).getImgUrl());
                tempList.add(question);
            }

            //根据题目的种类分类
            Map<Integer, List<RequestQuestion>> collect1 = tempList.stream().collect(Collectors.groupingBy(RequestQuestion::getQuestionType));

            //最终的问题列表
            List<RequestQuestion> resultsList = new LinkedList<>();

            //遍历题型
            for (Integer key : collect1.keySet()) {
                List<RequestQuestion> list = collect1.get(key);

                if(key == QuestionCode.singleChoice){
                    int[] ints = RandomUtil.randomArray(0, list.size()-1, pSingleNum);
                    if(ints != null){
                        for (int anInt : ints) {
                            resultsList.add(list.get(anInt));
                        }
                    }
                }
                if (key == QuestionCode.multipleChoice) {
                    int[] ints = RandomUtil.randomArray(0, list.size() - 1, pMultiNum);
                    if(ints != null){
                        for (int anInt : ints) {
                            resultsList.add(list.get(anInt));
                        }
                    }
                }
                if (key == QuestionCode.judgmentQuestion) {
                    int[] ints = RandomUtil.randomArray(0, list.size() - 1, pJudgeNum);
                    if(ints != null){
                        for (int anInt : ints) {
                            resultsList.add(list.get(anInt));
                        }
                    }
                }
            }
            map.put("status",StatusType.SUCCESS);
            map.put("data", resultsList);

        }
        return map;
    }

    //发布试卷
    @Override
    public int publishTestPaper(String testUUId, int singleNum, int multipleNum, int judgmentNum, int totalScore, int pDoNum) {
        TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));
        if(testPaper != null){
            Integer singleScore = testPaper.getSingleScore();
            Integer multipleScore = testPaper.getMultipleScore();
            Integer judgmentScore = testPaper.getJudgmentScore();
            if(singleScore * singleNum + multipleScore * multipleNum + judgmentScore * judgmentNum == totalScore){
                testPaper.setStatus(QuestionCode.TEST_PAPER_PUBLISHED); //发布
                testPaper.setPSingleNum(singleNum);
                testPaper.setPMultipleNum(multipleNum);
                testPaper.setPJudgmentNum(judgmentNum);
                testPaper.setPTotalScore(totalScore);
                testPaper.setPTotalNum(singleNum + multipleNum + judgmentNum);
                testPaper.setPDoNum(pDoNum);
                int i = testPaperMapper.updateById(testPaper);
                if(i > 0){
                    //发布成功
                    return StatusType.SUCCESS;
                }else {
                    //更新失败
                    return StatusType.ERROR;
                }
            }else {
                //总分不等于单选题分数+多选题分数+判断题分数
                return StatusType.CALCULATE_ERROR;
            }
        }else  {
            //试卷不存在
            return StatusType.NOT_EXISTS;
        }
    }


    //更新试卷表的内容
    public void updateTestPaper(TestPaper testPaper){
        try {
            testPaper.setTotalScore(
                    testPaper.getSingleScore() * testPaper.getSingleNum()
            + testPaper.getMultipleNum() * testPaper.getMultipleScore()
            + testPaper.getJudgmentNum() * testPaper.getJudgmentScore());
            testPaperMapper.updateById(testPaper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
