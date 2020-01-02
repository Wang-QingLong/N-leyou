package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/2 0:20
 * @description:
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableDiscoveryClient
public class LeYouSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouSearchApplication.class);
    }
}
