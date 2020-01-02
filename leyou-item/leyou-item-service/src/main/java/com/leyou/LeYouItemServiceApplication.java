package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/30 18:03
 * @description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.mapper")
public class LeYouItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouItemServiceApplication.class);
    }
}
