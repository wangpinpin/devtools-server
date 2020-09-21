package com.wpp.devtools.model;

import com.wpp.devtools.service.UserService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @program: devtools-server
 * @description: 定时任务
 * @author: wpp
 * @create: 2020-07-09
 **/
@Configuration
@EnableScheduling
@Slf4j
public class Schedule implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ScheduledExecutorService taskExecutor = Executors.newScheduledThreadPool(3);
        scheduledTaskRegistrar.setScheduler(taskExecutor);
    }

    @Autowired
    private UserService userService;

    //每小时执行一次
    //发送订阅任务
    @Scheduled(cron = "0 0 * * * ?")
    public void task1() {
        long startTime = System.currentTimeMillis();
        int count = userService.sendSubscribe();
        log.info("定时任务: {}, 完成, 耗时： {}s, 发送量: {}", "发送订阅",
                (System.currentTimeMillis() - startTime) / 1000, count);
    }

}
