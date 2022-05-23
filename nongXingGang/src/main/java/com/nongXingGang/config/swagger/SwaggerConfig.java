package com.nongXingGang.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2   //开启swagger2
public class SwaggerConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private TypeResolver typeResolver;

    //配置swagger的Docker的bean实例
    @Bean
    public Docket docket() {

        // 设置显示的swagger环境信息
        Profiles profiles = Profiles.of("dev","prod");

        // 判断是否处在自己设定的环境当中
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)   //enable 是否启动swagger，如果为false，则swagger不能浏览器中访问
                .select()
                //RequestHandlerSelectors  配置要扫描接口的方式
                //basePackage :指定的包
                //any(） :扫描全部
                //none :都不扫描
                //withClassAnnotation :扫描类上的注解  参数是一个注解的反射对象
                //withMethodAnnotation :扫描类上的注解
                .apis(RequestHandlerSelectors.basePackage("com.nongXingGang.controller"))
                //path()  过滤什么路径
//                .paths(PathSelectors.ant("/swagger"))
                .build();
    }

    //配置swagger 信息 = info
    private ApiInfo apiInfo() {
        //作者信息
        //作者信息
        Contact contact = new Contact("成大事", "82.157.157.133/", "1847085602@qq.com");
        return new ApiInfo("长顺县农营C-api文档",
                "成果",
                "1.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());


    }
}