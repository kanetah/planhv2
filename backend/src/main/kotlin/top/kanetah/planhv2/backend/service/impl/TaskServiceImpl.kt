package top.kanetah.planhv2.backend.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.backend.entity.Task
import top.kanetah.planhv2.backend.service.RepositoryService
import top.kanetah.planhv2.backend.service.TaskService
import java.sql.Timestamp

/**
 * created by kane on 2018/1/30
 */
@Service
class TaskServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : TaskService {

    override fun getTasks(
            userId: Int, subjectId: Int?, page: Int, limit: Int
    ) = with(repositoryService.taskRepository) {
        val allTask = taskList(subjectId, (page - 1) * limit, limit) ?: return@with null
        val unsubmitted = unsubmitted(userId) ?: return@with allTask
        allTask.removeAll(unsubmitted)
        unsubmitted.addAll(allTask)
        unsubmitted
    }

    override fun getAllTasks(
            userId: Int
    ) = with(repositoryService.taskRepository) {
        val allTask = allTasks() ?: return@with null
        val unsubmitted = unsubmitted(userId) ?: return@with allTask
        allTask.removeAll(unsubmitted)
        unsubmitted.addAll(allTask)
        unsubmitted
    }

    override fun create(
            subjectId: Int,
            title: String,
            content: String,
            isTeamTask: Boolean,
            deadline: Timestamp,
            type: String,
            format: String?,
            formatProcessorId: Int
    ) = repositoryService.taskRepository.save(Task(
            subjectId = subjectId,
            title = title,
            content = content,
            isTeamTask = isTeamTask,
            deadline = deadline,
            type = type,
            format = format,
            formatProcessorId = formatProcessorId
    )) > 0

    override fun deleteTask(
            id: Int
    ) = repositoryService.taskRepository.delete(id) > 0

    override fun updateTask(
            id: Int,
            subjectId: Int,
            title: String,
            content: String,
            isTeamTask: Boolean,
            deadline: Timestamp,
            type: String,
            format: String?,
            formatProcessorId: Int
    ) = with(repositoryService.taskRepository) {
        find(id)?.let {
            update(it.copy(
                    subjectId = subjectId,
                    title = title,
                    content = content,
                    isTeamTask = isTeamTask,
                    deadline = deadline,
                    type = type,
                    format = format,
                    formatProcessorId = formatProcessorId
            )) > 0
        }
    } ?: false

    override fun findTask(
            id: Int
    ) = repositoryService.taskRepository.find(id)
}
