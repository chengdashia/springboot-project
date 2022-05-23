package com.cloudDisk.service;

import com.cloudDisk.pojo.FileDel;
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
public interface FileDelService extends IService<FileDel> {

    //获取自己回收站的删除的文件
    Map<String, Object> getRecycleMyFileList(String uuId);

    //删除回收站的文件
    int delRecycleMyFile(String id, String fileId);
}
