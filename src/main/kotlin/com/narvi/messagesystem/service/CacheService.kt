package com.narvi.messagesystem.service

import mu.KotlinLogging
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CacheService(
    private val stringRedisTemplate: StringRedisTemplate,
) {

    fun delete(keys: List<String>): Boolean = try {
        stringRedisTemplate.delete(keys)
        true
    } catch (e: Exception) {
        log.error("Redis multi delete failed. keys: {}, cause: {}", keys, e.message)
        false
    }

    fun buildKey(prefix: String, key: String): String = "%s:%s".format(prefix, key)

    fun buildKey(prefix: String, firstKey: String, secondKey: String): String =
        "%s:%s:%s".format(prefix, firstKey, secondKey)

    companion object {
        private val log = KotlinLogging.logger {}
    }
}