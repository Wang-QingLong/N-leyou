package com.leyou.clients;

import com.leyou.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/4 14:44
 * @description:
 */
@FeignClient("user-service")
public interface UserFeignClient {


    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    @GetMapping("/query")
    UserDTO queryUserByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password);
}
