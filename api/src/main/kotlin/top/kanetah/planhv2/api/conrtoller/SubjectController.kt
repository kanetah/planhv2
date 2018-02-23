package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.SubjectService
import top.kanetah.planhv2.api.typeHandler.toIntArray

/**
 * created by kane on 2018/1/30
 */
@PlanHApiController
class SubjectController @Autowired constructor(
        private val subjectService: SubjectService,
        private val accessSecurityService: AccessSecurityService
) {

    @GetMapping("/subjects")
    fun getAllSubject() = subjectService.getAllSubject()

    @PostMapping("/subject")
    fun createSubject(
            @RequestBody values: Map<String, String>
    ) = subjectService.takeIf { accessSecurityService.authCheck("${values["authorized"]}") }
            ?.createSubject(
                    "${values["subjectName"]}",
                    "${values["teacherName"]}",
                    "${values["emailAddress"]}",
                    values["teamLimit"]?.toIntArray(),
                    values["recommendProcessorId"]?.toInt()!!
            ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @DeleteMapping("/subject/{id}")
    fun deleteSubject(
            @RequestBody values: Map<String, String>,
            @PathVariable("id") subjectId: Int
    ) = subjectService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.deleteSubject(subjectId).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @PutMapping("/subject/{id}")
    fun updateSubject(
            @PathVariable("id") subjectId: Int,
            @RequestBody values: Map<String, String>
    ) = subjectService.takeIf {
        accessSecurityService.authCheck("${values["authorized"]}")
    }?.updateSubject(
                    subjectId,
                    "${values["subjectName"]}",
                    "${values["teacherName"]}",
                    "${values["emailAddress"]}",
                    values["teamLimit"]?.toIntArray(),
                    values["recommendProcessorId"]?.toInt()!!
            ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/subject/{id}")
    fun findSubject(
            @PathVariable("id") subjectId: Int
    ) = subjectService.findSubject(subjectId)
}
