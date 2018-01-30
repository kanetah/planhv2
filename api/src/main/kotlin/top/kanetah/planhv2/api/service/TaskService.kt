package top.kanetah.planhv2.api.service

import top.kanetah.planhv2.api.entity.Task
import java.sql.Timestamp

/**
 * created by kane on 2018/1/30
 */
interface TaskService {
    fun getTasks(userId: Int, subjectId: Int?, page: Int, limit: Int): ArrayList<Task>?
    fun create(subjectId: Int, title: String, content: String, isTeamTask: Boolean, deadline: Timestamp, type: String, format: String?, formatProcessorId: Int): Boolean
    fun deleteTask(id: Int): Boolean
    fun updateTask(id: Int, subjectId: Int, title: String, content: String, isTeamTask: Boolean, deadline: Timestamp, type: String, format: String?, formatProcessorId: Int): Boolean
    fun findTask(id: Int): Task?
}
