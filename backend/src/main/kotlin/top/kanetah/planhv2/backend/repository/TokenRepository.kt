package top.kanetah.planhv2.backend.repository

import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Token

/**
 * created by kane on 2018/1/28
 */
@DataAccess
interface TokenRepository {
    
    fun save(token: Token): Int
    
    fun deleteByUserId(userId: Int): Int
    
    fun deleteByToken(token: String): Int
    
    fun findByToken(token: String): Token?
}