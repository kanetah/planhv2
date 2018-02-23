package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.entity.Admin
import top.kanetah.planhv2.api.entity.Auth
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.AdminService
import top.kanetah.planhv2.api.service.RepositoryService
import java.util.*

/**
 * created by kane on 2018/1/26
 */
@Service
class AdminServiceImpl @Autowired constructor(
        private val accessSecurityService: AccessSecurityService,
        private val repositoryService: RepositoryService
) : AdminService {
    
    override fun adminWriteIn(
            password: String, validate: String
    ) = repositoryService.adminRepository.findByPassword(password)?.let { admin ->
        repositoryService.authRepository.deleteByAdminId(admin.adminId)
        accessSecurityService.computeAuth(admin).let { auth ->
            if (repositoryService.authRepository.save(
                            Auth(adminId = admin.adminId, authorized = auth)) > 0
            ) auth else null
        }
    }
    
    override fun adminCrossOut(
            authorized: String
    ) = repositoryService.authRepository.deleteByAuthorized(authorized) > 0
    
    override fun getAllAdmins() = ArrayList<Any>().also { list ->
        repositoryService.adminRepository.findAll().forEach {
            list.add(object {
                @JsonValue
                val adminId = it.adminId
            })
        }
    }
    
    override fun createAdmin(
            password: String
    ) = repositoryService.adminRepository.save(Admin(password = password)) > 0
    
    override fun deleteAdmin(
            id: Int
    ) = repositoryService.adminRepository.delete(id) > 0
    
    override fun findAdmin(
            id: Int
    ) = repositoryService.adminRepository.find(id)
    
    override fun findAdmin(
            password: String
    ) = repositoryService.adminRepository.findByPassword(password)
}