package top.kanetah.planhv2.backend.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.ShutdownEndpoint
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.configuration.PortConfiguration
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
    ) = adminService.adminWriteIn("${values["password"]}", "${values["validate"]}").let {
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
    fun admins() = adminService.getAllAdmins()
    
    @PostMapping("/admin")
    fun createAdmin(
            @RequestBody values: Map<String, String>
    ) = adminService.takeIf { accessSecurityService.authCheck("${values["authorized"]}") }
            ?.createAdmin("${values["password"]}").let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @DeleteMapping("/admin/{id}")
    fun deleteAdmin(
            @PathVariable id: Int
    ) = object {
        @JsonValue
        val success = adminService.deleteAdmin(id)
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
            val password = it?.password
        }
    }
    
    @GetMapping("/admin")
    fun findAdminByPassword(
            @RequestHeader password: String
    ) = object {
        @JsonValue
        val adminId = adminService.findAdmin(password)?.adminId
    }
}
