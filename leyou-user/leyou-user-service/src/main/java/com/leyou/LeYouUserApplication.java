package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.leyou.user.mapper")
public class LeYouUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouUserApplication.class,args);
    }
}