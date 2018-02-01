package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.entity.Admin
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.repository.AuthRepository
import top.kanetah.planhv2.api.repository.TokenRepository
import top.kanetah.planhv2.api.repository.UserRepository
import top.kanetah.planhv2.api.service.AccessSecurityService
import java.util.*

/**
 * created by kane on 2018/1/23
 */
@Service
class AccessSecurityServiceImpl @Autowired constructor(
        private val authRepository: AuthRepository,
        private val tokenRepository: TokenRepository,
        private val userRepository: UserRepository
) : AccessSecurityService {
    
    private fun checkHelper(
            srt: String?, block: (String) -> Boolean
    ) = srt !== null && block(srt)
    
    override fun computeAuth(
            admin: Admin
    ) = "planhII${admin.adminId}-${admin.password.hashCode()}-${Date().hashCode()}"
    
    override fun authCheck(
            authorized: String?
    ) = checkHelper(authorized) {
        authRepository.findByAuthorized(it) !== null
    }
    
    override fun computeAccessToken(
            user: User
    ) = "${user.userId}-planhII-${user.userCode.hashCode() + user.userName.hashCode()}-"
    
    override fun computeToken(
            user: User
    ) = "${computeAccessToken(user)}${Date().hashCode()}"
    
    override fun tokenCheck(
            token: String?, id: Int?
    ) = checkHelper(token) {
        tokenRepository.findByToken(it).let {
            if (id === null) it !== null else it?.userId === id
        } || (id !== null && userRepository.find(id)?.let {
            it.userConfig.enableAccessToken && token.equals(computeAccessToken(it))
        } ?: false)
    }

//    override fun loadUserByUsername(username: String?): UserDetails {
//        return User(username, "123", MutableList(1, { GrantedAuthority { "user" } }))
//    }
}
