package com.example.springpushnotification.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "notification")
data class NotificationJPA(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long? = null,
    private val username: String,
    private val idOneSignal: String
)