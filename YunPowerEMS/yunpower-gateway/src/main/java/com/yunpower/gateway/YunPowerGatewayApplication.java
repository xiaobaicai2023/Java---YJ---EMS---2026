package com.yunpower.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author yunpower
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class YunPowerGatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(YunPowerGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  云捷EMS网关启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
