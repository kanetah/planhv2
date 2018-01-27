package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.service.AdminService
import top.kanetah.planhv2.api.service.authCheck

/**
 * created by kane on 2018/1/26
 */
@RestController
class AdminController @Autowired constructor(
        private val adminService: AdminService
) {
    
    @RequestMapping(value = ["/authorized"], method = [RequestMethod.POST])
    fun writeIn(
            @RequestParam password: String,
            @RequestParam validate: String
    ) = adminService.adminWriteIn(password, validate)
    
    @RequestMapping(value = ["/authorized"], method = [RequestMethod.DELETE])
    fun crossOut(
            @RequestParam authorized: String
    ) = adminService.adminCrossOut().takeIf {
        authorized.authCheck()
    }
    
    @RequestMapping(value = ["/admins"], method = [RequestMethod.GET])
    fun admins() = adminService.getAllAdmins()
    
    @RequestMapping(value = ["/admin"], method = [RequestMethod.POST])
    fun createAdmin(
            @RequestParam authorized: String,
            @RequestParam password: String
    ) = object {
        val success = adminService.createAdmin(password)
    }.takeIf { authorized.authCheck() }
    
    @RequestMapping(value = ["/admin/{id}"], method = [RequestMethod.DELETE])
    fun deleteAdmin(
            @PathVariable id: Int
    ) = object {
        val success = adminService.deleteAdmin(id)
    }
    
    @RequestMapping(value = ["/admin/{id}"], method = [RequestMethod.GET])
    fun findAdmin(
            @PathVariable id: Int
    ) = object {
        val adminId = adminService.findAdmin(id)?.adminId ?: "null"
    }
    
    @RequestMapping(value = ["/admin"], method = [RequestMethod.GET])
    fun findAdminByPassword(
            @RequestParam password: String
    ) = object {
        val adminId = adminService.findAdmin(password)?.adminId ?: "null"
    }
}
