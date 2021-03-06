package top.kanetah.planhv2.backend.conrtoller

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.ResourceService
import top.kanetah.planhv2.backend.service.SubmissionService

/**
 * created by kane on 2018/1/31
 */
@PlanHApiController
class SubmissionController(
        private val submissionService: SubmissionService,
        private val resourceService: ResourceService,
        private val accessSecurityService: AccessSecurityService
) {

    @GetMapping("/submissions")
    fun submissions(
            @RequestHeader token: String
    ) = submissionService.takeIf { accessSecurityService.tokenCheck(token) }?.findAllSubmission(token)

    @PostMapping("/submission")
    fun createSubmission(
            @RequestParam token: String,
            @RequestParam taskId: Int,
            @RequestParam(required = false) teamId: String,
            @RequestPart file: MultipartFile
    ) = submissionService.takeIf {
        accessSecurityService.tokenCheck(token)
    }?.createSubmission(token, taskId, try {
        teamId.toInt()
    } catch (e: NumberFormatException) {
        null
    }, file).let {
        object {
            @JsonValue
            val success = it
            @JsonValue
            val url = resourceService.findUrlByTokenAndTaskId(token, taskId)
        }
    }

    @PutMapping("/submission")
    fun updateSubmission(
            @RequestHeader token: String,
            @RequestHeader taskId: Int,
            @RequestHeader(required = false) teamId: String,
            @RequestPart file: MultipartFile
    ) = submissionService.takeIf {
        accessSecurityService.tokenCheck(token)
    }?.updateSubmission(token, taskId, try {
        teamId.toInt()
    } catch (e: NumberFormatException) {
        null
    }, file).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/submission/{id}")
    fun findSubmission(
            @RequestHeader token: String,
            @PathVariable("id") taskId: Int
    ) = submissionService.takeIf {
        accessSecurityService.tokenCheck(token)
    }?.findByTokenAndTaskId(token, taskId)
}
