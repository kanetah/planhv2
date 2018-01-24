package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.kanetah.planhv2.api.entity.Admin
import top.kanetah.planhv2.api.entity.Team
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.repository.AdminRepository
import top.kanetah.planhv2.api.repository.TeamRepository
import top.kanetah.planhv2.api.repository.UserRepository

/**
 * created by kane on 2018/1/23
 */
@RestController
@RequestMapping("/test")
class TestController @Autowired constructor(
        private val adminRepository: AdminRepository,
        private val userRepository: UserRepository,
        private val teamRepository: TeamRepository
) {
    
    @RequestMapping("/poi")
    fun poi() = "poi"
    
    @RequestMapping("/admin")
    fun admin(): Admin {
        return adminRepository.findByPassword("poi")
    }
    
    @RequestMapping("/user")
    fun user(): User {
        return userRepository.findByCode("1521192213")
    }
    
    @RequestMapping("/team")
    fun team(): Team? {
        return teamRepository.findByIndex(6)
    }
}
