package com.leyou.filters;

import com.leyou.auth.entity.Payload;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.gatewayconfig.FilterProperties;
import com.leyou.gatewayconfig.JwtProperties;
import com.leyou.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局过滤器，防止用户未登录直接访问其他的服务
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
@Slf4j
public class LoginTokenFilter extends ZuulFilter {

    @Autowired
    private JwtProperties props;

    @Autowired
    private FilterProperties filterProps;


    /**前置
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**次序，越早越好
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    /**
     *  过滤器是否生效，返回true，生效，false不生效
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
       //获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
       //获取request
        HttpServletRequest request = currentContext.getRequest();

        //获取请求的URI
        String requestURI = request.getRequestURI();

        //遍历请求URI，如果当前请求以配置某个白名单为开始，则放行: 判断白名单
        for (String allowPath : filterProps.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        //获取当前请求的资源管理:上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = currentContext.getRequest();

        //从请求cookie中获取token
        String token = CookieUtils.getCookieValue(request, props.getUser().getCookieName());

        //解析token
        try {
            Payload<UserInfo> infoFromToken = JwtUtils.getInfoFromToken(token, props.getPublicKey(), UserInfo.class);
            log.info("【网关服务】解析用户token成功，此时请求URI为:{}", request.getRequestURI());
        } catch (Exception e) {
            log.error("【网关服务】解析用户token失败，此时请求URI为:{}", request.getRequestURI());
            currentContext.setSendZuulResponse(false);//设置不响应
            currentContext.setResponseStatusCode(401);//未授权不能请求
        }

        return null;
    }
}
