package com.onlineExam.controller;

import com.onlineExam.utils.file.FileUtil;
import com.onlineExam.utils.result.CodeType;
import com.onlineExam.utils.result.MsgType;
import com.onlineExam.utils.result.R;
import com.onlineExam.utils.result.StatusType;
import com.onlineExam.utils.safe.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 这整个文件是管理上传的
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/upload")
public class UploadController {


    /**
     * @param request 获取token
     * @param file    文件名
     * @return R
     */
    @PostMapping("/imgUpload")
    public R uploadImg(HttpServletRequest request,
                       @ApiParam("图片文件") @RequestParam("file") MultipartFile file) {
        String id = JWTUtils.getEncryptUUId(request);
//        System.out.println("token:"+token);
        Map<String, Object> map = FileUtil.saveImgFile(file, id, false);
        if (StatusType.SUCCESS == (int) map.get("status")) {
            return R.success(map);
        }
        return R.failure(map);
    }



    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public R uploadFiles(HttpServletRequest request,
                         @RequestParam("file") MultipartFile[] files) {
        String id = JWTUtils.getEncryptUUId(request);
        List<String> list = new ArrayList<>(files.length);
        for (MultipartFile f : files) {
            Map<String, Object> map = FileUtil.saveFile(f,id);
            int status = (int) map.get("status");
            if (StatusType.SUCCESS == status) {
                String filePath = (String) map.get("filePath");
                list.add(filePath);

            } else {
                return new R(CodeType.FILE_UPLOAD_ERROR, MsgType.FILE_UPLOAD_ERROR, null);
            }
        }
        return new R(CodeType.SUCCESS, MsgType.SUCCESS, list);
    }


    public Map<String,Object> getFileUploadPath(HttpServletRequest request, MultipartFile file) {
        String id = JWTUtils.getEncryptUUId(request);

        return FileUtil.saveFile(file, id);

    }


//    private static Object saveFile(MultipartFile file,String openid){
//        if (file.isEmpty()){
//            return "未选择文件";
//        }
//        String filename = file.getOriginalFilename(); //获取上传文件原来的名称
//        String filePath = "E:\\test\\"+openid+"\\";
//        File temp = new File(filePath);
//        if (!temp.exists()){
//            temp.mkdirs();
//        }
//
//        File localFile = new File(filePath+filename);
//        try {
//            file.transferTo(localFile); //把上传的文件保存至本地
//            System.out.println("fileJudge:"+FileTypeJudge.getMimeType(localFile));
//
////            System.out.println(file.getOriginalFilename()+" 上传成功");
//        }catch (Exception e){
//            e.printStackTrace();
//            return "上传失败";
//        }
//
//        return "ok";
//    }

}