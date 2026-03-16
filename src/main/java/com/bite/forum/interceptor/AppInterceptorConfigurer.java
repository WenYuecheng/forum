package com.bite.forum.interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppInterceptorConfigurer implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录拦截器
        registry.addInterceptor(loginInterceptor) // 添加⽤⼾登录拦截器
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/sign-in.html") // 排除登录HTML
                .excludePathPatterns("/sign-up.html") // 排除注册HTML
                .excludePathPatterns("/user/login") // 排除登录api接⼝
                .excludePathPatterns("/user/register") // 排除注册api接⼝
                .excludePathPatterns("/user/logout") // 排除退出api接⼝
                .excludePathPatterns("/swagger*/**") // 排除登录swagger下所有
                .excludePathPatterns("/v3*/**") // 排除登录v3下所有，与swag
                .excludePathPatterns("/dist/**") // 排除所有静态⽂件
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/**.ico")
                .excludePathPatterns("/js/**");
    }
}
