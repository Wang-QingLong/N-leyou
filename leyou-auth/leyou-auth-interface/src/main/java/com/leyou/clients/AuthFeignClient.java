package com.leyou.clients;

import com.leyou.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/5 15:06
 * @description:
 */
@FeignClient("auth-service")
public interface AuthFeignClient {


    /**
     * 微服务认证并申请令牌
     * 因为服务发送的请求是没有cookie的，所以需要返回string
     * <p>
     * 这里通过id和密码去获取
     * <p>
     * authorization:授权的意思
     *
     * @param id     服务id
     * @param secret 密码
     * @return 无
     */
    @GetMapping("authorization")
    String authorize(@RequestParam("id") Long id,
                     @RequestParam("secret") String secret);
}
