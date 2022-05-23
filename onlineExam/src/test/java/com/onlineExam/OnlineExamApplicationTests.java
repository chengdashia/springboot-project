package com.onlineExam;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.onlineExam.mapper.*;
import com.onlineExam.pojo.*;
import com.onlineExam.pojo.question.RequestQuestion;
import com.onlineExam.pojo.question.SqlQuestion;
import com.onlineExam.utils.constants.QuestionCode;
import com.onlineExam.utils.random.RandomUtil;
import com.onlineExam.utils.safe.SecureDESUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class OnlineExamApplicationTests {

    @Resource
    private InstituteInfoMapper instituteInfoMapper;

    @Resource
    private StudentInfoMapper studentInfoMapper;

    @Resource
    private ClassInfoMapper classInfoMapper;

    @Resource
    private ScoreInfoMapper scoreInfoMapper;


    @Resource
    private QuestionInfoMapper questionInfoMapper;

    @Resource
    private TestPaperMapper testPaperMapper;


    @Test
    void insertInfo(){
//        //读取的时候设置格式
//        ExcelReader reader = ExcelUtil.getReader("E:\\anwer\\stuInfo.xlsx");
//        reader.addHeaderAlias("学院", "studentInstitute");
//        reader.addHeaderAlias("班级名称", "studentClass");
//        reader.addHeaderAlias("学号", "studentId");
//        reader.addHeaderAlias("学生姓名", "studentName");
//        reader.addHeaderAlias("性别", "studentSex");
//
//        //  设置单元格格式
//        reader.setCellEditor(new CellEditor() {
//            @Override
//            public Object edit(Cell cell, Object o) {
//                if (cell.getColumnIndex() == 4 ){
//                    if(cell.getStringCellValue().equals("男")) o = 1;
//                    if(cell.getStringCellValue().equals("女")) o = 0;
//                    return o;
//                }
//                return o;
//            }
//        });
//
//        //用对象来读取。
//        List<StudentInfo> studentInfos = reader.readAll(StudentInfo.class);
//
//        //根据学院分组
//        Map<String, List<StudentInfo>> collect = studentInfos.stream().collect(Collectors.groupingBy(StudentInfo::getStudentInstitute));
//
//        //遍历每个学院
//        for (String institute : collect.keySet()) {
////            List<StudentInfo> studentInfos1 = collect.get(institute);
////            System.out.println(studentInfos1);
////            继续教育学院的暂时先不插入数据
////            if (!institute.equals("继续教育学院")){
//                //根据班级分组
//                List<StudentInfo> instituteList = collect.get(institute);
//                Map<String, List<StudentInfo>> classList = instituteList.stream().collect(Collectors.groupingBy(StudentInfo::getStudentClass));
//
//                //准备数据
//                String instituteUUId = IdUtil.simpleUUID();
//                InstituteInfo instituteInfo = new InstituteInfo();
//                instituteInfo.setInstituteName(SecureDESUtil.encrypt(institute));
//                instituteInfo.setInstituteUuid(instituteUUId);
//                instituteInfo.setInstituteStudentNums(instituteList.size());
//                instituteInfoMapper.insert(instituteInfo);
//
//                //遍历每个班级
//                for (String className : classList.keySet()) {
////                    System.out.println("classsName: "+className);
//
//                    //获取每个班级的所有同学的学生信息
//                    List<StudentInfo> studentInfoList = classList.get(className);
//
//                    String classUUId = IdUtil.simpleUUID();
//                    ClassInfo classInfo = new ClassInfo();
//                    classInfo.setInstituteUuid(instituteUUId);
//                    classInfo.setClassName(SecureDESUtil.encrypt(className));
//                    classInfo.setClassUuid(classUUId);
//                    classInfo.setClassStudentsNum(studentInfoList.size());
//                    classInfoMapper.insert(classInfo);
//
//                    //遍历每个同学的信息进行数据的插入
//                    for (StudentInfo studentInfo : studentInfoList) {
//
//                        StudentInfo studentInfo1 = new StudentInfo();
//                        studentInfo1.setStudentId(SecureDESUtil.encrypt( studentInfo.getStudentId()) );
//                        studentInfo1.setStudentInstitute( instituteUUId );
//                        studentInfo1.setStudentSex( studentInfo.getStudentSex() );
//                        studentInfo1.setStudentName( SecureDESUtil.encrypt(studentInfo.getStudentName()) );
//                        studentInfo1.setStudentClass( classUUId );
//                        studentInfo1.setStudentPwd(SecureDESUtil.encrypt(studentInfo.getStudentId()));
//                        studentInfoMapper.insert(studentInfo1);
//
////                        System.out.println("序号：" + i + institute + "  " + instituteUUId + className + "  " + classUUId + studentInfo.getStudentId() + "  " + studentInfo.getStudentName());
////                    }
//                }
//            }

//        }

    }

    @Test
    public void test(){

        String testUUId = "ddf404db80b443759200a548f27f0ed4";
        TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));

        if(testPaper != null){

            Integer pSingleNum = testPaper.getPSingleNum();
            Integer pMultiNum = testPaper.getPMultipleNum();
            Integer pJudgeNum = testPaper.getPJudgmentNum();

            List<SqlQuestion> questionList = questionInfoMapper.selectJoinList(SqlQuestion.class, new MPJLambdaWrapper<>()
                            .select(QuestionInfo::getImgUrl,QuestionInfo::getQuestionType,QuestionInfo::getQuestionUuid, QuestionInfo::getCreateTime)
                            .selectAs(QuestionInfo::getQuestionTitle, SqlQuestion::getQuestionTitle)
                            .select(OptionInfo::getOptionContent, OptionInfo::getSerialLetter)
                            .leftJoin(OptionInfo.class, OptionInfo::getQuestionUuid, QuestionInfo::getQuestionUuid)
                            .eq(QuestionInfo::getTestUuid, testUUId));

            //按照题型分组
            Map<Integer, List<SqlQuestion>> collect = questionList.stream().collect(Collectors.groupingBy(SqlQuestion::getQuestionType));

            //准备数据
            List<Object> objects = new ArrayList<>();
            for(Integer key : collect.keySet()){
                //根据题型获取题目
                List<SqlQuestion> list = collect.get(key);

                //排序
                Collections.sort(list, new Comparator<SqlQuestion>() {
                    @Override
                    public int compare(SqlQuestion o1, SqlQuestion o2) {
                        return o1.getSerialLetter().compareTo(o2.getSerialLetter());
                    }
                });

                //按照题目的uuid分组
                Map<String, List<SqlQuestion>> collect1 = list.stream().collect(Collectors.groupingBy(SqlQuestion::getQuestionUuid));

                for (String key1 : collect1.keySet()) {
                    List<SqlQuestion> list1 = collect1.get(key1);
                    List<String> optionsList = list1.stream().map(SqlQuestion::getOptionContent).collect(Collectors.toList());
                }


                if(key == QuestionCode.singleChoice){
                    RandomUtil.randomArray(0,list.size(), pSingleNum);

                }
                if (key == QuestionCode.multipleChoice) {
                    RandomUtil.randomArray(0,list.size(), pMultiNum);
                }
                if (key == QuestionCode.judgmentQuestion) {
                    RandomUtil.randomArray(0,list.size(), pJudgeNum);
                }
            }


        }
    }


    @Test
    public void test02(){
//        Map<String, Object> map = new HashMap<>();
//        String testUUId = "ddf404db80b443759200a548f27f0ed4";
//        TestPaper testPaper = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().eq("test_uuid", testUUId));
//        if(testPaper != null){
//
//            //获取题目数量
//            Integer pSingleNum = testPaper.getPSingleNum();
//            Integer pMultiNum = testPaper.getPMultipleNum();
//            Integer pJudgeNum = testPaper.getPJudgmentNum();
//
//            //查询所有题目
//            List<SqlQuestion> questionList = questionInfoMapper.selectJoinList(SqlQuestion.class,
//                    new MPJLambdaWrapper<>()
//                            .select(QuestionInfo::getImgUrl,QuestionInfo::getQuestionType,QuestionInfo::getQuestionUuid, QuestionInfo::getCreateTime)
//                            .selectAs(QuestionInfo::getQuestionTitle, SqlQuestion::getQuestionTitle)
//                            .select(OptionInfo::getOptionContent, OptionInfo::getSerialLetter)
//                            .leftJoin(OptionInfo.class, OptionInfo::getQuestionUuid, QuestionInfo::getQuestionUuid)
//                            .eq(QuestionInfo::getTestUuid, testUUId));
//            Map<String, List<SqlQuestion>> collect = questionList.stream().collect(Collectors.groupingBy(SqlQuestion::getQuestionUuid));
//            List<RequestQuestion> tempList = new LinkedList<>();
//            for (String entry : collect.keySet()) {
//
//                List<SqlQuestion> sqlQuestionList = collect.get(entry);
//
//                Collections.sort(sqlQuestionList, new Comparator<SqlQuestion>() {
//                    @Override
//                    public int compare(SqlQuestion o1, SqlQuestion o2) {
//                        return o1.getSerialLetter().compareTo(o2.getSerialLetter());
//                    }
//                });
//
//                List<String> optionList = sqlQuestionList.stream().map(SqlQuestion::getOptionContent).collect(Collectors.toList());
//                RequestQuestion question = new RequestQuestion();
//                question.setQuestionUuid(entry);
//                question.setQuestionType(sqlQuestionList.get(0).getQuestionType());
//                question.setQuestionTitle(sqlQuestionList.get(0).getQuestionTitle());
//                question.setOptions(optionList);
//                question.setRightKey(null);
//                question.setCreateTime(sqlQuestionList.get(0).getCreateTime());
//                question.setImgUrl(sqlQuestionList.get(0).getImgUrl());
//                tempList.add(question);
//            }
//
//            Map<Integer, List<RequestQuestion>> collect1 = tempList.stream().collect(Collectors.groupingBy(RequestQuestion::getQuestionType));
//            List<RequestQuestion> resultsList = new LinkedList<>();
//            for (Integer key : collect1.keySet()) {
//                List<RequestQuestion> list = collect1.get(key);
//
//                log.info("题目数量：{}",list.size());
//                log.info("单选题数量：{}",pSingleNum);
//
//                if(key == QuestionCode.singleChoice){
//                    int[] ints = RandomUtil.randomArray(0, list.size()-1, pSingleNum);
//                    if(ints != null){
//                        for (int anInt : ints) {
//                            resultsList.add(list.get(anInt));
//                        }
//                    }
//                }
//                if (key == QuestionCode.multipleChoice) {
//                    int[] ints = RandomUtil.randomArray(0, list.size() - 1, pMultiNum);
//                    if(ints != null){
//                        for (int anInt : ints) {
//                            resultsList.add(list.get(anInt));
//                        }
//                    }
//                }
//                if (key == QuestionCode.judgmentQuestion) {
//                    int[] ints = RandomUtil.randomArray(0, list.size() - 1, pJudgeNum);
//                    if(ints != null){
//                        for (int anInt : ints) {
//                            resultsList.add(list.get(anInt));
//                        }
//                    }
//                }
//            }
//
//            log.info("查询到的题目数量为：{}", resultsList.size());
//        }
    }

    @Test
    void testScore(){
        String stuNo = "2019240004";
        String encrypt = SecureDESUtil.encrypt(stuNo);
        System.out.println(encrypt);

        String decrypt = "d8318a1b138965cccf2a1d1537f09e34";


    }

    @Test
    void testScoreInfo(){
        ScoreInfo scoreInfo = new ScoreInfo();
        scoreInfo.setStudentId("d8318a1b138965cccf2a1d1537f09e34");
        scoreInfo.setScore(100);
        scoreInfo.setCompletionTime(1);
        scoreInfo.setTestUuid("ddf404db80b443759200a548f27f0ed4");
        scoreInfo.setScoreUuid(IdUtil.simpleUUID());
        scoreInfoMapper.insert(scoreInfo);
    }
}

