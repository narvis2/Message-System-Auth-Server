package com.narvi.messagesystem.service

import com.narvi.messagesystem.constant.KeyPrefix
import com.narvi.messagesystem.dto.domain.UserId
import com.narvi.messagesystem.entity.UserEntity
import com.narvi.messagesystem.repository.UserRepository
import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val sessionService: SessionService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val cacheService: CacheService,
) {

    @Transactional
    fun addUser(username: String, password: String): UserId {
        val entity = userRepository.save(
            UserEntity(
                username = username,
                password = passwordEncoder.encode(password),
                connectionCount = 0,
            )
        )
        log.info("User registered. userId: {}, username: {}", entity.userId, entity.username)
        return UserId(entity.userId)
    }

    @Transactional
    fun removeUser() {
        val username = sessionService.getUsername()
        val entity = userRepository.findByUsername(username) ?: throw NoSuchElementException()
        val userId = entity.userId.toString()
        userRepository.deleteById(entity.userId)
        cacheService.delete(
            listOf(
                cacheService.buildKey(KeyPrefix.USER_ID, username),
                cacheService.buildKey(KeyPrefix.USERNAME, userId),
                cacheService.buildKey(KeyPrefix.USER, userId),
                cacheService.buildKey(KeyPrefix.USER_INVITECODE, userId)
            )
        )

        log.info("User unregistered. userId: {}, username: {}", entity.userId, entity.username)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}