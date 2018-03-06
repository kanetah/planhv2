package top.kanetah.planhv2.backend.conrtoller

import org.springframework.web.bind.annotation.RequestMapping
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.configuration.PortConfiguration

/**
 * created by kane on 2018/3/6
 */
@PlanHApiController
class IndexController {
    @RequestMapping("/")
    fun index() = "Hello, PlanH V2 backend is running on port: ${PortConfiguration.PORT}"
}