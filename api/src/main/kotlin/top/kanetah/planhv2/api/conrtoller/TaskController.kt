package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.TaskService
import java.sql.Timestamp

/**
 * created by kane on 2018/1/30
 */
@PlanHApiController
class TaskController @Autowired constructor(
        private val taskService: TaskService,
        private val accessSecurityService: AccessSecurityService
) {

    @GetMapping("/tasks")
    fun allTasks(
            @RequestHeader userId: Int
    ) = taskService.getAllTasks(userId)

    @GetMapping("/task")
    fun tasks(
            @RequestHeader userId: Int,
            @RequestHeader subjectId: String,
            @RequestHeader page: Int,
            @RequestHeader limit: Int
    ) = taskService.getTasks(userId, try {
        subjectId.toInt()
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
                    "${values["format"]}",
                    values["formatProcessorId"]?.toInt()!!
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
}
