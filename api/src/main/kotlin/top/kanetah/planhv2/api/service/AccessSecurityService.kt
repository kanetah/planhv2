package top.kanetah.planhv2.api.service

import org.springframework.security.core.userdetails.UserDetailsService

/**
 * created by kane on 2018/1/22
 */
interface AccessSecurityService : UserDetailsService

fun String?.authCheck(): Boolean = !this.isNullOrEmpty()
fun String?.tokenCheck(): Boolean = !this.isNullOrEmpty()
