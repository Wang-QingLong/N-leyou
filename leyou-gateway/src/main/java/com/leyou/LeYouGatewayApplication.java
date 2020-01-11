package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/30 16:55
 * @description:
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
@EnableScheduling   //开启定时任务开关 任务调度
public class LeYouGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouGatewayApplication.class);
    }
}
