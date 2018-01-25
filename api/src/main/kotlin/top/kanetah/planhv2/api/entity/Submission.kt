package top.kanetah.planhv2.api.entity

import top.kanetah.planhv2.api.annotation.Entity
import java.sql.Timestamp
import java.util.*

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Submission(
        val submissionId: Int = Int.MIN_VALUE,
        val taskId: Int,
        val userId: Int,
        val teamId: Int? = null,
        val submitData: Timestamp,
        val fileAttributes: SubmitFileAttributes
) {
    constructor(submissionId: Int, taskId: Int, userId: Int, teamId: Int? = null, submitData: Timestamp,
                formerName: String, saveName: String, size: Double, path: String) : this(
            submissionId, taskId, userId, teamId, submitData,
            SubmitFileAttributes(formerName, saveName, size, path)
    )
}
