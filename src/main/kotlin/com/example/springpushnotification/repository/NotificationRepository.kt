package com.example.springpushnotification.repository

import com.example.springpushnotification.model.NotificationJPA
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<NotificationJPA, Long>