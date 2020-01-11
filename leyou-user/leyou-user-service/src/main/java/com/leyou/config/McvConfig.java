package com.leyou.config;

import com.leyou.interceptors.PrivilegeTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器有很多，所以写好要配置，给整个服务中的拦截器列表中添加一个拦截器
 */
@Configuration
public class McvConfig implements WebMvcConfigurer {

    @Autowired
    private PrivilegeTokenInterceptor tokenInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器默认拦截路径为/**,excludePathPatterns表示拦截路径的排除路径
        registry.addInterceptor(tokenInterceptor).excludePathPatterns("/swagger-ui.html");
    }
}
