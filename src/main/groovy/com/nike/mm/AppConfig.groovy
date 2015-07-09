package com.nike.mm

import com.netflix.appinfo.AmazonInfo
import org.dozer.DozerBeanMapper
import org.dozer.Mapper
import org.jasypt.util.text.BasicTextEncryptor
import org.jasypt.util.text.TextEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
@EnableSpringDataWebSupport
@EnableElasticsearchRepositories(basePackages = "com.nike.mm.repository")
class AppConfig {

    @Value('${mm.encrypt.password}')
    String encryptPassword;

    @Bean
    TextEncryptor textEncryptor() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(this.encryptPassword);
        return textEncryptor;
    }


    @Bean
    Mapper dozerBeanMapper() {
        return new DozerBeanMapper()
    }

    @Bean
    protected static ThreadPoolTaskExecutor serviceTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("service-task-executor-");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        return executor;
    }

    @Bean
    protected static ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        return scheduler;
    }

    @Bean
    EurekaInstanceConfigBean eurekaInstanceConfigBean() {
        final EurekaInstanceConfigBean eurekaInstanceConfigBean = new EurekaInstanceConfigBean();
        eurekaInstanceConfigBean.dataCenterInfo = AmazonInfo.Builder.newBuilder().autoBuild("eureka.");
        return eurekaInstanceConfigBean
    }
}
