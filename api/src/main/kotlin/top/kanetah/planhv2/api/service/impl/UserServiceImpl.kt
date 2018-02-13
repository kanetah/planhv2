package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.entity.Token
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.entity.UserConfig
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.RepositoryService
import top.kanetah.planhv2.api.service.UserService
import kotlin.collections.ArrayList

/**
 * created by kane on 2018/1/28
 */
@Service
class UserServiceImpl @Autowired constructor(
        private val accessSecurityService: AccessSecurityService,
        private val repositoryService: RepositoryService
) : UserService {
    
    override fun login(
            userCode: String, userName: String
    ) = repositoryService.userRepository.findByCode(userCode)?.let { user ->
        repositoryService.tokenRepository.deleteByUserId(user.userId)
        accessSecurityService.computeToken(user).let { token ->
            if (repositoryService.tokenRepository.save(
                            Token(userId = user.userId, token = token)) > 0
            ) token else null
        }
    }
    
    override fun logout(
            token: String
    ) = repositoryService.tokenRepository.deleteByToken(token) > 0
    
    override fun findUserByToken(
            token: String
    ) = repositoryService.userRepository.findByToken(token)
    
    override fun configUser(
            token: String, theme: String?, enableAccessToken: Boolean
    ) = repositoryService.userRepository.findByToken(token)?.let {
        val accessTokenFlag = it.userConfig.enableAccessToken != enableAccessToken && enableAccessToken
        val accessToken =
                if (accessTokenFlag)
                    accessSecurityService.computeAccessToken(it)
                else null
        val saved = repositoryService.userRepository.update(it.copy(
                userConfig = UserConfig(theme, enableAccessToken),
                accessToken = if (accessTokenFlag) accessToken else it.accessToken
        ))
        object {
            @JsonValue
            val success = saved
            @JsonValue
            val accessToken = accessToken
        }
    }
    
    override fun getAllUser() = ArrayList<Any>().also { list ->
        repositoryService.userRepository.findAll()?.forEach {
            list.add(object {
                @JsonValue
                val userId = it.userId
                @JsonValue
                val userCode = it.userCode
                @JsonValue
                val userName = it.userName
            })
        }
    }
    
    override fun createUser(
            user: User
    ) = repositoryService.userRepository.save(user) > 0
    
    override fun deleteUser(
            id: Int
    ) = repositoryService.userRepository.delete(id) > 0
    
    override fun updateUser(
            id: Int, userCode: String?, userName: String?
    ) = with(repositoryService.userRepository) {
        find(id)?.let {
            update(it.copy(userName = userName ?: it.userName, userCode = userCode ?: it.userCode)) > 0
        }
    } ?: false
    
    override fun findUser(
            id: Int
    ) = repositoryService.userRepository.find(id)
}