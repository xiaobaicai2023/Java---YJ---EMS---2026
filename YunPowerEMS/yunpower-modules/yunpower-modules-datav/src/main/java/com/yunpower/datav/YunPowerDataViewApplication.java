package com.yunpower.datav;

import com.yunpower.common.security.annotation.EnableCustomConfig;
import com.yunpower.common.security.annotation.EnableRyFeignClients;
import com.yunpower.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: Hope
 * @created by Hope on 2024/5/7 13:46
 * @email: caoxiaotong@icihai.com
 * @description: 数据统计、数据大屏
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class YunPowerDataViewApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(YunPowerDataViewApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  数据服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}