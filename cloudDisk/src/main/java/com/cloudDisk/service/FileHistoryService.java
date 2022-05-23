package com.cloudDisk.service;

import com.cloudDisk.pojo.FileHistory;
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
public interface FileHistoryService extends IService<FileHistory> {

    //获取文件浏览记录
    Map<String, Object> getMyFileHistory(String uuId);
}
