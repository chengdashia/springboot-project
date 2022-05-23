package com.onlineExam.service.es;

import com.onlineExam.pojo.es.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 成大事
 * @since 2022/4/10 20:27
 */
public interface StudentEsDao extends ElasticsearchRepository<Student,String> {
}
