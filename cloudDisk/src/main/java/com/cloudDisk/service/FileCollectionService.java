package com.cloudDisk.service;

import com.cloudDisk.pojo.FileCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-13
 */
@Transactional
public interface FileCollectionService extends IService<FileCollection> {

    //查看收藏的文件
    Map<String, Object> getMyCollection(String uuId);
}
