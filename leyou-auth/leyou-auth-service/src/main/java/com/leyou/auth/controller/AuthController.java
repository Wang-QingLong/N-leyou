package com.leyou.auth.controller;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param response
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response) {

        this.authService.login(username, password, response);
        return ResponseEntity.ok().build();
    }





    /**
     * 前端只要刷新页面就会触发这个请求
     * 对cookie里面消息进行校验并返回用户名
     * 界面登录成功回显用户名
     *
     *
     * 获取Cookie也可以使用这个注解 @CookieValue("LY_TOKEN" String token)
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(this.authService.verify(request, response));
    }





    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        this.authService.logout(request, response);
        return ResponseEntity.ok().build();
    }





    /**
     * 微服务认证并申请令牌
     * 因为服务发送的请求是没有cookie的，所以需要返回string
     *
     * 这里通过id和密码去获取
     * @param id     服务id
     * @param secret 密码
     * @return 无
     */
    @GetMapping("authorization")
    public ResponseEntity<String> authorize(@RequestParam("id") Long id,
                                            @RequestParam("secret") String secret) {
        return ResponseEntity.ok(authService.authenticate(id, secret));
    }
}
