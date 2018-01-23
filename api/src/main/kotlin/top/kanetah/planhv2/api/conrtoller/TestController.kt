package top.kanetah.planhv2.api.conrtoller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.kanetah.planhv2.api.entity.Admin

/**
 * created by kane on 2018/1/23
 */
@RestController
@RequestMapping("/test")
class TestController {
    
    @RequestMapping("/poi")
    fun poi() = "poi"
    
    @RequestMapping("/admin")
    fun admin(): Admin {
        val admin = Admin(1, "password")
        println(admin)
        return admin
    }
}
