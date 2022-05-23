package com.cloudDisk.service;

import com.cloudDisk.pojo.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-10
 */
@Transactional
public interface FileInfoService extends IService<FileInfo> {

    //分页获取文件信息
    Map<String, Object> getFileInfoListByPage(int page);

    //根据文件id 获取文件信息
    Map<String, Object> getFileInfoById(String fileId);

    //根据文件id逻辑删除文件信息
    int delFile(String uuId, String fileId);

    //随机获取十条文件信息
    Map<String, Object> getFIieRandom10(int random);

    //获取点击量十条文件信息
    Map<String, Object> getFIieTop10();

    //上传文件
    int uploadFile(String id, String folderId, MultipartFile file, List<String> labelList, String fileName);
}
