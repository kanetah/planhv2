package top.kanetah.planhv2.api.service.impl

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.service.UserLoginService

/**
 * created by kane on 2018/1/23
 */
@Service
class UserLoginServiceImpl : UserLoginService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return User(username, "123", MutableList(1, { GrantedAuthority { "user" } }))
    }
}
