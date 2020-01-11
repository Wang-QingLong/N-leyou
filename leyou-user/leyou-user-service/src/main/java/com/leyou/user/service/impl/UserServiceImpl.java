package com.leyou.user.service.impl;

import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.user.entity.User;
import com.leyou.user.service.UserService;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.mapper.UserMapper;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.MySqlExceptionUtils;
import com.leyou.utils.NumberUtils;
import com.leyou.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.leyou.constants.MQConstants.Exchange.SMS_EXCHANGE_NAME;
import static com.leyou.constants.MQConstants.RoutingKey.VERIFY_CODE_KEY;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/3 15:05
 * @description:
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //Spring加密
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 查验数据
     *
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkData(String data, Integer type) {

        //健壮性判断
        if (StringUtils.isBlank(data)) {
            log.error("前端请求参数有误");
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        User u = new User();
        switch (type) {
            case 1:
                u.setUsername(data);
                break;
            case 2:
                u.setPhone(data);
                break;
            default:
                log.error("前端请求参数有误");
                throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        Boolean count = false;
        try {
            int count1 = userMapper.selectCount(u);
            if (count1 == 0) {
                count = true;
            }
        } catch (Exception e) {
            log.error("查询数据有误");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        return count;
    }

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送短信
     *
     * @param phone
     */
    @Override
    public void sendCode(String phone) {


        // 验证手机号格式
        if (!RegexUtils.isPhone(phone)) {
            log.error("参数有误");
            throw new LyException(ExceptionEnum.INVALID_PHONE_NUMBER);
        }


        HashMap<String, String> msg = new HashMap<>();
        /*获取6位数的数字验证码*/
        String code = NumberUtils.generateCode(6);
        msg.put("code", code);
        //向消息中间件，添加消息
        amqpTemplate.convertAndSend(SMS_EXCHANGE_NAME, VERIFY_CODE_KEY, msg);
        //把发出的验证码保存在redis中
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        System.out.println(phone + "///" + code);
    }


    /**
     * 注册
     *
     * @param user
     * @param code
     */
    @Override
    public void register(User user, String code) {


        String key = user.getPhone();

        //判断key是否存在，还要判断值是否匹配
        if (!redisTemplate.hasKey(key) && !redisTemplate.opsForValue().get(key).equals(code)) {
            log.error("输入的手机号码或验证码不正确");
            throw new LyException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }

        //对user加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        //把用户数据保存到数据库
        int count = userMapper.insertSelective(user);

        if (1 != count) {
            log.error("数据保存失败");
            throw new LyException(ExceptionEnum.DATA_SAVE_ERROR);
        }
        //注册完成后，可以删除redis中的内容
        //TODO 先判断，是否快过期，比如过期时间不足3s，则不要删除，超过则手动删除

    }


    /**
     * 根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public UserDTO queryUserByUsernameAndPassword(String username, String password) {

        User recode = new User();
        recode.setUsername(username);

        User user = this.userMapper.selectOne(recode);
        //解密
        if (null == user || !passwordEncoder.matches(password, user.getPassword())) {
            throw new LyException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }

        return BeanHelper.copyProperties(user, UserDTO.class);
    }
}
