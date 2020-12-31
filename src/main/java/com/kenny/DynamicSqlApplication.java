package com.kenny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class DynamicSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicSqlApplication.class, args);
    }

}
