package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 16:41
 * @description:
 */
@EnableEurekaServer       //声明这个应用是一个EurekaServer
@SpringBootApplication
public class LeYouRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouRegistryApplication.class);
    }
}
