package com.leyou.auth.service;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.AppInfo;
import com.leyou.auth.entity.ApplicationInfo;
import com.leyou.auth.entity.Payload;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.mapper.AppMapper;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.clients.UserFeignClient;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.user.dto.UserDTO;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private JwtProperties props;


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private AppMapper appMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;




    /**
     * 登录
     *
     * @param username
     * @param password
     * @param response
     */
    public void login(String username, String password, HttpServletResponse response) {

        //跨服务获取用户信息
        UserDTO userDTO = null;
        try {
            userDTO = userFeignClient.queryUserByUsernameAndPassword(username, password);
        } catch (Exception e) {
            log.error("跨服务获取用户信息为空");
            throw new LyException(ExceptionEnum.USER_SERVICE_ERROR);
        }

        UserInfo userInfo = BeanHelper.copyProperties(userDTO, UserInfo.class);

        userInfo.setRole("假role");
       //获取token
        String token = JwtUtils.generateTokenExpireInMinutes(userInfo, props.getPrivateKey(), props.getUser().getExpire());

        CookieUtils.newBuilder()
                .name(props.getUser().getCookieName())
                .value(token)
                .domain(props.getUser().getCookieDomain())  //域名
                .path("/")       //指定在什么路径时生效
                .httpOnly(true)  //因为Js是默认可以操作Cookie的，设置为True时Js不能操作
                .maxAge(props.getUser().getCookieMaxAge())  //设置Cookie保存最大期限
                .response(response)
                .build();
    }





    /**
     * 不仅仅校验用户登录与否，如果下一次校验成功，此时应该重新生成token，cookie
     *
     * @param request
     * @param response
     * @return
     */
    public UserInfo verify(HttpServletRequest request, HttpServletResponse response) {
        //根据Token名字获取tonken

        String token = CookieUtils.getCookieValue(request, props.getUser().getCookieName());

        Payload<UserInfo> infoFromToken = null;
        try {
            //获取公钥信息
            infoFromToken = JwtUtils.getInfoFromToken(token, props.getPublicKey(), UserInfo.class);

 /*------以下优化-----------*/


 /*---优化3，结合退出登录黑名单，判断一下是否存在黑名单-------- */

            //判定解析的载荷的信息，的id是否在redis，如果在，则直接报错
            if (redisTemplate.hasKey(infoFromToken.getId())) {
                //当发现token是假的，应该删除

                deleteCookie(response);
                log.error("token已经失效，这是假的token");
                throw new LyException(ExceptionEnum.INVALID_TOKEN_COOKIE);
            }

 /*---优化3，结合退出登录黑名单，判断一下是否存在黑名单-------- */



            /*-----优化2：为了防止用户在一分钟内频繁点，发送请求，限制用户在一分钟内不管点多少次只发送一次请求*/

            //过期时间-29min如果比当前早，说明生成时间超过了1min,minusMinutes:减分钟
            if (new DateTime(infoFromToken.getExpiration()).minusMinutes(props.getUser().getExpire() - 1).isBeforeNow()) {

     /*-----优化2：为了防止用户在一分钟内频繁点，发送请求，限制用户在一分钟内不管点多少次只发送一次请求*/



                /*---优化1：为了防止cookie时间到期了用户还在使用，于是，当前端发送请求表示用户还在使用时，重新生成token---------*/
                //成功了应该重新生成token和cookie
                String newToken = JwtUtils.generateTokenExpireInMinutes(infoFromToken.getInfo(), props.getPrivateKey(), props.getUser().getExpire());

                CookieUtils.newBuilder()
                        .name(props.getUser().getCookieName())
                        .value(newToken)
                        .domain(props.getUser().getCookieDomain())
                        .path("/")
                        .httpOnly(true)
                        .maxAge(props.getUser().getCookieMaxAge())
                        .response(response)
                        .build();
 /*---优化1：为了防止cookie时间到期了用户还在使用，于是，当前端发送请求表示用户还在使用时，重新生成token---------*/



            }

            log.info("【授权中心】解析用户的token成功");


        } catch (Exception e) {
            log.error("【授权中心】解析用户的token失败");
            throw new LyException(ExceptionEnum.INVALID_TOKEN_COOKIE);
        }

/*-------以上优化----------*/

        return infoFromToken.getInfo();
    }




    /**
     * 退出登录时要把cliamsId存入redis，做成类似于黑名单
     *
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        /*----------------------以下优化部分---------------------*/


 /*-----------优化:当用户退出登录后，判断用户的过期时间是否超过3秒，如果已经不足3秒，就不管了，
 * 如果超过3秒就加入黑名单，防止其他人盗用token去登录*/

        String token = CookieUtils.getCookieValue(request, props.getUser().getCookieName());


        //获取所有的载荷信息
        Payload<UserInfo> infoFromToken = JwtUtils.getInfoFromToken(token, props.getPublicKey(), UserInfo.class);

        //获取过期时间
        Date expiration = infoFromToken.getExpiration();

        //当过期时间超过3s，则存入redis
        if (!new DateTime(expiration).minusSeconds(3).isBeforeNow()) {
            //如果过期时间还长，超过了3s，则把此tokenId存入redis，存储时长就是剩余过期时间
            redisTemplate.opsForValue().set(infoFromToken.getId(), "", expiration.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        /*-----------优化:当用户退出登录后，判断用户的过期时间是否超过3秒，如果已经不足3秒，就不管了，
         * 如果超过3秒就加入黑名单，防止其他人盗用token去登录*/




        /*----------------------以上优化部分---------------------*/


        deleteCookie(response);
    }





    /**删除cookie
     * @param response
     */
    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(props.getUser().getCookieName(), "");
        cookie.setDomain(props.getUser().getCookieDomain()); //cookie绑定了指定的域名，只有访问这个域名才会提供
        cookie.setPath("/");     //指定在什么路径请求时生效
        cookie.setMaxAge(0);     //设置cookie的有效期，设置0就代表立即失效了
        response.addCookie(cookie);
    }







    /**验证查询权限生成JWT并返回
     * @param id
     * @param secret
     * @return
     */
    public String authenticate(Long id, String secret) {

        //先根据服务id查询服务信息
        ApplicationInfo applicationInfo = this.appMapper.selectByPrimaryKey(id);

        //验证密码信息,和服务信息
        if (null == applicationInfo || !passwordEncoder.matches(secret, applicationInfo.getSecret())) {
            log.error("id或者secret参数不合法");
            throw new LyException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }

        //生成token中存放的info信息
        AppInfo appInfo = new AppInfo();
        appInfo.setId(id);
        appInfo.setServiceName(applicationInfo.getServiceName());

        List<Long> targetIdList = this.appMapper.selectTargetIdList(id);
        appInfo.setTargetList(targetIdList);



        //根据info生成token，有效时间25小时，24小时周期替换token
        return JwtUtils.generateTokenExpireInMinutes(appInfo, props.getPrivateKey(), props.getApp().getExpire());

    }


}
