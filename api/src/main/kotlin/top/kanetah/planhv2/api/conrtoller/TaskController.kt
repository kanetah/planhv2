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

    //    @RequestMapping(value = ["/task"], method = [RequestMethod.GET])
    @GetMapping("/task")
    fun tasks(
            @RequestHeader userId: Int,
            @RequestHeader subjectId: Int?,
            @RequestHeader page: Int,
            @RequestHeader limit: Int
    ) = taskService.getTasks(userId, subjectId, page, limit)

    //    @RequestMapping(value = ["/task"], method = [RequestMethod.POST])
    @PostMapping("/task")
    fun createTask(
//            authorized: String,
//            subjectId: Int,
//            title: String,
//            content: String,
//            isTeamTask: Boolean,
//            deadline: String,
//            type: String,
//            format: String?,
//            formatProcessorId: Int
            @RequestBody values: Map<String, String>
    ) = taskService.takeIf {
        accessSecurityService.authCheck(values["authorized"])
    }?.let {
                it.create(
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
            }

    //    @RequestMapping(value = [], method = [RequestMethod.DELETE])
    @DeleteMapping("/task/{id}")
    fun deleteTask(
//            authorized: String,
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

    //    @RequestMapping(value = [""], method = [RequestMethod.PUT])
    @PutMapping("/task/{id}")
    fun updateTask(
//            authorized: String,
            @PathVariable("id") taskId: Int,
//            subjectId: Int,
//            title: String,
//            content: String,
//            isTeamTask: Boolean,
//            deadline: String,
//            type: String,
//            format: String?,
//            formatProcessorId: Int
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

//    @RequestMapping(value = [], method = [RequestMethod.GET])
    @GetMapping("/task/{id}")
    fun findTask(
            @PathVariable("id") taskId: Int
    ) = taskService.findTask(taskId)
}
