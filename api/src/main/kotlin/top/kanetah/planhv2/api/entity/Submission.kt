package top.kanetah.planhv2.api.entity

import java.util.*
import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Submission(
        val submissionId: Number,
        val taskId: Number,
        val userId: Number,
        val teamId: Number?,
        val submitData: Date,
        val fileAttributes: SubmitFileAttributes
)
