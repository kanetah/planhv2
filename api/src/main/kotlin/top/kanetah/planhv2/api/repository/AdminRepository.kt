package top.kanetah.planhv2.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import top.kanetah.planhv2.api.entity.Admin

/**
 * created by kane on 2018/1/23
 */
interface AdminRepository : JpaRepository<Admin, Long> {
    
    fun findByPassword(password: String): List<Admin>
}
