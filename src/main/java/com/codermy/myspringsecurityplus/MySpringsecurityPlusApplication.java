package com.codermy.myspringsecurityplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author codermy
 * @createTime 2020/7/10
 */
@SpringBootApplication
@EnableScheduling
public class MySpringsecurityPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringsecurityPlusApplication.class, args);
    }

}
