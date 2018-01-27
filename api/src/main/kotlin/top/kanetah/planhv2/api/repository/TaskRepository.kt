package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Task

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface TaskRepository {
    
    fun save(task: Task): Int
    
    fun delete(id: Int)
    
    fun update(task: Task)
    
    fun findByTitleLike(title: String): Array<Task>?
}