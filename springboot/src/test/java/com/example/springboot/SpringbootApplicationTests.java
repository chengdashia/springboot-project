package com.example.springboot;

import com.example.springboot.convert.UserBO;
import com.example.springboot.convert.UserConvert;
import com.example.springboot.convert.UserDO;
import com.example.springboot.utils.mail.MailService;
import com.example.springboot.utils.mail.MailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;

@SpringBootTest
class SpringbootApplicationTests {

    @Resource
    private MailService mailService;

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private MailUtils mailUtils;

    @Test
    void test2(){
        UserDO userDO = new UserDO();
        userDO.setUsername("134213");
        userDO.setId(1);
        userDO.setPwd("123");

        UserBO convert = UserConvert.INSTANCE.convert(userDO);
        System.out.println(convert);

    }

    @Test
    void test() {
        //Template template = freeMarkerConfigurer.getConfiguration().getTemplate("template");
        //
        //Map<String,Object> model = new HashMap<>();
        //String s1 = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        //mailUtils.sendSimpleMail("1197788450@qq.com","信球","赵公子？出去活动一下。");

        //mailUtils.sendTemplateMail("2030290987@qq.com","测试html模板","register.html");

        mailUtils.sendEmailVerificationCode("2205447556@qq.com");

        //mailUtils.sendAttachmentsMail("2030290987@qq.com","测试","测试","E:\\hexo\\themes\\hexo-theme-matery\\source\\medias\\avatar.jpg");


        //mailService.sendSimpleTextMailActual("zjy","sb",new String[]{"1197788450@qq.com"},null,null,null);
    }


  
}
