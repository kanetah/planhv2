package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.kanetah.planhv2.api.entity.Admin
import top.kanetah.planhv2.api.repository.AdminRepository

/**
 * created by kane on 2018/1/23
 */
@RestController
@RequestMapping("/test")
class TestController @Autowired constructor(
        private val adminRepository: AdminRepository
) {
    
    @RequestMapping("/poi")
    fun poi() = "poi"
    
    @RequestMapping("/admin")
    fun admin(): Admin {
//        val admin = Admin(1, "password")
//        println(admin)
        return adminRepository.getByPassword("poi")
    }
}
