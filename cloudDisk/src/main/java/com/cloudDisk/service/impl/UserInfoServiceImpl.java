package com.cloudDisk.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.mapper.*;
import com.cloudDisk.pojo.*;
import com.cloudDisk.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.globalResult.StatusType;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-04-10
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserLabelMapper userLabelMapper;

    @Resource
    private RootDirectoryInfoMapper rootDirectoryInfoMapper;

    @Resource
    private FolderInfoMapper folderInfoMapper;

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;


    @Override
    public Map<String, Object> getUserInfo(String uuId, int uInit) {
        Map<String, Object> map = new HashMap<>();
        try {

            Map<String, Object> userInfo = userInfoMapper.selectJoinMap(new MPJLambdaWrapper<>()
                    .select(UserInfo.class, i -> !i.getColumn().equals("user_info_id"))
                    .select(RootDirectoryInfo::getFolderId)
                    .leftJoin(RootDirectoryInfo.class, RootDirectoryInfo::getUserId, UserInfo::getUserId)
                    .eq(UserInfo::getUserId,uuId));
            if (userInfo != null) {
                List<Map<String, Object>> infoMaps = userLabelMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                        .select(LabelInfo::getLabelName)
                        .leftJoin(LabelInfo.class, LabelInfo::getInterestLabelId, UserLabel::getInterestLabelId)
                        .eq(UserInfo::getUserId, uuId));
                if (uInit == Constant.INIT) {
                    List<String> labelList = new ArrayList<>();
                    for (Map<String, Object> fileLabelMap : infoMaps) {
                        Object labelName = fileLabelMap.get("labelName");
                        labelList.add(String.valueOf(labelName));
                    }
                    map.put("status", StatusType.SUCCESS);
                    map.put("data", userInfo);
                    map.put("label", labelList);
                } else {
                    map.put("status", StatusType.SUCCESS);
                    map.put("data", userInfo);
                }

            } else {
                map.put("status", StatusType.NOT_EXISTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", StatusType.SQL_ERROR);
        }
        return map;
    }

    @Override
    public int register(String phone, String pwd) {
        UserInfo user = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("user_tel", phone));
        if (user == null) {
            user = new UserInfo();
            user.setUserInfoId(IdUtil.fastUUID());
            user.setUserId(IdUtil.simpleUUID());
            user.setUserTel(phone);
            user.setUserPwd(pwd);
            user.setUserName(phone + RandomUtil.randomString(IdUtil.simpleUUID(), 5));
            int userInsert = userInfoMapper.insert(user);
            //如果注册成功，则创建根目录
            if (userInsert == 1) {
                //先在文件表中创建
                FolderInfo folderInfo = new FolderInfo();
                folderInfo.setFolderInfoId(IdUtil.fastUUID());
                folderInfo.setFolderId(IdUtil.simpleUUID());
                folderInfo.setUserId(user.getUserId());
                folderInfo.setFolderUrl("/"+System.currentTimeMillis() + user.getUserId());
                folderInfo.setFolderName("我的资源");
                folderInfo.setFolderCreateTime(new Date());
                int folderInsert = folderInfoMapper.insert(folderInfo);
                //创建根目录
                if (folderInsert == 1) {
                    //初始化根目录
                    RootDirectoryInfo rootDirectoryInfo = new RootDirectoryInfo();
                    rootDirectoryInfo.setRootDirectoryId(IdUtil.fastUUID());
                    rootDirectoryInfo.setUserId(user.getUserId());
                    rootDirectoryInfo.setFolderId(folderInfo.getFolderId());
                    //插入根目录
                    int rootInsert = rootDirectoryInfoMapper.insert(rootDirectoryInfo);
                    if(rootInsert == 1){
                        FolderFileInfo folderFileInfo = new FolderFileInfo();
                        folderFileInfo.setFolderFileInfoId(IdUtil.fastUUID());
                        folderFileInfo.setFolderFileId(folderInfo.getFolderId());
                        folderFileInfo.setFolderFileType(Constant.FOLDER);
                        folderFileInfo.setFolderPd(folderInfo.getFolderId());
                        int insert = folderFileInfoMapper.insert(folderFileInfo);
                        if(insert == 1){
                            return StatusType.SUCCESS;
                        }else {
                            return StatusType.SQL_ERROR;
                        }
                    }else {
                        return StatusType.SQL_ERROR;
                    }
                }   else {
                    return StatusType.SQL_ERROR;
                }
            }else {
                return StatusType.SQL_ERROR;
            }
        }else {
            //手机号已经注册
            return StatusType.EXISTS;
        }
    }
}