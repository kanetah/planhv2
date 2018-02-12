package top.kanetah.planhv2.api.annotation

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController


/**
 * created by kane on 2018/2/12
 * restful 控制器，允许来自 planh.kanetah.top 的跨域请求
 */
@Target(AnnotationTarget.CLASS)
@RestController
@CrossOrigin("https://planh.kanetah.top")
annotation class PlanHApiController
