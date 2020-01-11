package com.leyou.task;

import com.leyou.clients.AuthClient;
import com.leyou.gatewayconfig.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/5 16:05
 * @description: 定时获取token，保存token
 */
@Slf4j
@Component
public class PrivilegeTokenHolder {

    @Autowired
    private JwtProperties prop;

    private String token;

    /**
     * token刷新间隔 long必须为小写
     */
    private static final long TOKEN_REFRESH_INTERVAL = 86400000L;

    /**
     * token获取失败后重试的间隔
     */
    private static final long TOKEN_RETRY_INTERVAL = 10000L;

    @Autowired
    private AuthClient authClient;


    /**定时获取token
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = TOKEN_REFRESH_INTERVAL)
    public void loadToken() throws InterruptedException {
        while (true) {
            try {
                // 向ly-auth发起请求，获取JWT
                this.token = authClient.authorize(prop.getApp().getId(), prop.getApp().getSecret());
                log.info("【网关】定时获取token成功");
                break;
            } catch (Exception e) {
                log.info("【网关】定时获取token失败");
            }
            // 休眠10秒，再次重试
            Thread.sleep(TOKEN_RETRY_INTERVAL);
        }
    }

    public String getToken(){
        return token;
    }
}

/*
*
* 解读：

- @Scheduled：声明在方法上，方法中的代码就是定时任务执行的代码。支持下列属性：
  - fixedDelay：控制方法执行的间隔时间，是以上一次方法执行完开始算起，如上一次方法执行阻塞住了，那么直到上一次执行完，并间隔给定的时间后，执行下一次。
  - fixedRate：是按照一定的速率执行，是从上一次方法执行开始的时间算起，如果上一次方法阻塞住了，下一次也是不会执行，但是在阻塞这段时间内累计应该执行的次数，当不再阻塞时，一下子把这些全部执行掉，而后再按照固定速率继续执行
  - cron表达式：可以定制化执行任务，但是执行的方式是与fixedDelay相近的，也是会按照上一次方法结束时间开始算起。

此处我们选择了fixedDelay，并定义了固定时长：86400000毫秒，也就是24小时。

*
*
* */