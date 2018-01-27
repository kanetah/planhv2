package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Admin

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface AdminRepository {
    
    fun save(admin: Admin): Int
    
    fun delete(id: Int): Int
    
    fun update(admin: Admin)
    
    fun findAll(): Array<Admin>
    
    fun find(id: Int): Admin?
    
    fun findByPassword(password: String): Admin?
}