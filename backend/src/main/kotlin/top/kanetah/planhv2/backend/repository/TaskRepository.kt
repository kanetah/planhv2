package top.kanetah.planhv2.backend.repository

import org.apache.ibatis.annotations.Param
import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Task

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface TaskRepository {

    fun save(task: Task): Int

    fun delete(id: Int): Int

    fun update(task: Task): Int

    fun find(id: Int): Task?

    fun findByTitleLike(title: String): Array<Task>?

    fun taskList(
            @Param("subjectId") subjectId: Int?,
            @Param("start") start: Int,
            @Param("size") size: Int
    ): ArrayList<Task>?

    fun unsubmitted(
            @Param("userId") userId: Int
    ): ArrayList<Task>?

    fun allTasks(): ArrayList<Task>?
}
