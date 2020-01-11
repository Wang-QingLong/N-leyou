package com.leyou.filters;

import com.leyou.gatewayconfig.JwtProperties;
import com.leyou.task.PrivilegeTokenHolder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * PRE_DECORATION_FILTER 是Zuul默认的处理请求头的过滤器，我们放到这个之后执行
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class TokenFilter extends ZuulFilter {

    @Autowired
    private JwtProperties props;

    @Autowired
    private PrivilegeTokenHolder privilegeTokenHolder;



    /**前置
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**次序
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    /**生效是否
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        //获取当前请求的资源管理器
        RequestContext currentContext = RequestContext.getCurrentContext();
        //把token存入请求头中
        currentContext.addZuulRequestHeader(props.getApp().getHeaderName(),privilegeTokenHolder.getToken());
        return null;
    }
}
