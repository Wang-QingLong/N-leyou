package com.leyou.interceptors;

import com.leyou.auth.entity.AppInfo;
import com.leyou.auth.entity.Payload;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 处理器拦截器
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class PrivilegeTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties props;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader(props.getApp().getHeaderName());

        Payload<AppInfo> infoFromToken = null;
        try {
            //使用公钥解析token
            infoFromToken = JwtUtils.getInfoFromToken(token, props.getPublicKey(), AppInfo.class);
            log.info("【用户服务】解析 {} 的请求token成功",infoFromToken.getInfo().getServiceName());
        } catch (Exception e) {
            log.error("【用户服务】解析 未知 请求token失败");
            response.setStatus(401);
            return false;//返回false表示拦截
        }

        //从载荷中获取实际存放的内容
        AppInfo info = infoFromToken.getInfo();

        //获取请求者可以请求的服务的id集合
        List<Long> targetList = info.getTargetList();

        //校验判断当前id的集合是否在目标列表
        if (!targetList.contains(props.getApp().getId())){

            log.error("【用户服务】解析 {} 请求token成功，但请求没有权限",infoFromToken.getInfo().getServiceName());
            response.setStatus(401);
            return false;
        }

        return true;
    }

}
