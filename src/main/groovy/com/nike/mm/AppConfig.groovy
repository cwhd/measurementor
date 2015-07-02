package com.nike.mm

import org.dozer.DozerBeanMapper
import org.dozer.Mapper
import org.jasypt.util.text.BasicTextEncryptor
import org.jasypt.util.text.TextEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import static org.elasticsearch.node.NodeBuilder.*;

@Configuration
@EnableSpringDataWebSupport
@EnableElasticsearchRepositories(basePackages = "com.nike.mm.repository")
class AppConfig {

    @Value('${mm.encrypt.password}')
    String encryptPassword;

    @Bean
    ElasticsearchTemplate elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(false).node().client());
    }

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
