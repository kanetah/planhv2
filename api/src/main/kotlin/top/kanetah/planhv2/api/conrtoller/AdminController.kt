package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.AdminService

/**
 * created by kane on 2018/1/26
 */
@PlanHApiController
class AdminController @Autowired constructor(
        private val adminService: AdminService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/authorized"], method = [RequestMethod.POST])
    fun writeIn(
            @RequestParam password: String,
            @RequestParam validate: String
    ) = adminService.adminWriteIn(password, validate).let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val authorized = it
        }
    }
    
    @RequestMapping(value = ["/authorized"], method = [RequestMethod.DELETE])
    fun crossOut(
            @RequestParam authorized: String
    ) = adminService.takeIf {
        accessSecurityService.authCheck(authorized)
    }?.adminCrossOut(authorized)
    
    @RequestMapping(value = ["/admins"], method = [RequestMethod.GET])
    fun admins() = adminService.getAllAdmins()
    
    @RequestMapping(value = ["/admin"], method = [RequestMethod.POST])
    fun createAdmin(
            @RequestParam authorized: String,
            @RequestParam password: String
    ) = adminService.takeIf { accessSecurityService.authCheck(authorized) }
            ?.createAdmin(password).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/admin/{id}"], method = [RequestMethod.DELETE])
    fun deleteAdmin(
            @PathVariable id: Int
    ) = object {
        @JsonValue
        val success = adminService.deleteAdmin(id)
    }
    
    @RequestMapping(value = ["/admin/{id}"], method = [RequestMethod.GET])
    fun findAdmin(
            @PathVariable id: Int
    ) = object {
        @JsonValue
        val adminId = adminService.findAdmin(id)?.adminId ?: "null"
    }
    
    @RequestMapping(value = ["/admin"], method = [RequestMethod.GET])
    fun findAdminByPassword(
            @RequestParam password: String
    ) = object {
        @JsonValue
        val adminId = adminService.findAdmin(password)?.adminId ?: "null"
    }
}
