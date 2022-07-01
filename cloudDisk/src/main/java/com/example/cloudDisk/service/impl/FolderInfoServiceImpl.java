package com.example.cloudDisk.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloudDisk.common.result.R;
import com.example.cloudDisk.common.result.ResultCode;
import com.example.cloudDisk.mapper.FolderFileInfoMapper;
import com.example.cloudDisk.mapper.FolderInfoMapper;
import com.example.cloudDisk.pojo.FolderFileInfo;
import com.example.cloudDisk.pojo.FolderInfo;
import com.example.cloudDisk.service.FolderInfoService;
import com.example.cloudDisk.utils.Constants.Constant;
import com.example.cloudDisk.utils.hdfs.HdfsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-07-01 11:54:54
 */
@Service
public class FolderInfoServiceImpl extends ServiceImpl<FolderInfoMapper, FolderInfo> implements FolderInfoService {

    @Resource
    private FolderInfoMapper folderInfoMapper;

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;

    /**
     * 创建文件夹
     * @param folderName     文件夹名称
     * @param folderDesc     文件夹描述
     * @param parentFolderId 父文件夹id
     * @return R
     */
    @Override
    public R<Object> createFolder(String folderName, String parentFolderId, String folderDesc) {
        String uId = (String) StpUtil.getLoginId();
        FolderInfo folder = folderInfoMapper.selectOne(new QueryWrapper<FolderInfo>()
                .select("folder_url")
                .eq("user_id", uId)
                .eq("folder_id", parentFolderId));

        String folderUrl = folder.getFolderUrl() + "/" + folderName;
        HdfsUtil.createFolder(folderUrl);
        try {
            FolderInfo folderInfo = new FolderInfo();
            folderInfo.setFolderInfoId(IdUtil.fastUUID());
            folderInfo.setFolderId(IdUtil.simpleUUID());
            folderInfo.setFolderName(folderName);
            folderInfo.setFolderTips(folderDesc);
            folderInfo.setUserId(uId);
            folderInfo.setFolderUrl(folderUrl);
            int save = folderInfoMapper.insert(folderInfo);
            if (save > 0) {
                //添加文件夹成功
                //folder_file_info表插入数据
                //uuid  , folder_file_id (file_info.file_id)   ,folder_file_type = 1 ,folder_pd = folder_id
                FolderFileInfo folderFileInfo = new FolderFileInfo();
                folderFileInfo.setFolderFileInfoId(IdUtil.fastUUID());
                folderFileInfo.setFolderFileId(folderInfo.getFolderId());
                folderFileInfo.setFolderPd(parentFolderId);
                folderFileInfo.setFolderFileType(Constant.FOLDER);
                folderFileInfoMapper.insert(folderFileInfo);
                return R.ok();
            }
            return R.error();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @Override
    public R<Object> deleteFolder(String folderId) {
        String uId = (String) StpUtil.getLoginId();
        FolderInfo folderInfo = folderInfoMapper.selectOne(new QueryWrapper<FolderInfo>()
                .select("folder_url")
                .eq("folder_id",folderId)
                .eq("user_id",uId));
        if (folderInfo != null) {
            String folderUrl = folderInfo.getFolderUrl();
            int delFolderInfo = folderInfoMapper.delete(new QueryWrapper<FolderInfo>().eq("folder_id", folderId));
            if (delFolderInfo > 1) {
                //删除文件夹成功
                //删除folder_file_info表数据
                int delFolderFileInfo = folderFileInfoMapper.delete(new QueryWrapper<FolderFileInfo>().eq("folder_file_id", folderId));
                if (delFolderFileInfo == 1) {
                    //删除文件夹 文件成功
                    HdfsUtil.deleteFileOrFolder(folderUrl);
                    return R.ok();
                }
                return R.error();

            }
            return R.error();
        }
        return R.error(ResultCode.NOT_EXIST.getCode(), ResultCode.NOT_EXIST.getMessage());
    }


    /**
     * 更改文件夹名称
     * @return  R
     */
    @Override
    public R<Object> updateFolderName(String folderId, String folderName) {
        FolderInfo folderInfo = folderInfoMapper.selectOne(new QueryWrapper<FolderInfo>().select("folder_url")
                .eq("folder_id", folderId));
        if (folderInfo != null) {
            String folderUrl = folderInfo.getFolderUrl();
            String substring = folderUrl.substring(folderUrl.lastIndexOf("/")+1);
            String newFilePath = folderUrl.replace(substring,folderName);
            folderInfo.setFolderUrl(newFilePath);
            folderInfo.setFolderName(folderName);
            int i = folderInfoMapper.updateById(folderInfo);
            if (i == 1) {
                HdfsUtil.folderRename(folderUrl, folderName);
                return R.ok();
            }
            return R.error();
        }
        return R.error(ResultCode.NOT_EXIST.getCode(), ResultCode.NOT_EXIST.getMessage());
    }

    /**
     * 更改文件夹备注
     * @param folderId 文件夹id
     * @param folderRemark 文件夹的备注
     * @return  R
     */
    @Override
    public R<Object> updateFolderRemark(String folderId, String folderRemark) {
        try {
            int update = folderInfoMapper.update(null, new UpdateWrapper<FolderInfo>()
                    .set("file_tips", folderRemark)
                    .eq("folder_id", folderId));
            if (update == 1){
                return R.ok();
            }
            return R.error();

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }
}
