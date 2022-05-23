package com.nongXingGang.service.es;

import com.nongXingGang.pojo.es.DemandEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 成大事
 * @since 2022/4/22 22:29
 */
public interface DemandEsDao extends ElasticsearchRepository<DemandEs, String> {
}
