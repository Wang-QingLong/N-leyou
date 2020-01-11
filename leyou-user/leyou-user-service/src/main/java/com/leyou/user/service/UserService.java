package com.leyou.user.service;

import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/3 15:03
 * @description:
 */
public interface UserService {

    /**
     * 查验数据是否是正确的
     *
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送短信
     *
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 注册
     *
     * @param user
     * @param code
     */
    void register(User user, String code);

    /**
     * 根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    UserDTO queryUserByUsernameAndPassword(String username, String password);
}
