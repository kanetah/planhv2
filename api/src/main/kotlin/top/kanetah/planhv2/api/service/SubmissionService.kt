package top.kanetah.planhv2.api.service

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.Submission

/**
 * created by kane on 2018/1/31
 */
interface SubmissionService {
    fun findAllSubmission(token: String): ArrayList<Submission>?
    fun createSubmission(token: String, taskId: Int, teamId: Int?, file: MultipartFile): Boolean
    fun deleteSubmission(token: String, taskId: Int): Boolean
    fun findByTokenAndTaskId(token: String, taskId: Int): Submission?
}