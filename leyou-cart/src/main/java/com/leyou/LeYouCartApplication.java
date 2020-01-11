package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/10 20:34
 * @description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeYouCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouCartApplication.class);
    }
}
