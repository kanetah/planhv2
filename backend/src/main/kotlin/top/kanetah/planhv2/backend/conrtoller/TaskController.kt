package top.kanetah.planhv2.backend.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.SubmissionService
import top.kanetah.planhv2.backend.service.TaskService
import java.sql.Timestamp

/**
 * created by kane on 2018/1/30
 */
@PlanHApiController
class TaskController @Autowired constructor(
        private val taskService: TaskService,
        private val accessSecurityService: AccessSecurityService,
        private val submissionService: SubmissionService
) {

    @GetMapping("/tasks")
    fun allTasks(
            @RequestHeader(required = false) userId: Int?
    ) = taskService.getAllTasks(userId)

    @GetMapping("/task")
    fun tasks(
            @RequestHeader(required = false) userId: Int?,
            @RequestHeader(required = false) subjectId: String?,
            @RequestHeader page: Int,
            @RequestHeader limit: Int
    ) = taskService.getTasks(userId, try {
        subjectId?.toInt()
    } catch (e: NumberFormatException) {
        null
    }, page, limit)

    @PostMapping("/task")
    fun createTask(
            @RequestBody values: Map<String, String>
    ) = taskService.takeIf {
        accessSecurityService.authCheck(values["authorized"])
    }?.create(
            values["subjectId"]?.toInt()!!,
            "${values["title"]}",
            "${values["content"]}",
            values["isTeamTask"]?.toBoolean()!!,
            Timestamp.valueOf("${values["deadline"]}"),
            "${values["type"]}",
            "${values["format"]}"
    ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @DeleteMapping("/task/{id}")
    fun deleteTask(
            @RequestBody values: Map<String, String>,
            @PathVariable("id") taskId: Int
    ) = taskService.takeIf {
        accessSecurityService.authCheck(values["authorized"])
    }?.deleteTask(taskId).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @PutMapping("/task/{id}")
    fun updateTask(
            @PathVariable("id") taskId: Int,
            @RequestBody values: Map<String, String>
    ) = taskService.takeIf {
        accessSecurityService.authCheck(values["authorized"])
    }?.updateTask(
            taskId,
            values["subjectId"]?.toInt()!!,
            "${values["title"]}",
            "${values["content"]}",
            values["isTeamTask"]?.toBoolean()!!,
            Timestamp.valueOf("${values["deadline"]}"),
            "${values["type"]}",
            "${values["format"]}",
            values["formatProcessorId"]?.toInt()!!
    ).let {
        object {
            @JsonValue
            val success = it
        }
    }

    @GetMapping("/task/{id}")
    fun findTask(
            @PathVariable("id") taskId: Int
    ) = taskService.findTask(taskId)

    @GetMapping("/task/submission/{id}")
    fun findSubmissionFroAdmin(
            @RequestHeader authorized: String,
            @PathVariable("id") taskId: Int
    ) = submissionService.takeIf {
        accessSecurityService.authCheck(authorized)
    }?.findByTaskId(taskId)

    @PostMapping("/send/{id}")
    fun sendTaskEmail(
            @RequestBody values: Map<String, String>,
            @PathVariable("id") taskId: Int
    ) = taskService.takeIf {
        accessSecurityService.authCheck(values["authorized"])
    }?.sendTask(taskId).let {
        object {
            @JsonValue
            val success = true
        }
    }
}
