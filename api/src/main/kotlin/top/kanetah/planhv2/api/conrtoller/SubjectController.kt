package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.SubjectService
import top.kanetah.planhv2.api.typeHandler.toIntArray

/**
 * created by kane on 2018/1/30
 */
@RestController
class SubjectController @Autowired constructor(
        private val subjectService: SubjectService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/subjects"], method = [RequestMethod.GET])
    fun getAllSubject() = subjectService.getAllSubject()
    
    @RequestMapping(value = ["/subject"], method = [RequestMethod.POST])
    fun createSubject(
            @RequestParam authorized: String,
            @RequestParam subjectName: String,
            @RequestParam teacherName: String,
            @RequestParam emailAddress: String,
            @RequestParam teamLimit: String?,
            @RequestParam recommendProcessorId: Int
    ) = subjectService.takeIf { accessSecurityService.authCheck(authorized) }
            ?.createSubject(
                    subjectName, teacherName, emailAddress,
                    teamLimit?.toIntArray(), recommendProcessorId
            ).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/subject/{id}"], method = [RequestMethod.DELETE])
    fun deleteSubject(
            @RequestParam authorized: String,
            @PathVariable("id") subjectId: Int
    ) = subjectService.takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = it.deleteSubject(subjectId)
        }
    }
    
    @RequestMapping(value = ["/subject/{id}"], method = [RequestMethod.PUT])
    fun updateSubject(
            @RequestParam authorized: String,
            @PathVariable subjectId: Int,
            @RequestParam subjectName: String,
            @RequestParam teacherName: String,
            @RequestParam emailAddress: String,
            @RequestParam teamLimit: String?,
            @RequestParam recommendProcessorId: Int
    ) = subjectService.takeIf { accessSecurityService.authCheck(authorized) }
            ?.updateSubject(
                    subjectId, subjectName, teacherName, emailAddress,
                    teamLimit?.toIntArray(), recommendProcessorId).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/subject/{id}"], method = [RequestMethod.GET])
    fun findSubject(
            @PathVariable("id") subjectId: Int
    ) = subjectService.findSubject(subjectId)
}
