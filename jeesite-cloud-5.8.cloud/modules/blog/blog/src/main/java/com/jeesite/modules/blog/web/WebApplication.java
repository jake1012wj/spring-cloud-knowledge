package com.jeesite.modules.blog.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
//@EnableOpenApi
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients("com.jeesite.modules.blog.commons.feign")
@ComponentScan(basePackages = {
        "com.jeesite.modules.blog.commons.config",
        "com.jeesite.modules.blog.commons.fallback",
        "com.jeesite.modules.blog.utils",
        "com.jeesite.modules.blog.xo.utils",
        "com.jeesite.modules.blog.web",
        "com.jeesite.modules.blog.xo.service"})
public class WebApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(WebApplication.class, args);
    }

    /**
     * 设置时区
     */
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
