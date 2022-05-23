package com.onlineExam.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlineExam.mapper.AnswerInfoMapper;
import com.onlineExam.mapper.OptionInfoMapper;
import com.onlineExam.mapper.QuestionInfoMapper;
import com.onlineExam.mapper.TestPaperMapper;
import com.onlineExam.pojo.AnswerInfo;
import com.onlineExam.pojo.OptionInfo;
import com.onlineExam.pojo.QuestionInfo;
import com.onlineExam.pojo.TestPaper;
import com.onlineExam.pojo.question.BatchAddBody;
import com.onlineExam.pojo.question.ExcelQuestionBody;
import com.onlineExam.pojo.question.RequestQuestion;
import com.onlineExam.service.QuestionInfoService;
import com.onlineExam.utils.NumberLetterUtil;
import com.onlineExam.utils.constants.QuestionCode;
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
public class QuestionInfoServiceImpl extends ServiceImpl<QuestionInfoMapper, QuestionInfo> implements QuestionInfoService {

    @Resource
    private QuestionInfoMapper questionInfoMapper;

    @Resource
    private OptionInfoMapper optionInfoMapper;

    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private AnswerInfoMapper answerInfoMapper;

    //添加问题
    @Override
    public Map<String, Object> addQuestion(RequestQuestion requestQuestion) {
        Map<String, Object> map = new HashMap<>();
        QuestionInfo questionInfo = new QuestionInfo();
        String questionUUId = IdUtil.simpleUUID();
        questionInfo.setQuestionUuid(questionUUId);
        questionInfo.setTestUuid(requestQuestion.getTestUuid());
        questionInfo.setSerialNum(requestQuestion.getSerialNum());
        questionInfo.setQuestionTitle(requestQuestion.getQuestionTitle());
        questionInfo.setRightKey(NumberLetterUtil.getRightKey(requestQuestion.getRightKey()));
        questionInfo.setQuestionType(requestQuestion.getQuestionType());
        if (!"".equals(requestQuestion.getImgUrl())){
            questionInfo.setImgUrl(requestQuestion.getImgUrl());
        }
        questionInfo.setQuestionType(requestQuestion.getQuestionType());
        questionInfo.setCreateTime(new Date());
        questionInfo.setUpdateTime(new Date());

        int result = 0;
        List<String> options = null;
        int insert = questionInfoMapper.insert(questionInfo);
        if(insert == 1){
            result += insert;
            options = requestQuestion.getOptions();

            for (int i = 0; i < options.size(); i++) {
                OptionInfo optionInfo = new OptionInfo();
                optionInfo.setOptionUuid(IdUtil.simpleUUID());
                optionInfo.setQuestionUuid(questionUUId);
                optionInfo.setOptionContent(options.get(i));
                optionInfo.setSerialLetter(NumberLetterUtil.numberToLetter(i+1,true));
                optionInfo.setCreateTime(new Date());
                optionInfo.setUpdateTime(new Date());
                    int insert1 = optionInfoMapper.insert(optionInfo);
                    if(insert1 == 1){
                        result++;
                    }
            }
            if(result == options.size() + 1){
                //成功了。
                map.put("status",StatusType.SUCCESS);
                map.put("data",questionUUId);
            }

        }else {
            map.put("status",StatusType.ERROR);
        }
        if(options != null && result == options.size() + 1){
            addUpdateTestPaper(requestQuestion.getTestUuid(),requestQuestion.getQuestionType());
        }else {
            questionInfoMapper.delete(new QueryWrapper<QuestionInfo>().eq("question_uuid",questionUUId));
        }
        return map;
    }

    //修改问题
    @Override
    public int updateQuestion(RequestQuestion requestQuestion) {
        String questionUuid = requestQuestion.getQuestionUuid();
        List<String> options = requestQuestion.getOptions();
        int result = 0;

        QuestionInfo questionInfo = questionInfoMapper.selectOne(new QueryWrapper<QuestionInfo>()
                    .eq("test_uuid", requestQuestion.getTestUuid())
                    .eq("question_uuid", questionUuid));

        if (questionInfo != null){
            questionInfo.setQuestionType(requestQuestion.getQuestionType());
            questionInfo.setQuestionTitle(requestQuestion.getQuestionTitle());
            questionInfo.setSerialNum(requestQuestion.getSerialNum());
            questionInfo.setUpdateTime(new Date());

            int insert1 = questionInfoMapper.updateById(questionInfo);
            if(insert1 == 1){          //如果题目的更新成功
                result += insert1;
                List<OptionInfo> optionInfoList = optionInfoMapper.selectList(new QueryWrapper<OptionInfo>()
                        .eq("question_uuid", questionUuid));
                //开始对选项进行更新
                for (int i = 0; i < optionInfoList.size(); i++) {
                    optionInfoList.get(i).setOptionContent(options.get(i));
                    optionInfoList.get(i).setSerialLetter(NumberLetterUtil.numberToLetter(i+1,true));
                    optionInfoList.get(i).setUpdateTime(new Date());

                        int insert2 = optionInfoMapper.updateById(optionInfoList.get(i));
                        if(insert2 == 1){   //如果一个选项更新成功
                            result += insert2;
                        }
                    }
                }
            }
            if(result == 1+options.size()){
                return StatusType.SUCCESS;
            }else{
                questionInfoMapper.delete(new QueryWrapper<QuestionInfo>().eq("question_uuid",questionUuid));
                //有问题
                return StatusType.ERROR;
            }

    }


    //通过excel读出的数据进行添加
    @Override
    public int batchAddQuestion(String testUUId,List<ExcelQuestionBody> read) {
        //对读出的数据进行整理
        List<BatchAddBody> questionList = new LinkedList<>();
        for (ExcelQuestionBody questionBody : read) {
            BatchAddBody batchAddBody = new BatchAddBody();
            batchAddBody.setTestUuid(testUUId);
            batchAddBody.setQuestionType(questionBody.getType());
            batchAddBody.setQuestionTitle(questionBody.getTitle());
            batchAddBody.setSerialNum(questionBody.getSerialNum());
            batchAddBody.setRightKey(questionBody.getRightKey());
            batchAddBody.setScore(Integer.parseInt(questionBody.getScore()));
            batchAddBody.setAnswerAnalysis(questionBody.getAnswerAnalysis());

            //方法一：利用stream()先过滤，再返回一个list
            //            batchAddBody.setOptions(Arrays.asList(questionBody.getOption1(),
            //                    questionBody.getOption2(),
            //                    questionBody.getOption3(),
            //                    questionBody.getOption4(),
            //                    questionBody.getOption5()).stream().filter(Objects::nonNull).collect(Collectors.toList()));


            //将空选项为null不放到列表
            //方法二：直接使用removeIf
            List<String> options = new LinkedList<>(Arrays.asList(questionBody.getOption1(),
                    questionBody.getOption2(),
                    questionBody.getOption3(),
                    questionBody.getOption4(),
                    questionBody.getOption5()));
            options.removeIf(Objects::isNull);
            batchAddBody.setOptions(options);
            questionList.add(batchAddBody);
        }

        //循环对题目进行插入
        int questionSize = 0;
        for (BatchAddBody batchAddBody : questionList) {
            QuestionInfo questionInfo = new QuestionInfo();
            String questionUUId = IdUtil.simpleUUID();
            questionInfo.setQuestionUuid(questionUUId);
            questionInfo.setTestUuid(testUUId);
            questionInfo.setSerialNum(batchAddBody.getSerialNum());
            questionInfo.setQuestionTitle(batchAddBody.getQuestionTitle());
            questionInfo.setRightKey(batchAddBody.getRightKey());
            questionInfo.setAnswerAnalysis(batchAddBody.getAnswerAnalysis());
            questionInfo.setImgUrl(null);
            questionInfo.setQuestionType(batchAddBody.getQuestionType());
            questionInfo.setCreateTime(new Date());
            questionInfo.setUpdateTime(new Date());

            //对题目插入
            int insert = questionInfoMapper.insert(questionInfo);
            if (insert == 1) {
                List<String> options = batchAddBody.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    OptionInfo optionInfo = new OptionInfo();
                    optionInfo.setOptionUuid(IdUtil.simpleUUID());
                    optionInfo.setQuestionUuid(questionUUId);
                    optionInfo.setOptionContent(options.get(i));
                    optionInfo.setSerialLetter(NumberLetterUtil.numberToLetter(i + 1, true));
                    optionInfo.setCreateTime(new Date());
                    optionInfo.setUpdateTime(new Date());
                    optionInfoMapper.insert(optionInfo);
                }
                questionSize++;
            } else {
                return StatusType.ERROR;
            }
        }
//        System.out.println("questionSize ;" +questionSize);
        if(questionSize == read.size()){
//            题目插入成功
//            题目更新完成后，对试卷的信息进行更新
            Map<Integer, List<BatchAddBody>> collect = questionList.stream().collect(Collectors.groupingBy(BatchAddBody::getQuestionType));
            int singleNum = 0,multipleNum = 0,judgementNum = 0;
            int singleScore = 0,multipleScore = 0,judgementScore = 0;
            for(Integer type : collect.keySet()){
                if(type == QuestionCode.singleChoice){
                    singleNum = collect.get(type).size();
                    singleScore = collect.get(type).get(0).getScore();
                }
                if(type == QuestionCode.multipleChoice){
                    multipleNum = collect.get(type).size();
                    multipleScore = collect.get(type).get(0).getScore();
                }
                if(type == QuestionCode.judgmentQuestion){
                    judgementNum = collect.get(type).size();
                    judgementScore = collect.get(type).get(0).getScore();
                }
            }
            System.out.println("singleNum: "+singleNum);
            System.out.println("multipleNum: "+multipleNum);
            System.out.println("judgementNum: "+judgementNum);
            testPaperMapper.update(null,new LambdaUpdateWrapper<TestPaper>()
                        .set(TestPaper::getSingleNum,singleNum)                               //设置单选题的数量
                        .set(TestPaper::getSingleScore,singleScore)                           //设置单选题的分值
                        .set(TestPaper::getMultipleNum,multipleNum)                           //设置多选题的数量
                        .set(TestPaper::getMultipleScore,multipleScore)                       //设置多选题的分值
                        .set(TestPaper::getJudgmentNum,judgementNum)                          //设置判断题的数量
                        .set(TestPaper::getJudgmentScore,judgementScore)                      //设置判断题的分值
                        .set(TestPaper::getTotalNum,questionList.size())                      //设置试卷的总的数量
                        .set(TestPaper::getTotalScore,questionList.stream().mapToInt(BatchAddBody::getScore).sum())     //设置试卷的总的分值
                        .eq(TestPaper::getTestUuid,testUUId));
                //对答案进行更新
            int i = answerSave(testUUId);
            if(i == StatusType.SUCCESS){
                return StatusType.SUCCESS;
            }
            else if(i == StatusType.SQL_ERROR){
                return StatusType.SQL_ERROR;
            }else {
                return StatusType.ERROR;
            }
         }
        return StatusType.SUCCESS;
    }

    //删除并更新序号
    @Override
    public int delAndUpdateSerialNum(String testUUId, int serialNum, int questionType) {
        int delete = questionInfoMapper.delete(new QueryWrapper<QuestionInfo>()
            .eq("test_uuid", testUUId)
            .eq("serial_num", serialNum));

        if(delete == 1){
            List<QuestionInfo> questionInfos= questionInfoMapper.selectList(new QueryWrapper<QuestionInfo>().gt("serial_num", serialNum));
            updateBatchById(questionInfos);{
                for (QuestionInfo questionInfo :questionInfos){
                    questionInfo.setSerialNum(questionInfo.getSerialNum()-1);
                    questionInfoMapper.updateById(questionInfo);
                }
            }
            delUpdateTestPaper(testUUId,questionType);
            return StatusType.SUCCESS;
        }else {
            return StatusType.ERROR;
        }
    }


    //删除试题的时候：更新试卷表的内容
    public void delUpdateTestPaper(String testUUId, int questionType){
        try {
            TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>()
                    .eq("test_uuid", testUUId));

            if(questionType == QuestionCode.singleChoice){
                testPaper.setSingleNum(testPaper.getSingleNum() - 1);
                testPaper.setTotalNum(testPaper.getTotalNum()-1);
                testPaper.setTotalScore(testPaper.getTotalScore() - testPaper.getSingleScore());

            }
            if(questionType == QuestionCode.multipleChoice){
                testPaper.setMultipleNum(testPaper.getMultipleNum() - 1);
                testPaper.setTotalNum(testPaper.getTotalNum() - 1);
                testPaper.setTotalScore(testPaper.getTotalScore() - testPaper.getMultipleScore());

            }
            if(questionType == QuestionCode.judgmentQuestion){
                testPaper.setJudgmentNum(testPaper.getJudgmentNum() - 1);
                testPaper.setTotalNum(testPaper.getTotalNum() - 1);
                testPaper.setTotalScore(testPaper.getTotalScore() - testPaper.getJudgmentScore());

            }
            testPaperMapper.updateById(testPaper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //增加试题的时候：更新试卷表的内容
    public void addUpdateTestPaper(String testUUId, int questionType){
        try {
            TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>()
                    .eq("test_uuid", testUUId));
            if(questionType == QuestionCode.singleChoice){
                testPaper.setSingleNum(testPaper.getSingleNum() + 1);
                testPaper.setTotalNum(testPaper.getTotalNum()+1);
                testPaper.setTotalScore(testPaper.getTotalScore() + testPaper.getSingleScore());

            }else if(questionType == QuestionCode.multipleChoice){
                testPaper.setMultipleNum(testPaper.getMultipleNum() + 1);
                testPaper.setTotalNum(testPaper.getTotalNum()+1);
                testPaper.setTotalScore(testPaper.getTotalScore() + testPaper.getMultipleScore());

            }else if(questionType == QuestionCode.judgmentQuestion){
                testPaper.setJudgmentNum(testPaper.getJudgmentNum() + 1);
                testPaper.setTotalNum(testPaper.getTotalNum() + 1);
                testPaper.setTotalScore(testPaper.getTotalScore() + testPaper.getJudgmentScore());

            }
            testPaperMapper.updateById(testPaper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //答案保存
    public int answerSave(String testUUId){
        //从问题表中查出正确答案和题型  按照序号排序
        List<QuestionInfo> questionInfoList = questionInfoMapper.selectList(new QueryWrapper<QuestionInfo>()
                .select("right_key","question_type")
                .eq("test_uuid", testUUId)
                .orderByAsc("serial_num"));

        //按照题目类型分类
        Map<Integer, List<QuestionInfo>> collect = questionInfoList.stream().collect(Collectors.groupingBy(QuestionInfo::getQuestionType));
        List<String> resultList = new LinkedList<>();


        //遍历不同类型的题目。
        for(Integer key : collect.keySet()){
            List<QuestionInfo> questionInfos = collect.get(key);

            //将对象的正确答案的属性抽取出来。
            List<String> questionRightKeyList = questionInfos.stream().map(QuestionInfo::getRightKey).collect(Collectors.toList());
            //将列表中间加 —— 变成字符串
            String result = String.join("-", questionRightKeyList);
            //添加到答案列表
            resultList.add(result);
        }
        int result;

            AnswerInfo answerInfo = answerInfoMapper.selectOne(new QueryWrapper<AnswerInfo>()
                    .eq("test_uuid", testUUId));
            //如果答案表中没有这张试卷的答案。就做插入。
            if(answerInfo == null){
                answerInfo = new AnswerInfo();
                answerInfo.setAnswerUuid(IdUtil.simpleUUID());
                answerInfo.setTestUuid(testUUId);


                if(resultList.size() == 1) {
                    answerInfo.setSingleAnswer(resultList.get(0));
                    answerInfo.setMultipleAnswer(null);
                    answerInfo.setJudgmentAnswer(null);
                }
                if(resultList.size() == 2){
                    answerInfo.setSingleAnswer(resultList.get(0));
                    answerInfo.setMultipleAnswer(resultList.get(1));
                    answerInfo.setJudgmentAnswer(null);
                }
                if(resultList.size() == 3){
                    answerInfo.setSingleAnswer(resultList.get(0));
                    answerInfo.setMultipleAnswer(resultList.get(1));
                    answerInfo.setJudgmentAnswer(resultList.get(2));
                }

                answerInfo.setCreateTime(DateUtil.date());
                answerInfo.setUpdateTime(DateUtil.date());
                result = answerInfoMapper.insert(answerInfo);
            }else {
                //如果答案表中有这张试卷的答案。就做更新。

                if(resultList.size() == 1) {
                    answerInfo.setSingleAnswer(resultList.get(0));
                    answerInfo.setMultipleAnswer(null);
                    answerInfo.setJudgmentAnswer(null);
                }
                if(resultList.size() == 2){
                    answerInfo.setSingleAnswer(resultList.get(0));
                    answerInfo.setMultipleAnswer(resultList.get(1));
                    answerInfo.setJudgmentAnswer(null);
                }
                if(resultList.size() == 3){
                    answerInfo.setSingleAnswer(resultList.get(0));
                    answerInfo.setMultipleAnswer(resultList.get(1));
                    answerInfo.setJudgmentAnswer(resultList.get(2));
                }


                answerInfo.setUpdateTime(new Date());
                result = answerInfoMapper.updateById(answerInfo);
            }
            return  result;
    }




}
