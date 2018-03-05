package top.kanetah.planhv2.api.service

import top.kanetah.planhv2.api.entity.Admin
import top.kanetah.planhv2.api.entity.User

/**
 * created by kane on 2018/1/22
 */
interface AccessSecurityService
{
    fun computeAuth(admin: Admin): String
    fun authCheck(authorized: String?): Boolean
    fun computeAccessToken(user: User): String
    fun computeToken(user: User): String
    fun tokenCheck(token: String?, id: Int? = null): Boolean
}
