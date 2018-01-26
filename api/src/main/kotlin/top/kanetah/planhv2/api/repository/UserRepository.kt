package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.User

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface UserRepository {
    
    fun save(user: User): Int
    
    fun delete(id: Int)
    
    fun update(user: User)
    
    fun findByCode(code: String): User?
}
