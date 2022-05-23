package com.example.springbootcaptcha.controller;


import com.example.springbootcaptcha.utils.VerifyImage;
import com.example.springbootcaptcha.utils.VerifyImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author 成大事
 * @since 2022/5/2 11:34
 */
@RestController
public class TestController {


    @Autowired
    VerifyImageUtil verifyImageUtil;

    //    定义一个Ant模式通配符的Resource查找器，可以用来查找类路径下或者文件系统中的资源。
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /**
     * 返回裁剪后的图及坐标
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    @RequestMapping("getCutImage")
    @ResponseBody
    public void getCutImage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

//        设置响应的信息类型
        httpServletResponse.setContentType("image/png");

//        得到图片文件夹中所有路径
        Resource[] resources = resolver.getResources("classpath*:/verifyImg/*");
        int ranNum = new Random().nextInt(resources.length);

//        通过随机的图片路径得到验证码图片
        VerifyImage verifyImage = verifyImageUtil.getVerifyImage(String.valueOf(resources[ranNum].getFile()));

        ByteArrayOutputStream baosCutImage = new ByteArrayOutputStream();
        ImageIO.write(verifyImage.getCutImage(), "png", baosCutImage);
        baosCutImage.flush();
        byte[] imageInByteCutImage = baosCutImage.toByteArray();
        baosCutImage.close();

////        session处理
        HttpSession mySession = httpServletRequest.getSession();
        mySession.setAttribute("ImageX", verifyImage.getXPosition());
        mySession.setAttribute("ImageY", verifyImage.getYPosition());
        System.out.println("ImageX:" + mySession.getAttribute("ImageX"));
        System.out.println("ImageY:" + mySession.getAttribute("ImageY"));

        OutputStream stream = httpServletResponse.getOutputStream();
        stream.write(imageInByteCutImage);
//        清空
        stream.flush();
        //        关闭
        stream.close();

    }

    /**
     * 返回原图
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    @RequestMapping("getSrcImage")
    @ResponseBody
    public void getSrcImage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

//        设置响应的信息类型
        httpServletResponse.setContentType("image/png");

//        得到图片文件夹中所有路径
        Resource[] resources = resolver.getResources("classpath*:/verifyImg/*");
        int ranNum = new Random().nextInt(resources.length);

//        通过随机的图片路径得到验证码图片
        VerifyImage verifyImage = verifyImageUtil.getVerifyImage(String.valueOf(resources[ranNum].getFile()));

        ByteArrayOutputStream baosSrcImage = new ByteArrayOutputStream();
        ImageIO.write(verifyImage.getSrcImage(), "png", baosSrcImage);
        baosSrcImage.flush();
        byte[] imageInByteSrcImage = baosSrcImage.toByteArray();
        baosSrcImage.close();

        OutputStream stream = httpServletResponse.getOutputStream();
        stream.write(imageInByteSrcImage);
        stream.flush();
        //        清空
        stream.flush();
        //        关闭
        stream.close();

    }

    /**
     * 得到图片验证的坐标，进行有效性校对
     *
     * @param x
     * @param y
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @RequestMapping("verifyImage")
    public String verifyImage(String x, String y, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        HttpSession httpSession = httpServletRequest.getSession();
        String ImageX = String.valueOf(httpSession.getAttribute("ImageX"));
        String ImageY = String.valueOf(httpSession.getAttribute("ImageY"));

//        计算验证图片坐标的误差值
        int absX = Math.abs(Integer.parseInt(x) - Integer.parseInt(ImageX));
        int absY = Math.abs(Integer.parseInt(y) - Integer.parseInt(ImageY));

        if (absX < 6 && absY < 6) {
            return "验证码正确";
        }

        return "验证码错误";
    }
}
