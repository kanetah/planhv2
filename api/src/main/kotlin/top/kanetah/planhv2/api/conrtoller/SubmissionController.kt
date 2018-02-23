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
    
//    @RequestMapping(value = [], method = [RequestMethod.GET])
    @GetMapping("/submissions")
    fun submissions(
            @RequestHeader token: String
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }?.findAllSubmission(token)
    
//    @RequestMapping(value = [], method = [RequestMethod.POST])
    @PostMapping("/submission")
    fun createSubmission(
            @RequestHeader token: String,
            @RequestHeader taskId: Int,
            @RequestHeader teamId: Int?,
            @RequestPart file: MultipartFile
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.createSubmission(token, taskId, teamId, file).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
//    @RequestMapping(value = [], method = [RequestMethod.PUT])
    @PutMapping("/submission")
    fun updateSubmission(
            @RequestHeader token: String,
            @RequestHeader taskId: Int,
            @RequestHeader teamId: Int?,
            @RequestPart file: MultipartFile
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.updateSubmission(token, taskId, teamId, file).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
//    @RequestMapping(value = [], method = [RequestMethod.GET])
    @GetMapping("/submission")
    fun findSubmission(
            @RequestHeader token: String,
            @RequestHeader taskId: Int
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.findByTokenAndTaskId(token, taskId)
}
