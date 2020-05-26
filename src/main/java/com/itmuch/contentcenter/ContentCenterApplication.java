package com.itmuch.contentcenter;

import com.itmuch.contentcenter.configuration.GlobalFeignConfiguration;
import com.itmuch.contentcenter.sentineltest.TestControllerBlockHandlerClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author 吴永华
 * @MapperScan 扫描Mybatis哪些包里面的接口
 */
@MapperScan("com.itmuch")
@SpringBootApplication
@EnableFeignClients // (defaultConfiguration = GlobalFeignConfiguration.class)
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    // @Bean是：在spring容器中，创建一个对象，类型RestTemplate；名称/ID是：restTemplate
    // <bean id="restTemplate" class="org.springframework.web.client.RestTemplate"/>
    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}