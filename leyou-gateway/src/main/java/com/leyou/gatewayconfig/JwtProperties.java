package com.leyou.gatewayconfig;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;

@ConfigurationProperties("ly.jwt")
@Data
@Slf4j
public class JwtProperties implements InitializingBean {

    /**
     * 公钥地址
     */
    private String pubKeyPath;

    private PublicKey publicKey;

    private AppTokenInfo app = new AppTokenInfo();

    @Data
    public class AppTokenInfo{
        private Long id;
        private String secret;

        /**
         * 添加认证的请求头
         */
        private String headerName;
    }

    private UserTokenInfo user = new UserTokenInfo();

    @Data
    public class UserTokenInfo{
        private String cookieName;
    }

    //afterPropertiesSet等到所有的属性全部注入了值以后
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("【网关服务】初始化公钥失败！", e);
            throw new RuntimeException(e);
        }
    }

}
