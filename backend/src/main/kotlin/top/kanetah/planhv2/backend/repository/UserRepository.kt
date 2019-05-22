package top.kanetah.planhv2.backend.repository

import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.User

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface UserRepository {

    fun save(user: User): Int

    fun delete(id: Int): Int

    fun update(user: User): Int

    fun findAll(): ArrayList<User>?

    fun find(id: Int): User?

    fun findByCode(code: String): User?

    fun findByToken(token: String): User?

    fun count(): Int
}
