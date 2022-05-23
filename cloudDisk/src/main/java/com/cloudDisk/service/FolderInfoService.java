package com.cloudDisk.service;

import com.cloudDisk.pojo.FolderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-13
 */
@Transactional
public interface FolderInfoService extends IService<FolderInfo> {

    //删除文件夹
    int deleteFolder(String id, String folderId);
}
