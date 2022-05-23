package com.cloudDisk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.mapper.FileInfoMapper;
import com.cloudDisk.mapper.FolderInfoMapper;
import com.cloudDisk.pojo.FileInfo;
import com.cloudDisk.pojo.FolderFileInfo;
import com.cloudDisk.mapper.FolderFileInfoMapper;
import com.cloudDisk.pojo.FolderInfo;
import com.cloudDisk.service.FolderFileInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.globalResult.StatusType;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-15
 */
@Service
public class FolderFileInfoServiceImpl extends ServiceImpl<FolderFileInfoMapper, FolderFileInfo> implements FolderFileInfoService {

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;

    @Resource
    private FolderInfoMapper folderInfoMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public Map<String,Object> getFolderFileByFolderId(String folderId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<FolderFileInfo> folderFileInfoList = folderFileInfoMapper.selectList(new QueryWrapper<FolderFileInfo>().eq("folder_pd", folderId));
            if (folderFileInfoList != null) {
                Map<Integer, List<FolderFileInfo>> collect = folderFileInfoList.stream().collect(Collectors.groupingBy(FolderFileInfo::getFolderFileType));
                List<String> folderIdList = null;
                List<String> fileIdList = null;
                for(Integer key : collect.keySet()){
                    if(key == Constant.FOLDER){
                        folderIdList = collect.get(key).stream().map(FolderFileInfo::getFolderFileId).collect(Collectors.toList());
                    }
                    if(key == Constant.FILE){
                        fileIdList = collect.get(key).stream().map(FolderFileInfo::getFolderFileId).collect(Collectors.toList());
                    }
                }
                List<Map<String, Object>> fileMaps = fileInfoMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                                .select(FileInfo::getFileId,FileInfo::getFileUploadTime,FileInfo::getFileOthers,FileInfo::getFileName)
                        .in(FileInfo::getFileId, fileIdList));
                List<Map<String, Object>> folderMaps = folderInfoMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                                .select(FolderInfo::getFolderId,FolderInfo::getFolderName,FolderInfo::getFolderCreateTime,FolderInfo::getFolderTips)
                        .in(FolderInfo::getFolderId, folderIdList));
                map.put("status", StatusType.SUCCESS);
                map.put("fileList", fileMaps);
                map.put("folderList", folderMaps);
            }else {
                map.put("status", StatusType.FOLDER_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;

    }
}
