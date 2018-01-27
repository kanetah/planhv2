package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.entity.Admin
import top.kanetah.planhv2.api.repository.AdminRepository
import top.kanetah.planhv2.api.service.AdminService

/**
 * created by kane on 2018/1/26
 */
@Service
class AdminServiceImpl @Autowired constructor(
        private val adminRepository: AdminRepository
) : AdminService {
    
    override fun adminWriteIn(password: String, validate: String): String {
        TODO("not implemented")
    }
    
    override fun adminCrossOut() {
        TODO("not implemented")
    }
    
    override fun getAllAdmins() = ArrayList<Any>().also { list ->
        adminRepository.findAll().forEach {
            list.add(object {
                val password = it.password
            })
        }
    }
    
    override fun createAdmin(
            password: String
    ) = adminRepository.save(Admin(password = password)) > 0
    
    override fun deleteAdmin(
            id: Int
    ) = adminRepository.delete(id) > 0
    
    override fun findAdmin(
            id: Int
    ) = adminRepository.find(id)
    
    override fun findAdmin(
            password: String
    ) = adminRepository.findByPassword(password)
}