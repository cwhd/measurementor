package com.nike.mm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableAsync
@EnableScheduling
class MeasurementorApplication extends WebMvcAutoConfiguration {

    static void main(String[] args) {
        SpringApplication.run MeasurementorApplication, args
    }
}
