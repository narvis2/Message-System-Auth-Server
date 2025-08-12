package com.narvi.messagesystem.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SessionService {
    fun getUsername(): String = SecurityContextHolder.getContext().authentication.name
}