package com.xuecheng.manage_cms;

import com.huanshare.huanSwaggerCore.core.EnableHuanSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableHuanSwagger
@EntityScan(basePackages={"com.xuecheng.framework.domain.cms"})//扫描cms实体
@ComponentScan(basePackages={"com.xuecheng.api","com.xuecheng.manage_cms"})//扫描接口
public class ManageCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class, args);
    }
}
