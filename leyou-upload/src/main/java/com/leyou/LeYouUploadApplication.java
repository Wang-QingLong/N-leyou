package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/19 15:11
 * @description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeYouUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouUploadApplication.class);
    }
}
