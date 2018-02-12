package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.ResourceService

/**
 * created by kane on 2018/1/31
 */
@PlanHApiController
class ResourceController @Autowired constructor(
        private val resourceService: ResourceService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/resources"], method = [RequestMethod.GET])
    fun resources() = resourceService.findAllResourceWithoutSubmissionResource()
    
    @RequestMapping(value = ["/resource"], method = [RequestMethod.POST])
    fun createResource(
            @RequestParam token: String,
            @RequestPart file: MultipartFile
    ) = resourceService.takeIf { accessSecurityService.tokenCheck(token) }?.createResource(file).let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val resourceUrl = it?.resourceUrl
        }
    }
    
    @RequestMapping(value = ["/resource/{id}"], method = [RequestMethod.DELETE])
    fun deleteResource(
            @RequestParam authorized: String,
            @PathVariable("id") resourceId: Int
    ) = resourceService.takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = it.deleteResource(resourceId)
        }
    }
    
    @RequestMapping(value = ["/resource/{filename:.+}"], method = [RequestMethod.GET])
    fun download(
            @PathVariable("filename") fileName: String
    ) = resourceService.download(fileName)
}
