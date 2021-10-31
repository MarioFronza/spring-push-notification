package com.example.springpushnotification.controller

import com.example.springpushnotification.service.NotificationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("notification")
class NotificationController(
    private val notificationService: NotificationService
) {

    @PostMapping("/all/{message}")
    fun sendToAll(@PathVariable("message") message: String) {
        notificationService.sendMessageToAllUsers(message)
    }

    @PostMapping("/one/{userId}/{message}")
    fun sendToOne(
        @PathVariable("userId") userId: String,
        @PathVariable("message") message: String
    ) {
        notificationService.sendMessageToUser(userId, message)
    }
}