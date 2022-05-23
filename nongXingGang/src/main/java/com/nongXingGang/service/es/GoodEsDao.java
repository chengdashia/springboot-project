package com.nongXingGang.service.es;

import com.nongXingGang.pojo.es.GoodEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 成大事
 * @since 2022/4/22 22:26
 */
@Repository
public interface GoodEsDao extends ElasticsearchRepository<GoodEs, String> {
}
