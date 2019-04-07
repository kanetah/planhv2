package top.kanetah.planhv2.backend.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.AdminService

/**
 * created by kane on 2018/1/26
 */
@PlanHApiController
class AdminController @Autowired constructor(
        private val adminService: AdminService,
        private val accessSecurityService: AccessSecurityService
) {

    @PostMapping("/authorized")
    fun writeIn(
            @RequestBody values: Map<String, String>
    ) = adminService.adminWriteIn("${values["word"]}", "${values["key"]}").let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val authorized = it
        }
    }

    @DeleteMapping("/authorized")
    fun crossOut(
            @RequestBody values: Map<String, String>
    ) = adminService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.adminCrossOut("${values["authorized"]}")

    @PostMapping("/allow")
    fun allowNewKey(
            @RequestBody values: Map<String, String>
    ) = adminService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.allowNewKey(
            "${values["word"]}",
            "true" == values["clearAll"]?.trim(),
            "true" == values["allow"]?.trim()
    ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @PostMapping("/shutdown")
    fun shutdown(
            @RequestBody values: Map<String, String>
    ) = adminService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.shutdown(values["port"]?.toInt()).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/admins")
    fun admins(
            @RequestHeader authorized: String
    ) = adminService.takeIf {
        accessSecurityService.authCheck(authorized)
    }?.getAllAdmins() ?: arrayListOf()

    @PostMapping("/admin")
    fun createAdmin(
            @RequestBody values: Map<String, String>
    ) = adminService.takeIf { accessSecurityService.authCheck("${values["authorized"]}") }
            ?.createAdmin("${values["word"]}").let {
                object {
                    @JsonValue
                    val success = it
                }
            }

    @DeleteMapping("/admin/{id}")
    fun deleteAdmin(
            @PathVariable id: Int,
            @RequestHeader authorized: String
    ) = object {
        @JsonValue
        val success = adminService.takeIf {
            accessSecurityService.authCheck(authorized)
        }?.deleteAdmin(id)
    }

    @GetMapping("/admin/{id}")
    fun findAdmin(
            @PathVariable("id") id: Int,
            @RequestHeader("authorized") authorized: String
    ) = adminService.takeIf { accessSecurityService.authCheck(authorized) }?.findAdmin(id).let {
        object {
            @JsonValue
            val adminId = it?.adminId
            @JsonValue
            val word = it?.word
        }
    }

    @GetMapping("/admin")
    fun findAdminByWord(
            @RequestHeader word: String,
            @RequestHeader("authorized") authorized: String
    ) = adminService.takeIf { accessSecurityService.authCheck(authorized) }?.findAdmin(word)?.adminId.let {
        object {
            @JsonValue
            val success = it != null
            @JsonValue
            val adminId = it
        }
    }
}
