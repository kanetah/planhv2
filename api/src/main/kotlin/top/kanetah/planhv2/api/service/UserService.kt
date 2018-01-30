package top.kanetah.planhv2.api.service

import top.kanetah.planhv2.api.entity.User

/**
 * created by kane on 2018/1/28
 */
interface
UserService {
    fun login(userCode: String, userName: String): String?
    fun logout(token: String): Boolean
    fun findUserByToken(token: String): User?
    fun configUser(token: String, theme: String?, enableAccessToken: Boolean): Any
    fun getAllUser(): ArrayList<Any>
    fun createUser(user: User): Boolean
    fun deleteUser(id: Int): Boolean
    fun updateUser(id: Int, userCode: String?, userName: String?): Boolean
    fun findUser(id: Int): User?
}
