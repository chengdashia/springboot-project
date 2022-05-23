package com.onlineExam.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onlineExam.mapper.AnswerInfoMapper;
import com.onlineExam.mapper.QuestionInfoMapper;
import com.onlineExam.mapper.TestPaperMapper;
import com.onlineExam.pojo.AnswerInfo;
import com.onlineExam.pojo.QuestionInfo;
import com.onlineExam.pojo.TestPaper;
import com.onlineExam.service.AnswerInfoService;
import com.onlineExam.utils.constants.QuestionCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
public class AnswerInfoServiceImpl extends ServiceImpl<AnswerInfoMapper, AnswerInfo> implements AnswerInfoService {

    @Resource
    private AnswerInfoMapper answerInfoMapper;

    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private QuestionInfoMapper questionInfoMapper;

    @Override
    public int testPaperSave(String testUUId) {

        TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>()
                .eq("test_uuid",testUUId));

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

                //将信息保存
                testPaperSave(resultList,answerInfo,collect,testPaper);

                answerInfo.setCreateTime(DateUtil.date());
                answerInfo.setUpdateTime(DateUtil.date());

                testPaperMapper.updateById(testPaper);
                result = answerInfoMapper.insert(answerInfo);
            }else {
                //将信息保存
                testPaperSave(resultList,answerInfo,collect,testPaper);

                answerInfo.setUpdateTime(new Date());
                testPaperMapper.updateById(testPaper);
                result = answerInfoMapper.updateById(answerInfo);
            }
            return  result;
    }


    public void testPaperSave(List<String> resultList,AnswerInfo answerInfo,Map<Integer, List<QuestionInfo>> collect,TestPaper testPaper){
        int sigNum;
        int mulNum = 0;
        int jugNum = 0;

        //如果只有单选题的答案
        if(resultList.size() == 1) {
            answerInfo.setSingleAnswer(resultList.get(0));
            answerInfo.setMultipleAnswer(null);
            answerInfo.setJudgmentAnswer(null);


            sigNum = collect.get(QuestionCode.singleChoice).size();

            testPaper.setSingleNum(sigNum);
            testPaper.setMultipleNum(mulNum);
            testPaper.setJudgmentNum(jugNum);

            testPaper.setTotalNum(sigNum+mulNum+jugNum);

            testPaper.setTotalScore(
                    sigNum * testPaper.getSingleScore()
            );
        }
        if(resultList.size() == 2){
            answerInfo.setSingleAnswer(resultList.get(0));
            answerInfo.setMultipleAnswer(resultList.get(1));
            answerInfo.setJudgmentAnswer(null);

            sigNum = collect.get(QuestionCode.singleChoice).size();
            mulNum = collect.get(QuestionCode.multipleChoice).size();

            testPaper.setSingleNum(sigNum);
            testPaper.setMultipleNum(mulNum);
            testPaper.setJudgmentNum(jugNum);

            testPaper.setTotalNum(sigNum+mulNum+jugNum);
            testPaper.setTotalScore(
                    sigNum * testPaper.getSingleScore()
                            + mulNum * testPaper.getMultipleScore()
            );
        }
        if(resultList.size() == 3){
            answerInfo.setSingleAnswer(resultList.get(0));
            answerInfo.setMultipleAnswer(resultList.get(1));
            answerInfo.setJudgmentAnswer(resultList.get(2));


            sigNum = collect.get(QuestionCode.singleChoice).size();
            mulNum = collect.get(QuestionCode.multipleChoice).size();
            jugNum = collect.get(QuestionCode.judgmentQuestion).size();

            testPaper.setSingleNum(sigNum);
            testPaper.setMultipleNum(mulNum);
            testPaper.setJudgmentNum(jugNum);

            testPaper.setTotalNum(sigNum+mulNum+jugNum);
            testPaper.setTotalScore(
                    sigNum * testPaper.getSingleScore()+
                            mulNum * testPaper.getMultipleScore() +
                            jugNum * testPaper.getJudgmentScore()
            );
        }
    }
}
