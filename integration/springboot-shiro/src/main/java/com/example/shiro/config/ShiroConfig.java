package com.example.shiro.config;

import com.example.config.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("websecurityManager") DefaultWebSecurityManager getDefaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(getDefaultWebSecurityManager);

        //添加shiro 的内置过滤器
        /**      认证过滤器：
         *       anon :   无需过滤就可以访问
         *       authc:   必须认证了才能访问
         *       authcBasic: 需要通过 httpBasic 认证
         *       user:    必须拥有  记住我 功能才能实现
         *
         *       授权过滤器：
         *       perms:   拥有对某个资源的权限才能访问
         *       role:    拥有某个角色的权限才能访问
         *       port: 请求的端口必须为指定值才能访问
         *       rest:请求必须是RESTful ,method为port ,get.delete,put
         *       ssl: 必须是安全的URL请求，协议为：HTTPS
         */

        Map<String, String> filterMap = new LinkedHashMap<>();



        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }

    //DefaultWebSecurityManager
    @Bean(name="websecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关联userRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }


    //创建 realm 对象  需要自定义类
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

}
