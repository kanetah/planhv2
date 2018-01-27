package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Auth

/**
 * created by kane on 2018/1/27
 */
@DataAccess
interface AuthRepository {
    
    fun save(auth: Auth): Int
    
    fun delete(id: Int): Int
    
    fun deleteByAuthorized(authorized: String): Int
    
    fun find(id: Int): Auth?
    
    fun findByAuthorized(authorized: String): Auth?
}
