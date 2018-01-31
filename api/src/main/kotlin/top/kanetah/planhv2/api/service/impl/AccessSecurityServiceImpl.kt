package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.RepositoryService

/**
 * created by kane on 2018/1/23
 */
@Service
class AccessSecurityServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : AccessSecurityService {
    
    private fun check(srt: String?, block: (String) -> Boolean): Boolean {
        return srt !== null && block(srt)
    }
    
    override fun authCheck(
            authorized: String?
    ) = check(authorized) {
        repositoryService.authRepository.findByAuthorized(it) !== null
    }
    
    override fun tokenCheck(
            token: String?, id: Int?
    ) = check(token) {
        repositoryService.tokenRepository.findByToken(it)?.userId === id
    }
    
//    override fun loadUserByUsername(username: String?): UserDetails {
//        return User(username, "123", MutableList(1, { GrantedAuthority { "user" } }))
//    }
}
