package com.cloudDisk.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudDisk.mapper.*;
import com.cloudDisk.pojo.*;
import com.cloudDisk.service.FileInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.file.FileUtil;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.hdfs.HdfsUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-10
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private FileLabelMapper fileLabelMapper;

    @Resource
    private FileDelMapper fileDelMapper;

    @Resource
    private FolderInfoMapper folderInfoMapper;

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;

    //根据分页获取文件信息
    @Override
    public Map<String, Object> getFileInfoListByPage(int page) {
        Map<String, Object> map = new HashMap<>();

        IPage<Map<String, Object>> mapIPage;
        try {
            mapIPage = fileInfoMapper.selectJoinMapsPage(new Page<>(page, 20),
                    new MPJLambdaWrapper<>()
                            .select(FileInfo.class, i -> !i.getColumn().equals("file_del")
                                    && !i.getColumn().equals("file_path")
                                    && !i.getColumn().equals("file_folder_id")
                                    && !i.getColumn().equals("file_info_id")
                            )
                            .select(FileInfo::getFileId)
                            .select(UserInfo::getUserName)
                            .leftJoin(UserInfo.class, UserInfo::getUserId, FileInfo::getFileUploadId)
                            .orderByAsc(FileInfo::getFileUploadTime)
                            .eq(FileInfo::getFileStatus, Constant.PUBLIC_TYPE));
            if (mapIPage != null) {
                map.put("status", StatusType.SUCCESS);
                map.put("data", mapIPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;

    }

    //根据文件id获取文件信息
    @Override
    public Map<String, Object> getFileInfoById(String fileId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> fileInfoMaps = fileInfoMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(FileInfo.class, i -> !i.getColumn().equals("file_path")
                            && !i.getColumn().equals("file_info_id")
                            && !i.getColumn().equals("file_folder_id")
                            && !i.getColumn().equals("file_status")
                            && !i.getColumn().equals("file_del"))
                    .select(UserInfo::getUserName, UserInfo::getUserAvatar)
                    .leftJoin(UserInfo.class, UserInfo::getUserId, FileInfo::getFileUploadId)
                    .eq(FileInfo::getFileId, fileId));

            List<Map<String, Object>> fileLabelMaps = fileLabelMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(LabelInfo::getLabelName)
                    .leftJoin(LabelInfo.class, LabelInfo::getInterestLabelId, FileLabel::getFileLabelId)
                    .eq(FileLabel::getFileId, fileId));

            List<String> labelList = new ArrayList<>();
            for (Map<String, Object> fileLabelMap : fileLabelMaps) {
                Object labelName = fileLabelMap.get("labelName");
                labelList.add(String.valueOf(labelName));
            }
            map.put("status", StatusType.SUCCESS);
            map.put("data", fileInfoMaps);
            map.put("label", labelList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;
    }

    //逻辑删除文件
    @Override
    public int delFile(String uuId, String fileId) {
        int delete = fileInfoMapper.delete(new UpdateWrapper<FileInfo>()
                .eq("file_id", fileId));
        if (delete > 0) {
            FileDel fileDel = new FileDel();
            fileDel.setFileId(fileId);
            fileDel.setFileDelId(IdUtil.fastUUID());
            fileDel.setUserId(uuId);
            int insert = fileDelMapper.insert(fileDel);
            if (insert > 0) {
                return StatusType.SUCCESS;
            } else {
                return StatusType.ERROR;
            }
        }else {
            return StatusType.ERROR;
        }
    }

    //随机获取十条文件信息
    @Override
    public Map<String, Object> getFIieRandom10(int random) {
        Map<String, Object> map = new HashMap<>();
        IPage<Map<String, Object>> mapIPage;
        try {
            mapIPage = fileInfoMapper.selectJoinMapsPage(new Page<>(random, 10),
                    new MPJLambdaWrapper<>()
                            .select(FileInfo.class, i -> !i.getColumn().equals("file_del")
                                    && !i.getColumn().equals("file_path")
                                    && !i.getColumn().equals("file_folder_id")
                                    && !i.getColumn().equals("file_info_id")
                            )
                            .select(FileInfo::getFileId)
                            .select(UserInfo::getUserName)
                            .leftJoin(UserInfo.class, UserInfo::getUserId, FileInfo::getFileUploadId)
                            .orderByAsc(FileInfo::getFileClickNums)
                            .eq(FileInfo::getFileStatus, Constant.PUBLIC_TYPE));
            if (mapIPage != null) {
                map.put("status", StatusType.SUCCESS);
                map.put("data", mapIPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;

    }

    //获取点击量十条文件信息
    @Override
    public Map<String, Object> getFIieTop10() {
        Map<String, Object> map = new HashMap<>();
        IPage<Map<String, Object>> mapIPage;
        try {
            mapIPage = fileInfoMapper.selectJoinMapsPage(new Page<>(1, 10),
                    new MPJLambdaWrapper<>()
                            .select(FileInfo.class, i -> !i.getColumn().equals("file_del")
                                    && !i.getColumn().equals("file_path")
                                    && !i.getColumn().equals("file_folder_id")
                                    && !i.getColumn().equals("file_info_id")
                            )
                            .select(FileInfo::getFileId)
                            .select(UserInfo::getUserName)
                            .leftJoin(UserInfo.class, UserInfo::getUserId, FileInfo::getFileUploadId)
                            .orderByAsc(FileInfo::getFileClickNums)
                            .eq(FileInfo::getFileStatus, Constant.PUBLIC_TYPE));
            if (mapIPage != null) {
                map.put("status", StatusType.SUCCESS);
                map.put("data", mapIPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;

    }

    @Override
    public int uploadFile(String id, String folderId, MultipartFile file, List<String> labelList, String fileName) {
        //先保存到本地
        Map<String, Object> map = FileUtil.saveFile(file, id);
        //保存成功！
        if (map.get("status").equals(StatusType.SUCCESS)) {
            FolderInfo folderInfo = folderInfoMapper.selectOne(new QueryWrapper<FolderInfo>()
                    .select("folder_url")
                    .eq("folder_id", folderId));
            //f父文件夹的路径
            String folderUrl = folderInfo.getFolderUrl();
            //上传到本地的文件
            File localFile = (File) map.get("filePath");

            //获取路径
            String filePath = localFile.getPath();
            //从本地上传到hdfs成功
            boolean upload = HdfsUtil.upload(filePath, folderUrl);
            if (upload) {
                //文件后缀
                String name = localFile.getName();
                String suffix = name.substring(name.lastIndexOf("."));

                //拼接成hdfs 中的文件路径
                String hdfsFilePath = folderUrl + "/" +name;
                String newHdfsFilePath = folderUrl + "/" +fileName +suffix;

                //将名字改一下
                HdfsUtil.rename(hdfsFilePath,fileName);

                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileInfoId(IdUtil.fastUUID());
                String fileId = IdUtil.simpleUUID();
                fileInfo.setFileId(fileId);
                fileInfo.setFilePath(newHdfsFilePath);
                fileInfo.setFileFolderId(folderId);
                fileInfo.setFileName(fileName);
                fileInfo.setFileType(RandomUtil.randomInt(1,5));
                fileInfo.setFileUploadId(id);
                int fileInsert = fileInfoMapper.insert(fileInfo);
                //如果文件上传成功，插入文件表
                if (fileInsert == 1 ) {
                    FolderFileInfo folderFileInfo = new FolderFileInfo();
                    folderFileInfo.setFolderFileInfoId(IdUtil.fastUUID());
                    folderFileInfo.setFolderFileId(fileId);
                    folderFileInfo.setFolderFileType(Constant.FILE);
                    folderFileInfo.setFolderPd(folderId);
                    int folderFileInsert = folderFileInfoMapper.insert(folderFileInfo);
                    //如果文件夹文件表插入成功，插入folderFileInfo表
                    if (folderFileInsert == 1) {
                        //插入标签
                        for (String s : labelList) {
                            FileLabel fileLabel = new FileLabel();
                            fileLabel.setFileLableId(IdUtil.fastUUID());
                            fileLabel.setFileLabelId(s);
                            fileLabel.setFileId(fileId);
                            fileLabelMapper.insert(fileLabel);
                        }
                        return StatusType.SUCCESS;
                    }else {
                        return StatusType.ERROR;
                    }

                }
                else {
                    return StatusType.ERROR;
                }

            }
            else {
                return StatusType.ERROR;
            }
        }else {
            return StatusType.ERROR;
        }
    }


}