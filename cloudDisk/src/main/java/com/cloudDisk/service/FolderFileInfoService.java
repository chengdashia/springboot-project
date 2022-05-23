package com.cloudDisk.service;

import com.cloudDisk.pojo.FolderFileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-15
 */
@Transactional
public interface FolderFileInfoService extends IService<FolderFileInfo> {

    //查询用户文件夹下的文件
    Map<String,Object> getFolderFileByFolderId(String folderId);

}
