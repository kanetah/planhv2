package top.kanetah.planhv2.backend.conrtoller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.configuration.PortConfiguration
import javax.sound.sampled.Port

/**
 * created by kane on 2018/3/6
 */
@PlanHApiController
class IndexController {
    @RequestMapping("/", "/{port}/health")
    fun index(
            @PathVariable(required = false) port: Int?
    ) = if (port == null || PortConfiguration.PORT == port) {
        "Hello, PlanH V2 backend is running on port: ${PortConfiguration.PORT}"
    } else {
        throw IllegalAccessException()
    }
}
