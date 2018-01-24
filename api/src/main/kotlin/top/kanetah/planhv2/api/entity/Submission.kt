package top.kanetah.planhv2.api.entity

import java.util.*

/**
 * created by kane on 2018/1/23
 */
data class Submission(
        val submissionId: Int,
        val taskId: Int,
        val userId: Int,
        val teamId: Int?,
        val submitData: Date,
        val fileAttributes: SubmitFileAttributes
)
