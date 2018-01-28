package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Token

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