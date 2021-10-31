package com.example.springpushnotification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class SpringPushNotificationApplication

fun main(args: Array<String>) {
    runApplication<SpringPushNotificationApplication>(*args)
}
