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
            @RequestParam userId: Int
    ) = taskService.getAllTasks(userId)
    
//    @RequestMapping(value = ["/task"], method = [RequestMethod.GET])
    @GetMapping("/task")
    fun tasks(
            @RequestParam userId: Int,
            @RequestParam subjectId: Int?,
            @RequestParam page: Int,
            @RequestParam limit: Int
    ) = taskService.getTasks(userId, subjectId, page, limit)
    
//    @RequestMapping(value = ["/task"], method = [RequestMethod.POST])
    @PostMapping("/task")
    fun createTask(
            authorized: String,
            subjectId: Int,
            title: String,
            content: String,
            isTeamTask: Boolean,
            deadline: String,
            type: String,
            format: String?,
            formatProcessorId: Int
    ) = taskService.takeIf { accessSecurityService.authCheck(authorized) }?.let {
        it.create(
                subjectId, title, content, isTeamTask, Timestamp.valueOf(deadline),
                type, format, formatProcessorId
        ).let {
            object {
                @JsonValue
                val success = it
            }
        }
    }
    
    @RequestMapping(value = ["/task/{id}"], method = [RequestMethod.DELETE])
    fun deleteTask(
            authorized: String,
            taskId: Int
    ) = taskService.takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = it.deleteTask(taskId)
        }
    }
    
    @RequestMapping(value = ["/task/{id}"], method = [RequestMethod.PUT])
    fun updateTask(
            authorized: String,
            taskId: Int,
            subjectId: Int,
            title: String,
            content: String,
            isTeamTask: Boolean,
            deadline: String,
            type: String,
            format: String?,
            formatProcessorId: Int
    ) = taskService.takeIf { accessSecurityService.authCheck(authorized) }
            ?.updateTask(
                    taskId, subjectId, title, content, isTeamTask,
                    Timestamp.valueOf(deadline), type, format, formatProcessorId
            ).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/task/{id}"], method = [RequestMethod.GET])
    fun findTask(
            @PathVariable("id") taskId: Int
    ) = taskService.findTask(taskId)
}
