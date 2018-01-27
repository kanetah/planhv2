package top.kanetah.planhv2.api.service

import top.kanetah.planhv2.api.entity.Admin

/**
 * created by kane on 2018/1/26
 */
interface AdminService {
    fun adminWriteIn(password: String, validate: String): String?
    fun adminCrossOut(authorized: String): Boolean
    fun getAllAdmins(): ArrayList<Any>
    fun createAdmin(password: String): Boolean
    fun deleteAdmin(id: Int): Boolean
    fun findAdmin(id: Int): Admin?
    fun findAdmin(password: String): Admin?
}
