package com.cloudDisk.config.interceptors;

import cn.dev33.satoken.exception.BackResultException;
import cn.dev33.satoken.exception.StopMatchException;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouteFunction;
import cn.dev33.satoken.servlet.model.SaRequestForServlet;
import cn.dev33.satoken.servlet.model.SaResponseForServlet;
import cn.dev33.satoken.stp.StpUtil;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cloudDisk.utils.safe.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022/4/16 10:35
 */
public class SaRouteInterceptorNew implements HandlerInterceptor {
    /**
     * 每次进入拦截器的[执行函数]，默认为登录校验
     */
    public SaRouteFunction function = (req, res, handler) -> StpUtil.checkLogin();

    /**
     * 创建一个路由拦截器
     */
    public SaRouteInterceptorNew() {
    }

    /**
     * 创建, 并指定[执行函数]
     * @param function [执行函数]
     */
    public SaRouteInterceptorNew(SaRouteFunction function) {
        this.function = function;
    }

    /**
     * 静态方法快速构建一个
     * @param function 自定义模式下的执行函数
     * @return sa路由拦截器
     */
    public static SaRouteInterceptor newInstance(SaRouteFunction function) {
        return new SaRouteInterceptor(function);
    }


    // ----------------- 验证方法 -----------------

    /**
     * 每次请求之前触发的方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String token = request.getHeader("token");
            Map<String, Object> map = new HashMap<>();
            try {
                JWTUtil.verify(token);         //验证令牌
                return true;   //放行请求
            }catch(SignatureVerificationException e){
                e.printStackTrace();
                map.put("msg","无效签名！");
            }catch(TokenExpiredException e){
                e.printStackTrace();
                map.put("msg","token过期！");
            } catch(AlgorithmMismatchException e){
                e.printStackTrace();
                map.put("msg","token算法不一致！");
            }catch(Exception e){
                e.printStackTrace();
                map.put("msg","token无效！");
            }
            function.run(new SaRequestForServlet(request), new SaResponseForServlet(response), handler);
        } catch (StopMatchException e) {
            // 停止匹配，进入Controller
        } catch (BackResultException e) {
            // 停止匹配，向前端输出结果
            if(response.getContentType() == null) {
                response.setContentType("text/plain; charset=utf-8");
            }
            response.getWriter().print(e.getMessage());
            return false;
        }

        // 通过验证
        return true;
    }
}
