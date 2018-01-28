package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.UserService

/**
 * created by kane on 2018/1/28
 */
@RestController
class UserController @Autowired constructor(
        private val userService: UserService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/token"], method = [RequestMethod.POST])
    fun login(
            @RequestParam userCode: String,
            @RequestParam userName: String
    ) = userService.login(userCode, userName).let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val token = it
        }
    }
    
    @RequestMapping(value = ["/token"], method = [RequestMethod.DELETE])
    fun logout(
            @RequestParam token: String
    ) = takeIf { accessSecurityService.tokenCheck(token) }?.let {
        object {
            @JsonValue
            val success = userService.logout(token)
        }
    }
    
    @RequestMapping(value = ["/user"], method = [RequestMethod.GET])
    fun findUser(
            token: String
    ) = userService.findUserByToken(token)
    
    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.PATCH])
    fun configUser(
            @RequestParam token: String,
            @PathVariable("id") userId: Int,
            @RequestParam theme: String?,
            @RequestParam enableAccessToken: Boolean?
    ) = takeIf { accessSecurityService.tokenCheck(token, userId) }?.let {
        userService.configUser(token, theme, enableAccessToken ?: false)
    }
    
    @RequestMapping(value = ["/users"], method = [RequestMethod.GET])
    fun users(
            @RequestParam token: String
    ) = takeIf { accessSecurityService.tokenCheck(token) }?.let {
        userService.getAllUser()
    }
    
    @RequestMapping(value = ["/user"], method = [RequestMethod.POST])
    fun createUser(
            @RequestParam authorized: String,
            @RequestParam userCode: String,
            @RequestParam userName: String
    ) = takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = userService.createUser(User(userCode = userCode, userName = userName))
        }
    }
    
    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.DELETE])
    fun deleteUser(
            @RequestParam authorized: String,
            @PathVariable id: Int
    ) = takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = userService.deleteUser(id)
        }
    }
    
    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.PUT])
    fun updateUser(
            @RequestParam authorized: String,
            @PathVariable("id") userId: Int,
            @RequestParam userCode: String?,
            @RequestParam userName: String?
    ) = takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = userService.updateUser(userId, userCode, userName)
        }
    }
    
    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.GET])
    fun findUser(
            @RequestParam authorized: String,
            @PathVariable("id") userId: Int
    ) = takeIf { accessSecurityService.authCheck(authorized) }?.let {
        userService.findUser(userId)
    }
}
