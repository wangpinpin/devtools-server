package com.wpp.devtools;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
public class DevtoolsApplication {

    public static void main(String[] args) {

        //全局转JSON转驼峰
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.CamelCase;

        SpringApplication app = new SpringApplication(DevtoolsApplication.class);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";

        try {
            log.info("\n----------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\t{}://localhost:{}\n\t" +
                            "External: \t{}://{}:{}\n\t" +
                            "swagger: \t{}://localhost:{}/swagger-ui.html\n----------------------------------------------------------",
                    env.getProperty("spring.application.name"),
                    protocol,
                    env.getProperty("server.port"),
                    protocol,
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port"),
                    protocol,
                    env.getProperty("server.port"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
