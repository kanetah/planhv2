package top.kanetah.planhv2.backend.repository

import org.apache.ibatis.annotations.Param
import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Submission

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface SubmissionRepository {

    fun save(submission: Submission): Int

    fun delete(id: Int): Int

    fun deleteByTaskId(taskId: Int): Int

    fun update(submission: Submission): Int

    fun findAllByUserId(userId: Int): ArrayList<Submission>?

    fun findByTokenAndTaskId(@Param("token") token: String, @Param("taskId") taskId: Int): Submission?

    fun findLastByUserId(userId: Int): Submission?

    fun findByUserId(userId: Int): ArrayList<Submission>?

    fun findByTaskId(taskId: Int): ArrayList<Submission>?
}
