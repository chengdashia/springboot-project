package com.onlineExam.pojo.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author 成大事
 * @since 2022/4/10 20:17
 */
@Data
@Document(indexName = "student",createIndex = false)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Keyword,analyzer = "ik_max_word")
    private String studentId;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String studentName;

    @Field(type = FieldType.Keyword)
    private String className;

    @Field(type = FieldType.Keyword)
    private String instituteName;

    @Field(type = FieldType.Integer)
    private Integer studentSex;
}
