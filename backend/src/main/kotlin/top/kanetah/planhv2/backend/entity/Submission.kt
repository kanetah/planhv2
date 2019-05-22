package top.kanetah.planhv2.backend.entity

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.annotation.Entity
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
        val submitDate: Timestamp,
        val fileAttributes: SubmitFileAttributes
) {
    constructor(
            taskId: Int, userId: Int, teamId: Int?, fileAttributes: SubmitFileAttributes
    ) : this(taskId = taskId, userId = userId, teamId = teamId,
            submitDate = Timestamp(Date().time), fileAttributes = fileAttributes)

    fun getResourceId() = fileAttributes.resourceId
    fun getFormerName() = fileAttributes.formerName
    fun getSaveName() = fileAttributes.saveName
    fun getSize() = fileAttributes.size
    fun getPath() = fileAttributes.path
}
