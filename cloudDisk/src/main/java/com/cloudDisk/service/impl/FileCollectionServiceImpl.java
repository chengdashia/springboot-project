package com.cloudDisk.service.impl;

import com.cloudDisk.pojo.FileCollection;
import com.cloudDisk.mapper.FileCollectionMapper;
import com.cloudDisk.pojo.FileInfo;
import com.cloudDisk.service.FileCollectionService;
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
public class FileCollectionServiceImpl extends ServiceImpl<FileCollectionMapper, FileCollection> implements FileCollectionService {

    @Resource
    private FileCollectionMapper fileCollectionMapper;

    //查看自己的收藏的文件
    @Override
    public Map<String, Object> getMyCollection(String uuId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> maps = fileCollectionMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(FileCollection::getFileId, FileCollection::getCollectionTime)
                    .select(FileInfo::getFileName, FileInfo::getFileType, FileInfo::getFileOthers)
                    .leftJoin(FileInfo.class, FileInfo::getFileId, FileCollection::getFileId)
                    .eq(FileCollection::getUserId, uuId));
            if (maps != null) {
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
