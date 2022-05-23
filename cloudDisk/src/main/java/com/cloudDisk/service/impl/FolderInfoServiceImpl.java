package com.cloudDisk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.mapper.FolderFileInfoMapper;
import com.cloudDisk.pojo.FolderFileInfo;
import com.cloudDisk.pojo.FolderInfo;
import com.cloudDisk.mapper.FolderInfoMapper;
import com.cloudDisk.service.FolderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.hdfs.HdfsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-13
 */
@Service
public class FolderInfoServiceImpl extends ServiceImpl<FolderInfoMapper, FolderInfo> implements FolderInfoService {

    @Resource
    private FolderInfoMapper folderInfoMapper;

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;

    //删除文件夹
    @Override
    public int deleteFolder(String id, String folderId) {
        FolderInfo folderInfo = folderInfoMapper.selectOne(new QueryWrapper<FolderInfo>()
                .select("folder_url")
                .eq("folder_id",folderId)
                .eq("user_id",id));
        if (folderInfo != null) {
            String folderUrl = folderInfo.getFolderUrl();
            int delFolderInfo = folderInfoMapper.delete(new QueryWrapper<FolderInfo>().eq("folder_id", folderId));
            if (delFolderInfo == 1) {
                //删除文件夹成功
                //删除folder_file_info表数据
                int delFolderFileInfo = folderFileInfoMapper.delete(new QueryWrapper<FolderFileInfo>().eq("folder_file_id", folderId));
                if (delFolderFileInfo == 1) {
                    //删除文件夹 文件成功
                    HdfsUtil.deleteFileOrFolder(folderUrl);
                    return StatusType.SUCCESS;
                }else {
                    return StatusType.ERROR;
                }
            }else {
                return StatusType.ERROR;
            }
        }else {
            return StatusType.NOT_EXISTS;
        }
    }
}
