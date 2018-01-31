package top.kanetah.planhv2.api.entity

/**
 * created by kane on 2018/1/23
 */
data class SubmitFileAttributes(
        val resourceId: Int,
        val formerName: String,
        val saveName: String,
        val size: Double,
        val path: String
)
