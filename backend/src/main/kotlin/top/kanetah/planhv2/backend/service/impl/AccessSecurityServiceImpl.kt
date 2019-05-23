package top.kanetah.planhv2.backend.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.backend.entity.Admin
import top.kanetah.planhv2.backend.entity.User
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.RepositoryService
import java.util.*

/**
 * created by kane on 2018/1/23
 */
@Service
class AccessSecurityServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : AccessSecurityService {

    private fun checkHelper(
            srt: String?, block: (String) -> Boolean
    ) = srt !== null && block(srt)

    override fun computeAuth(
            admin: Admin
    ) = "\$planhII${admin.adminId}-${admin.word.hashCode()}-${Date().hashCode()}"

    override fun authCheck(
            authorized: String?
    ) = checkHelper(authorized) {
        repositoryService.authRepository.findByAuthorized(it) !== null
    }

    override fun computeAccessToken(
            user: User
    ) = "\$${user.userId}-planhII-${user.userCode.hashCode() + user.userName.hashCode()}-"

    override fun computeToken(
            user: User
    ) = "${computeAccessToken(user)}static"

    override fun tokenCheck(
            token: String?, id: Int?
    ) = checkHelper(token) {
        repositoryService.tokenRepository.findByToken(it).let {
            if (id === null) it !== null else it?.userId === id
        } || (id !== null && repositoryService.userRepository.find(id)?.let {
            it.userConfig.enableAccessToken && token.equals(computeAccessToken(it))
        } ?: false)
    }
}
