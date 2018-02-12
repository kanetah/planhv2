package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.UserService

/**
 * created by kane on 2018/1/28
 */
@PlanHApiController
class UserController @Autowired constructor(
        private val userService: UserService,
        private val accessSecurityService: AccessSecurityService
) {

    @PostMapping("/token")
    fun login(
            @RequestBody values: Map<String, String>
    ) = userService.login("${values["userCode"]}", "${values["userName"]}").let {
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
    ) = userService.takeIf { accessSecurityService.tokenCheck(token, userId) }
            ?.configUser(token, theme, enableAccessToken ?: false)

    @RequestMapping(value = ["/users"], method = [RequestMethod.GET])
    fun users(
            @RequestParam token: String
    ) = userService.takeIf { accessSecurityService.tokenCheck(token) }?.getAllUser()

    @RequestMapping(value = ["/user"], method = [RequestMethod.POST])
    fun createUser(
            @RequestParam authorized: String,
            @RequestParam userCode: String,
            @RequestParam userName: String
    ) = userService.takeIf { accessSecurityService.authCheck(authorized) }
            ?.createUser(User(userCode = userCode, userName = userName)).let {
        object {
            @JsonValue
            val success = it
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
    ) = userService.takeIf { accessSecurityService.authCheck(authorized) }
            ?.updateUser(userId, userCode, userName).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.GET])
    fun findUser(
            @RequestParam authorized: String,
            @PathVariable("id") userId: Int
    ) = userService.takeIf { accessSecurityService.authCheck(authorized) }?.findUser(userId)
}
