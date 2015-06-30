package com.nike.mm

import org.dozer.DozerBeanMapper
import org.dozer.Mapper
import org.jasypt.util.text.BasicTextEncryptor
import org.jasypt.util.text.StrongTextEncryptor
import org.jasypt.util.text.TextEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
@EnableSpringDataWebSupport
class AppConfig {

    @Value('${mm.encrypt.password}')
    String encryptPassword;

    @Bean
    StrongTextEncryptor strongTextEncryptor() {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();//BasicTextEncryptor
        textEncryptor.setPassword(this.encryptPassword);
        return textEncryptor;
    }

    @Bean
    TextEncryptor textEncryptor() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();//BasicTextEncryptor
        textEncryptor.setPassword(this.encryptPassword);
        return textEncryptor;
    }


    @Bean
    Mapper dozerBeanMapper() {
        return new DozerBeanMapper()
    }

    @Bean
    protected static ThreadPoolTaskExecutor serviceTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("service-task-executor-");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        return executor;
    }

    @Bean
    protected static ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        return scheduler;
    }
}
