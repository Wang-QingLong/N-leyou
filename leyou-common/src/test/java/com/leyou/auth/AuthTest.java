package com.leyou.auth;


import com.leyou.auth.entity.Payload;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;


public class AuthTest {

    private String privateFilePath = "F:/heima/rsa/id_rsa";
    private String publicFilePath = "F:/heima/rsa/id_rsa.pub";

    /**
     * RSA非对称加密技术
     *
     * @throws Exception
     */
    @Test
    public void testRSA() throws Exception {
        // 生成密钥对  secret:就是干扰值，相当于之前的盐，keySize是最大长度
        RsaUtils.generateKey(publicFilePath, privateFilePath, "hello", 2048);

        // 获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath);
        System.out.println("privateKey = " + privateKey);
        // 获取公钥
        PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath);
        System.out.println("publicKey = " + publicKey);
    }



    /**测试生成token
     * @throws Exception
     */
    @Test
    public void testJWT() throws Exception {
        // 获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath);
        // 生成token
        String token = JwtUtils.generateTokenExpireInMinutes(new UserInfo(10086L, "Wang", "admin"), privateKey, 5);
        System.out.println("token = " + token);

        // 获取公钥
        PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath);
        //从token里面使用公钥去解析信息
        Payload<UserInfo> info = JwtUtils.getInfoFromToken(token, publicKey, UserInfo.class);

        System.out.println("info.getExpiration() = " + info.getExpiration());
        System.out.println("info.getInfo() = " + info.getInfo());
        System.out.println("info.getId() = " + info.getId());
    }
}