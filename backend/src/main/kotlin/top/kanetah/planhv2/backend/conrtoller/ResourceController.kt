package top.kanetah.planhv2.backend.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.ResourceService

/**
 * created by kane on 2018/1/31
 */
@PlanHApiController
class ResourceController @Autowired constructor(
        private val resourceService: ResourceService,
        private val accessSecurityService: AccessSecurityService
) {

    @GetMapping("/resources")
    fun resources() = resourceService.findAllResourceWithoutSubmissionResource()

    @PostMapping("/resource")
    fun createResource(
            @RequestParam token: String,
            @RequestPart file: MultipartFile
    ) = resourceService.takeIf {
        accessSecurityService.tokenCheck(token)
    }?.createResource(file).let {
        object {
            @JsonValue
            val success = it !== null
            @JsonValue
            val resourceUrl = it?.resourceUrl
        }
    }

    @DeleteMapping("/resource/{id}")
    fun deleteResource(
            @RequestBody values: Map<String, String>,
            @PathVariable("id") resourceId: Int
    ) = resourceService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.deleteResource(resourceId).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/resource/{id}")
    fun getResource(
            @PathVariable("id") resourceId: Int
    ) = resourceService.find(resourceId)

    @GetMapping("/download/{filename:.+}")
    fun download(
            @PathVariable("filename") fileName: String
    ) = resourceService.download(fileName)

    @GetMapping("/download/task/{subject}/{title}/{filename:.+}")
    fun downloadTaskResource(
            @PathVariable("subject") subjectName: String,
            @PathVariable("title") taskTitle: String,
            @PathVariable("filename") fileName: String
    ) = resourceService.download(subjectName, taskTitle, fileName)
}
