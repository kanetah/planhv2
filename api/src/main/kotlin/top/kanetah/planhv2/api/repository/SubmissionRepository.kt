package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Submission

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface SubmissionRepository {
    
    fun save(submission: Submission): Int
    
    fun delete(id: Int): Int
    
    fun update(submission: Submission): Int
    
    fun findAllByUserId(userId: Int): ArrayList<Submission>?
    
    fun findByTokenAndTaskId(token: String, taskId: Int): Submission?
}
