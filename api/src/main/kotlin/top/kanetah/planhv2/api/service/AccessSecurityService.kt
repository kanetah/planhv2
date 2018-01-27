package top.kanetah.planhv2.api.service

import org.springframework.security.core.userdetails.UserDetailsService

/**
 * created by kane on 2018/1/22
 */
interface AccessSecurityService : UserDetailsService {
    fun authCheck(authorized: String?): Boolean// = !this.isNullOrEmpty()
    fun tokenCheck(authorized: String?): Boolean// = !this.isNullOrEmpty()
}
