package top.kanetah.planhv2.api.conrtoller

import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.SubmissionService
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.annotation.JsonValue

/**
 * created by kane on 2018/1/31
 */
@RestController
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
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }?.let {
        object {
            @JsonValue
            val success = it.createSubmission(token, taskId, teamId, file)
        }
    }

    @RequestMapping(value = ["/submission"], method = [RequestMethod.DELETE])
    fun deleteSubmission(
            @RequestParam token: String,
            @RequestParam taskId: Int
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }?.let {
        object {
            @JsonValue
            val success = it.deleteSubmission(token, taskId)
        }
    }

    @RequestMapping(value = ["/submission"], method = [RequestMethod.GET])
    fun findSubmission(
            @RequestParam token: String,
            @RequestParam taskId: Int
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.findByTokenAndTaskId(token, taskId)
}
