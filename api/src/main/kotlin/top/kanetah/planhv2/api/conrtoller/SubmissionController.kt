package top.kanetah.planhv2.api.conrtoller

import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.SubmissionService
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController

/**
 * created by kane on 2018/1/31
 */
@PlanHApiController
class SubmissionController(
        private val submissionService: SubmissionService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/submissions"], method = [RequestMethod.GET])
    fun submissions(
            @RequestParam token: String
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }?.findAllSubmission(token)
    
    @RequestMapping(value = ["/submission"], method = [RequestMethod.POST])
    fun createSubmission(
            @RequestParam token: String,
            @RequestParam taskId: Int,
            @RequestParam teamId: Int?,
            @RequestPart file: MultipartFile
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.createSubmission(token, taskId, teamId, file).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/submission"], method = [RequestMethod.PUT])
    fun updateSubmission(
            @RequestParam token: String,
            @RequestParam taskId: Int,
            @RequestParam teamId: Int?,
            @RequestPart file: MultipartFile
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.updateSubmission(token, taskId, teamId, file).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/submission"], method = [RequestMethod.GET])
    fun findSubmission(
            @RequestParam token: String,
            @RequestParam taskId: Int
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.findByTokenAndTaskId(token, taskId)
}
