package com.nike.mm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication
@EnableAsync
class MeasurementorApplication {

    static void main(String[] args) {
        SpringApplication.run MeasurementorApplication, args
    }
}
