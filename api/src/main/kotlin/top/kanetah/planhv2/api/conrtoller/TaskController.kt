package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.TaskService
import java.sql.Timestamp

/**
 * created by kane on 2018/1/30
 */
@RestController
class TaskController @Autowired constructor(
        private val taskService: TaskService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/tasks"], method = [RequestMethod.GET])
    fun tasks(
            @RequestParam userId: Int,
            @RequestParam subjectId: Int?,
            @RequestParam page: Int,
            @RequestParam limit: Int
    ) = taskService.getTasks(userId, subjectId, page, limit)
    
    @RequestMapping(value = ["/task"], method = [RequestMethod.POST])
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
        object {
            @JsonValue
            val success = it.create(
                    subjectId, title, content, isTeamTask, Timestamp.valueOf(deadline), type, format, formatProcessorId
            )
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
    ) = taskService.takeIf { accessSecurityService.authCheck(authorized) }?.let {
        object {
            @JsonValue
            val success = it.updateTask(
                    taskId, subjectId, title, content, isTeamTask, Timestamp.valueOf(deadline), type, format, formatProcessorId
            )
        }
    }
    
    @RequestMapping(value = ["/task/{id}"], method = [RequestMethod.GET])
    fun findTask(
            @PathVariable("id") taskId: Int
    ) = taskService.findTask(taskId)
}
