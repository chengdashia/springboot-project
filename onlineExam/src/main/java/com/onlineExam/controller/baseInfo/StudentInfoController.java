package com.onlineExam.controller.baseInfo;


import com.onlineExam.pojo.es.Student;
import com.onlineExam.service.StudentInfoService;
import com.onlineExam.service.es.StudentEsDao;
import com.onlineExam.utils.result.CodeType;
import com.onlineExam.utils.result.MsgType;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.JWTUtils;
import com.onlineExam.utils.safe.SecureDESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-02-17
 */
@Api(tags = "学生信息")
@RestController
@RequestMapping("/studentInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StudentInfoController {

    private final StudentInfoService studentInfoService;

    private final StudentEsDao studentEsDao;

    private final ElasticsearchRestTemplate restTemplate;

    /**
     * 如果数据库有这个同学的信息。则直接返回token
     * @param studentId   学号
     * @return  R->token
     */
    @ApiOperation("学号+姓名登录")
    @PostMapping("/loginByStuName")
    public R LoginByStuName(@ApiParam(value = "学号",required = true) @NotBlank(message = "学号不能为空！") @Size(min = 10,max = 10) @RequestParam("studentId")  String studentId,
                            @ApiParam(value = "姓名",required = true) @NotBlank(message = "姓名不能为空！") @RequestParam("studentName") String studentName){
        studentId = SecureDESUtil.encrypt(studentId);
        studentName = SecureDESUtil.encrypt(studentName);
        int result = studentInfoService.login(studentId,studentName);
        if(result == StatusType.SUCCESS){
            Map<String, String> map = new HashMap<>();
            map.put("stuNo",SecureDESUtil.decrypt(studentId));
            map.put("stuName",SecureDESUtil.decrypt(studentName));
            String token = JWTUtils.getToken(map);
            return R.success(token);
        }else if(result == StatusType.NOT_EXISTS){       //学号+姓名查不到
            return R.failure("请先检查账号密码是否有误！或先申请添加信息！");
        }else if(result == StatusType.SQL_ERROR){         //数据库有问题
            return R.sqlError();
        }else {
            return R.failure();          //就是有问题
        }

    }


    /**
     * 如果数据库有这个同学的信息。则直接返回token
     * @param stuNo   学号
     * @param pwd     m密码
     * @return  R->token
     */
    @ApiOperation("学号+密码登录")
    @PostMapping("/loginByStuPwd")
    public R LoginByStuPwd(@ApiParam(value = "学号",required = true) @NotBlank(message = "学号不能为空！") @Size(min = 10,max = 10) @RequestParam("stuNo")  String stuNo,
                           @ApiParam(value = "密码",required = true) @NotBlank(message = "密码不能为空！") @RequestParam("pwd") String pwd){
        stuNo = SecureDESUtil.encrypt(stuNo);
        pwd = SecureDESUtil.encrypt(pwd);
        Map<String, Object> resultMap = studentInfoService.LoginByStuPwd(stuNo, pwd);
        int status = (int) resultMap.get("status");
        if(status == StatusType.SUCCESS){
            Map<String, String> map = new HashMap<>();
            map.put("stuNo",SecureDESUtil.decrypt(stuNo));
            String token = JWTUtils.getToken(map);
            Map<Object, Object> map1 = new HashMap<>();
            map1.put("token",token);
            map1.put("name",resultMap.get("data"));
            return R.success(map1);
        }else if(status == StatusType.NOT_EXISTS){       //学号+姓名查不到
            return R.failure(CodeType.ID_NOT_EXISTS,MsgType.USER_NOT_EXISTS);
        }else if(status == StatusType.PWD_ERROR){
            return R.failure(MsgType.USER_NOT_EXISTS);          //就是有问题
        }else {
            return R.sqlError();
        }

    }



    /**
     * 如果数据库有这个同学的信息。则直接返回token
     * @param pwd     m密码
     * @return  R->token
     */
    @ApiOperation("修改密码")
    @PostMapping("/updateStuPwd")
    public R updateStuPwd(HttpServletRequest request,
                           @ApiParam(value = "密码",required = true) @NotBlank(message = "密码不能为空！") @NotNull @RequestParam("pwd") String pwd){
        String stuNo = JWTUtils.getEncryptStuNo(request);
        int result = studentInfoService.updateStuPwd(stuNo, pwd);
        if(result == StatusType.SUCCESS){
            return R.success();
        }else if(result == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.failure();
        }

    }


    /**
     *
     * @param studentId        学号
     * @param studentName       姓名
     * @param instituteUUId     学院的uuid
     * @param classUUId        班级的uuid
     * @return    R
     */
    @ApiOperation("添加学生信息")
    @PostMapping("/addLogin")
    public R addStudentLogin(@ApiParam(value = "学号",required = true) @NotBlank(message = "学号不能为空！") @Size(min = 10,max = 10)  @RequestParam("studentId") String studentId,
                             @ApiParam(value = "姓名",required = true) @NotBlank(message = "姓名不能为空！") @RequestParam("studentName") String studentName,
                             @ApiParam(value = "学院的uuid",required = true) @NotBlank(message = "学院的uuid不能为空！") @RequestParam("instituteUUId") String instituteUUId,
                             @ApiParam(value = "班级的uuid",required = true) @NotBlank(message = "班级的uuid不能为空！") @RequestParam("classUUId") String classUUId){
        studentId = SecureDESUtil.encrypt(studentId);
        studentName = SecureDESUtil.encrypt(studentName);
        int result = studentInfoService.addLogin(studentId,studentName,instituteUUId,classUUId);
        if(result == StatusType.SUCCESS){
            Map<String, String> map = new HashMap<>();
            map.put("studentId",studentId);
            map.put("studentName",studentName);
            String token = JWTUtils.getToken(map);
            return R.success(token);
        }else if(result == StatusType.EXISTS){       //学生信息存在，不要重复添加
            return R.failure(MsgType.USER_EXISTS);
        }else if(result == StatusType.SQL_ERROR){         //数据库有问题
            return R.sqlError();
        }else {
            return R.failure();          //就是有问题
        }
    }

    @ApiOperation("重置密码")
    @PostMapping("/reSetPwd")
    public R reSetPwd(
            @ApiParam(value = "学号",required = true) @NotBlank(message = "学号不能为空！") @Size(min = 10,max = 10)  @RequestParam("studentId") String studentId,
            @ApiParam(value = "姓名",required = true) @NotBlank(message = "姓名不能为空！") @RequestParam("studentName") String studentName,
            @ApiParam(value = "学院的uuid",required = true) @NotBlank(message = "学院的uuid不能为空！") @RequestParam("instituteUUId") String instituteUUId,
            @ApiParam(value = "班级的uuid",required = true) @NotBlank(message = "班级的uuid不能为空！") @RequestParam("classUUId") String classUUId,
            @ApiParam(value = "密码",required = true) @NotBlank(message = "密码不能为空！") @RequestParam("newPwd") String newPwd
    ){
        studentId = SecureDESUtil.encrypt(studentId);
        studentName = SecureDESUtil.encrypt(studentName);
        newPwd = SecureDESUtil.encrypt(newPwd);
        int i = studentInfoService.reSetPwd(instituteUUId, classUUId, studentId, studentName, newPwd);
        if(i == StatusType.SUCCESS){
            return R.success();
        }else if(i == StatusType.NOT_EXISTS){
            return R.notExists();
        }else if(i == StatusType.SQL_ERROR){
            return R.sqlError();
        }else if(i == StatusType.CHOOSE_ERROR){
            return R.failure(MsgType.CHOOSE_ERROR);
        }else {
            return R.failure();
        }
    }


    @ApiOperation("通过学生名字搜")
    @PostMapping("/findByName")
    public R findByName(
            @ApiParam(value = "名字",required = true) @RequestParam("stuName") @NotBlank @NotNull String stuName
    ){

        String filed = "studentName";
//
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery(filed,stuName))
                .withHighlightBuilder(new HighlightBuilder()
                        .field(filed)
                        .preTags("<span style='color:red'>")
                        .postTags("</span>"))
                .build();

        SearchHits<Student> search = restTemplate.search(query, Student.class);
        List<SearchHit<Student>> searchHits = search.getSearchHits();


        ArrayList<Student> list = new ArrayList<>();
        for (SearchHit<Student> searchHit : searchHits) {
            Student student = searchHit.getContent();
            student.setStudentName(String.valueOf(searchHit.getHighlightField(filed)).substring(1,String.valueOf(searchHit.getHighlightField(filed)).length()-1));
            System.out.println(searchHit.getHighlightField(filed));
            list.add(student);
            System.out.println(searchHit.getContent());
        }


        return R.success(list);

    }

    @ApiOperation("通过班级名字搜")
    @PostMapping("/findByClassName")
    public R findByClassName(
            @ApiParam(value = "名字",required = true) @RequestParam("className") @NotBlank @NotNull String className
    ){
        String filed = "className";
//
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery(filed,className))
                .withHighlightBuilder(new HighlightBuilder()
                        .field(filed)
                        .preTags("<span style='color:red'>")
                        .postTags("</span>"))
                .build();

        SearchHits<Student> search = restTemplate.search(query, Student.class);
        List<SearchHit<Student>> searchHits = search.getSearchHits();


        ArrayList<Student> list = new ArrayList<>();
        for (SearchHit<Student> searchHit : searchHits) {
            Student student = searchHit.getContent();
            student.setClassName(String.valueOf(searchHit.getHighlightField(filed)).substring(1,String.valueOf(searchHit.getHighlightField(filed)).length()-1));
            list.add(student);
            System.out.println(searchHit.getContent());
        }


        return R.success(list);

    }


    @ApiOperation("通过学院名字搜")
    @PostMapping("/findByInstituteName")
    public R findByInstituteName(
            @ApiParam(value = "名字",required = true) @RequestParam("instituteName") @NotBlank @NotNull String instituteName
    ){
        String filed = "instituteName";

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery(filed,instituteName))
//                .withPageable(PageRequest.of(0,20))           //分页
                .withHighlightBuilder(new HighlightBuilder()
                        .field(filed)
                        .preTags("<span style='color:red'>")
                        .postTags("</span>"))
                .build();

        SearchHits<Student> search = restTemplate.search(query, Student.class);
        List<SearchHit<Student>> searchHits = search.getSearchHits();
        ArrayList<Student> list = new ArrayList<>();
        for (SearchHit<Student> searchHit : searchHits) {
            Student student = searchHit.getContent();
            student.setInstituteName(String.valueOf(searchHit.getHighlightField(filed)).substring(1,String.valueOf(searchHit.getHighlightField(filed)).length()-1));
            list.add(student);
            System.out.println(searchHit.getContent());
        }


        return R.success(list);

    }



    @ApiOperation("通过学号搜  高亮")
    @PostMapping("/findByStudentId")
    public R testSearchHigh(
            @ApiParam(value = "学号",required = true) @RequestParam("stuNo") @NotBlank @NotNull String stuNo
    ){

        String filed = "studentId";

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(
                QueryBuilders.wildcardQuery(filed, "*"+stuNo+"*"))
                .build();
        SearchHits<Student> search = restTemplate.search(query, Student.class);

        List<SearchHit<Student>> searchHits = search.getSearchHits();

        ArrayList<Student> list = new ArrayList<>();
        for (SearchHit<Student> searchHit : searchHits) {
            Student student = searchHit.getContent();
            student.setStudentId("<span style='color:red'>"+stuNo+"</span>"+student.getStudentId().substring(stuNo.length()));
            list.add(student);
//            System.out.println(searchHit.getContent());
        }
        return R.success(list);

    }

}

