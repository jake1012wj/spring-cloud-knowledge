package com.jeesite.modules.blog;

import com.jeesite.modules.blog.spider.util.IdWorker;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@EnableCaching
@EnableRabbit
@EnableAdminServer
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class})
//@EnableOpenApi
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jeesite.modules", "com.jeesite.modules.blog.commons.feign"})
@ComponentScan(basePackages = {
        "com.jeesite.modules.blog.commons.config.durid",
        "com.jeesite.modules.blog.commons.config.feign",
        "com.jeesite.modules.blog.commons.config.redis",
        "com.jeesite.modules.blog.commons.config",
        "com.jeesite.modules.blog.commons.fallback",
        "com.jeesite.modules.blog.commons.handler",
        "com.jeesite.modules.blog.search",
        "com.jeesite.modules.blog.utils",
        "com.jeesite.modules.blog.picture",
        "com.jeesite.modules.blog.admin",
        "com.jeesite.modules.blog.xo.utils",
        "com.jeesite.modules.blog.xo.service",
        "com.jeesite.modules.blog.sms",
})
public class BlogApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        /**
         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        SpringApplication.run(BlogApplication.class, args);
    }

    /**
     * 设置时区
     */
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }
}
