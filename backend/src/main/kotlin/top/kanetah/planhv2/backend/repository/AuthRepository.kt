package top.kanetah.planhv2.backend.repository

import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Auth

/**
 * created by kane on 2018/1/27
 */
@DataAccess
interface AuthRepository {

    fun save(auth: Auth): Int

    fun deleteByAdminId(adminId: Int): Int

    fun deleteByAuthorized(authorized: String): Int

    fun find(id: Int): Auth?

    fun findByAuthorized(authorized: String): Auth?
}
