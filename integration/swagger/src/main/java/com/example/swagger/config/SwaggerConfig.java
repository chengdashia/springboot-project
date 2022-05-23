package com.example.swagger.config;

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
@EnableSwagger2     //开启swagger2
public class SwaggerConfig {

    //配置了swagger的Docket的bean实例
    @Bean
    public Docket docket(Environment environment){

        //设置要显示的swagger环境
        Profiles profiles = Profiles.of("dev","test");
        boolean flag = environment.acceptsProfiles(profiles);


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .enable(flag)   //enable 是否启动swagger，如果为false，则swagger不能浏览器中访问
                .select()
                //RequestHandlerSelectors  配置要扫描接口的方式
                //basePackage :指定的包
                //any(） :扫描全部
                //none :都不扫描
                //withClassAnnotation :扫描类上的注解  参数是一个注解的反射对象
                //withMethodAnnotation :扫描类上的注解
                .apis(RequestHandlerSelectors.basePackage("com.example.swagger.controller"))
                //path()  过滤什么路径
//                .paths(PathSelectors.ant("/swagger"))
                .build();
    }

    //配置swagger 信息 =info
    private ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("董博阳", "https://www.baidu.com", "1847085602@qq.com");
        return new ApiInfo("项目api文档",
                        "成果",
                        "1.0",
                        "urn:tos",
                         contact,
                        "Apache 2.0",
                        "http://www.apache.org/licenses/LICENSE-2.0",
                        new ArrayList());

    }
}
