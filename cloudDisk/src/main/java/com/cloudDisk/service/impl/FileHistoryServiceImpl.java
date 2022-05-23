package com.cloudDisk.service.impl;

import com.cloudDisk.pojo.FileHistory;
import com.cloudDisk.mapper.FileHistoryMapper;
import com.cloudDisk.pojo.FileInfo;
import com.cloudDisk.service.FileHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudDisk.utils.globalResult.StatusType;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-13
 */
@Service
public class FileHistoryServiceImpl extends ServiceImpl<FileHistoryMapper, FileHistory> implements FileHistoryService {

    @Resource
    private FileHistoryMapper fileHistoryMapper;

    @Override
    public Map<String, Object> getMyFileHistory(String uuId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> maps = fileHistoryMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(FileHistory::getHistoryId, FileHistory::getViewTime)
                    .select(FileInfo::getFileName, FileInfo::getFileType, FileInfo::getFileOthers)
                    .leftJoin(FileInfo.class, FileInfo::getFileId, FileHistory::getFileId)
                    .eq(FileHistory::getUserId, uuId));
            if(maps != null){
                map.put("status", StatusType.SUCCESS);
                map.put("data", maps);
            }else {
                map.put("status", StatusType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;
    }
}
