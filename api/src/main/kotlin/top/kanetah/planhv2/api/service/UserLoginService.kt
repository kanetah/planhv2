package top.kanetah.planhv2.api.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * created by kane on 2018/1/22
 */
@Service
class UserLoginService : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return User(username, "123", MutableList(1, { GrantedAuthority { "user" } }))
    }
}
