package com.cloudDisk;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.mapper.*;
import com.cloudDisk.pojo.*;
import com.cloudDisk.service.UserInfoService;
import com.cloudDisk.utils.Constants.Constant;
import com.cloudDisk.utils.date.DateCalculateUtil;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.random.RandomValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class CloudDiskApplicationTests {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private LabelInfoMapper labelInfoMapper;

    @Resource
    private FileLabelMapper fileLabelMapper;

    @Resource
    private UserLabelMapper userLabelMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private FolderFileInfoMapper folderFileInfoMapper;

    @Resource
    private FolderInfoMapper folderInfoMapper;

    @Resource
    private RootDirectoryInfoMapper rootDirectoryInfoMapper;

    @Test
    void contextLoads() {

        List<FolderInfo> folderInfos = folderInfoMapper.selectList(null);
        List<UserInfo> userInfos = userInfoService.getBaseMapper().selectList(null);
        for (FolderInfo folderInfo : folderInfos) {
            for (UserInfo userInfo : userInfos) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileInfoId(IdUtil.fastUUID());
                fileInfo.setFileId(IdUtil.simpleUUID());
                fileInfo.setFilePath(folderInfo.getFolderUrl()+"/"+userInfo.getUserName());


                fileInfo.setFileFolderId(folderInfo.getFolderId());


                fileInfo.setFileName(userInfo.getUserName());
                fileInfo.setFileType(RandomUtil.randomInt(1,5));

                fileInfo.setFileUploadId(userInfo.getUserId());

                fileInfo.setFileStatus(RandomUtil.randomInt(0,3));
                fileInfo.setFileOthers(RandomValue.getChineseName());
                fileInfo.setFileClickNums(RandomUtil.randomNumbers(5));
                fileInfo.setFileDownloadNums(RandomUtil.randomNumbers(5));
                fileInfo.setFileAvatar(IdUtil.fastUUID());
                fileInfoMapper.insert(fileInfo);

                FolderFileInfo folderFileInfo = new FolderFileInfo();
                folderFileInfo.setFolderFileInfoId(IdUtil.fastUUID());
                folderFileInfo.setFolderFileId(fileInfo.getFileId());
                folderFileInfo.setFolderFileType(Constant.FOLDER);
                folderFileInfo.setFolderPd(folderInfo.getFolderId());
                folderFileInfoMapper.insert(folderFileInfo);

            }

        }
    }


    @Test
    void testInsertUser(){

        for (int i = 0; i < 100; i++) {
            UserInfo user = new UserInfo();
            user.setUserInfoId(IdUtil.fastUUID());
            user.setUserId(IdUtil.simpleUUID());
            String tel = RandomValue.getTel();
            user.setUserTel(tel);
            user.setUserPwd(RandomUtil.randomNumbers(3));
            user.setUserName(tel+ RandomUtil.randomString(IdUtil.simpleUUID(),5));
            userInfoService.save(user);
        }

    }


    @Test
    void testDate(){
        FileInfo fileInfo = fileInfoMapper
                .selectOne(new QueryWrapper<FileInfo>()
                        .eq("file_id", "0134d26061b04003a7f78b20dd62db49"));
        Date fileUploadTime = fileInfo.getFileUploadTime();
        String s = DateCalculateUtil.remainingExpirationTime(fileUploadTime);
        log.info("{}",s);
    }

    @Test
    void insertFolder(){
        List<UserInfo> userInfos = userInfoService.getBaseMapper().selectList(null);
        for (UserInfo userInfo : userInfos) {
            FolderInfo folderInfo = new FolderInfo();
            folderInfo.setFolderInfoId(IdUtil.fastUUID());
            folderInfo.setFolderId(IdUtil.simpleUUID());
            folderInfo.setFolderName(RandomValue.getChineseName());
            folderInfo.setUserId(userInfo.getUserId());
            folderInfo.setFolderUrl("/"+System.currentTimeMillis()+userInfo.getUserId());
            folderInfo.setFolderTips(RandomValue.getRandomCharacters(RandomUtil.randomInt(5,10)));
            folderInfoMapper.insert(folderInfo);
        }
    }


    @Test
    void testLabel(){
        String str = "感同身受,语重心长,不以为然,气急败坏,天涯海角,惊心动魄,叹为观止,喜出望外,揠苗助长,语无伦次,情不自禁,小心翼翼,耐人寻味,叶公好龙,惟妙惟肖,莫名其妙,络绎不绝,栩栩如生,胸有成竹,微不足道,海市蜃楼,忧心忡忡,锲而不舍,名副其实,心有灵犀,流连忘返,乐不思蜀,生机勃勃,兴致勃勃,面面相觑,妄自菲薄,息息相关,美不胜收,滥竽充数,囫囵吞枣,豁然开朗,无动于衷,千钧一发,风云变幻,和风细雨,蒸蒸日上,沁人心脾,油然而生,秀色可餐,格物致知,疑惑不解,集腋成裘,不知所措,望尘莫及,津津有味,差强人意,道貌岸然,买椟还珠,争先恐后,卧薪尝胆,不言而喻,大相径庭,和颜悦色,如愿以偿,恍然大悟,旧话重提,石沉大海,荒淫无道,不翼而飞,狗屁不通,刻骨仇恨,瑶草奇花,人无千日好,花无百日红,劫后余生,利欲熏心,你推我让,月朗星稀,甘心乐意,废铜烂铁,内外勾结,爽爽快快,压卷之作,同病相怜";
        String[] split = str.split(",");

        for (String s : split) {
            LabelInfo labelInfo = new LabelInfo();
            labelInfo.setLabelInfoId(IdUtil.fastUUID());
            labelInfo.setInterestLabelId(IdUtil.simpleUUID());
            labelInfo.setLabelName(s);
            labelInfoMapper.insert(labelInfo);
        }



    }

    @Test
    void testFileLabel(){
        List<LabelInfo> labelInfos = labelInfoMapper.selectList(null);
        List<String> collect = labelInfos.stream().map(LabelInfo::getInterestLabelId).collect(Collectors.toList());

        List<FileInfo> fileInfos = fileInfoMapper.selectList(null);
        for (FileInfo fileInfo : fileInfos) {
            for (int i = 0;i < RandomUtil.randomInt(3,6);i++){
                FileLabel fileLabel = new FileLabel();
                fileLabel.setFileLableId(IdUtil.fastUUID());
                fileLabel.setFileId(fileInfo.getFileId());
                fileLabel.setFileLabelId(collect.get(RandomUtil.randomInt(0,collect.size())));
                fileLabelMapper.insert(fileLabel);
            }
        }
    }

//    @Test
//    void testMsg(){
//        String Msg = "{result:0,errmsg:OK,ext:,sid:2640:334969813716499917810569523,fee:1,isocode:CN}";
//        JSONObject jsonObject = JSON.parseObject(Msg);
//        String errmsg = (String) jsonObject.get("errmsg");
//        System.out.println(errmsg);
//    }

    @Test
    void testUserLabel(){
        List<LabelInfo> labelInfos = labelInfoMapper.selectList(null);
        List<String> collect = labelInfos.stream().map(LabelInfo::getInterestLabelId).collect(Collectors.toList());

        List<UserInfo> userInfos = userInfoService.getBaseMapper().selectList(null);
        for (UserInfo userInfo : userInfos) {
            for (int i = 0;i < RandomUtil.randomInt(3,6);i++){
                UserLabel userLabel = new UserLabel();
                userLabel.setUserLabelId(IdUtil.fastUUID());
                userLabel.setUserId(userInfo.getUserId());
                userLabel.setInterestLabelId(collect.get(RandomUtil.randomInt(0,collect.size())));
                userLabelMapper.insert(userLabel);
            }
        }
    }

    @Test
    void testRootDirectoryInfo(){

        List<FolderInfo> folderInfos = folderInfoMapper.selectList(null);
        for (FolderInfo folderInfo : folderInfos) {
            RootDirectoryInfo rootDirectoryInfo = new RootDirectoryInfo();
            rootDirectoryInfo.setRootDirectoryId(IdUtil.fastUUID());
            rootDirectoryInfo.setUserId(folderInfo.getUserId());
            rootDirectoryInfo.setFolderId(folderInfo.getFolderId());
            rootDirectoryInfoMapper.insert(rootDirectoryInfo);
        }
    }


    @Test
    void testRegister(){
        for (int i = 0; i < 100; i++) {
            String phone = RandomValue.getTel();
            String pwd = RandomValue.getEmail(5, 10);
            UserInfo user = new UserInfo();
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
                folderInfo.setFolderUrl("/" + System.currentTimeMillis() + user.getUserId());
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
                    if (rootInsert == 1) {
                        FolderFileInfo folderFileInfo = new FolderFileInfo();
                        folderFileInfo.setFolderFileInfoId(IdUtil.fastUUID());
                        folderFileInfo.setFolderFileId(folderInfo.getFolderId());
                        folderFileInfo.setFolderFileType(Constant.FOLDER);
                        folderFileInfo.setFolderPd(folderInfo.getFolderId());
                        folderFileInfoMapper.insert(folderFileInfo);

                    }
                }
            }


        }
    }
}
