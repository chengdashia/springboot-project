package com.cloudDisk.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.mapper.FileInfoMapper;
import com.cloudDisk.mapper.FolderFileInfoMapper;
import com.cloudDisk.pojo.FileDel;
import com.cloudDisk.mapper.FileDelMapper;
import com.cloudDisk.pojo.FileInfo;
import com.cloudDisk.pojo.FolderFileInfo;
import com.cloudDisk.service.FileDelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.date.DateCalculateUtil;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.hdfs.HdfsUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022-04-13
 */
@Service
public class FileDelServiceImpl extends ServiceImpl<FileDelMapper, FileDel> implements FileDelService {

    @Resource
    private FileDelMapper fileDelMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;

    //从回收站获取自己的删除记录
    @Override
    public Map<String, Object> getRecycleMyFileList(String uuId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> maps = fileDelMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(FileDel::getFileId, FileDel::getFileDelTime)
                    .select(FileInfo::getFileName, FileInfo::getFileType)
                    .leftJoin(FileInfo.class, FileInfo::getFileId, FileDel::getFileId)
                    .eq(FileDel::getUserId, uuId));
            if (maps != null) {
                for (Map<String, Object> m : maps) {
                    Date fileDelTime = (Date) m.get("fileDelTime");
                    String expirationTime = DateCalculateUtil.remainingExpirationTime(fileDelTime);
                    m.put("expirationTime", expirationTime);
                }
                map.put("status", StatusType.SUCCESS);
                map.put("data", maps);
            }else {
                map.put("status", StatusType.ERROR);
            }
        } catch (Exception e) {
            map.put("status", StatusType.SQL_ERROR);
            e.printStackTrace();
        }

        return map;
    }

    //从回收站删除文件
    @Override
    public int delRecycleMyFile(String id, String fileId) {
        FileInfo fileInfo = fileInfoMapper.getFilePathByFileId(fileId);
        if (fileInfo != null) {
            String filePath = fileInfo.getFilePath();
            int delete = fileInfoMapper.delFileInfoByFileId(fileId);
            if (delete == 1) {
                int delete1 = folderFileInfoMapper.delete(new QueryWrapper<FolderFileInfo>().eq("folder_file_id", fileId));
                if (delete1 == 1) {
                    return StatusType.SUCCESS;
                }else {
                    return StatusType.ERROR;
                }
            }else {
                return StatusType.ERROR;
            }
        }else {
            return StatusType.ERROR;
        }
    }
}
