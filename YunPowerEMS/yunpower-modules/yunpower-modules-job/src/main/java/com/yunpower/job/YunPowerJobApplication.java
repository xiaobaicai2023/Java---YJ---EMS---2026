package com.yunpower.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.yunpower.common.security.annotation.EnableCustomConfig;
import com.yunpower.common.security.annotation.EnableRyFeignClients;
import com.yunpower.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 定时任务
 * 
 * @author yunpower
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients   
@SpringBootApplication
public class YunPowerJobApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(YunPowerJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  定时任务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
