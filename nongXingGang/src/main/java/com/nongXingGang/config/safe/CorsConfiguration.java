//package com.nongXingGang.config.safe;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @author 成大事
// * @since 2022/5/24 16:14
// */
//@Configuration
//public class CorsConfiguration {
//
//    /*
//     * 这里主要为了解决跨域问题,所以重写addCorsMappings方法
//     */
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("*")
//                        .exposedHeaders("access-control-allow-headers",
//                                "access-control-allow-methods",
//                                "access-control-allow-origin",
//                                "access-control-max-age",
//                                "X-Frame-Options")
//                        .allowCredentials(false).maxAge(3600);
//            }
//        };
//    }
//
//}
