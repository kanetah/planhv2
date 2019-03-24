package top.kanetah.planhv2.backend.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.entity.User
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.SubmissionService
import top.kanetah.planhv2.backend.service.UserService

/**
 * created by kane on 2018/1/28
 */
@PlanHApiController
class UserController @Autowired constructor(
        private val userService: UserService,
        private val accessSecurityService: AccessSecurityService,
        private val submissionService: SubmissionService
) {

    @PostMapping("/token")
    fun login(
            @RequestBody values: Map<String, String>
    ) = userService.login(
            "${values["userCode"]}",
            "${values["userName"]}"
    ).let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val token = it
        }
    }

    @DeleteMapping("/token")
    fun logout(
            @RequestBody values: Map<String, String>
    ) = userService.takeIf {
        accessSecurityService.tokenCheck("${values["token"]}")
    }?.logout("${values["token"]}").let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/user")
    fun findUser(
            @RequestHeader token: String
    ) = userService.findUserByToken(token)

    @PatchMapping("/user/{id}")
    fun configUser(
            @PathVariable("id") userId: Int,
            @RequestBody values: Map<String, String>
    ) = userService.takeIf {
        accessSecurityService.tokenCheck("${values["token"]}", userId)
    }?.configUser(
            "${values["token"]}",
            values["theme"],
            values["enableAccessToken"]?.toBoolean() ?: false
    )

    @GetMapping("/users")
    fun users(
            @RequestHeader(required = false) authorized: String?,
            @RequestHeader(required = false) token: String?
    ) = userService.let {
        accessSecurityService.run {
            when {
                !authorized.isNullOrBlank() -> {
                    it.takeIf { authCheck(authorized) }?.getAllUserWithLastSubmission()
                }
                !token.isNullOrBlank() -> {
                    it.takeIf { tokenCheck(token) }?.getAllUser()
                }
                else -> throw RuntimeException("security check fail")
            }
        }
    }

    @PostMapping("/user")
    fun createUser(
            @RequestBody values: Map<String, String>
    ) = userService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.createUser(
            User(userCode = "${values["userCode"]}", userName = "${values["userName"]}")
    ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @PostMapping("/users")
    fun createUserBatch(
            @RequestParam authorized: String,
            @RequestPart file: MultipartFile
    ) = userService.takeIf {
        accessSecurityService.authCheck(authorized)
    }?.createUserBatch(file).let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val count = it
        }
    }

    @DeleteMapping("/user/{id}")
    fun deleteUser(
            @RequestBody values: Map<String, String>,
            @PathVariable id: Int
    ) = userService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.deleteUser(id).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @PutMapping("/user/{id}")
    fun updateUser(
            @PathVariable("id") userId: Int,
            @RequestBody values: Map<String, String>
    ) = userService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.updateUser(
            userId,
            "${values["userCode"]}",
            "${values["userName"]}"
    ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/user/{id}")
    fun findUser(
            @RequestHeader authorized: String,
            @PathVariable("id") userId: Int
    ) = userService.takeIf { accessSecurityService.authCheck(authorized) }?.findUser(userId)

    @GetMapping("/user/submission/{id}")
    fun findSubmissionFroAdmin(
            @RequestHeader authorized: String,
            @PathVariable("id") userId: Int
    ) = submissionService.takeIf {
        accessSecurityService.authCheck(authorized)
    }?.findByUserId(userId)
}
