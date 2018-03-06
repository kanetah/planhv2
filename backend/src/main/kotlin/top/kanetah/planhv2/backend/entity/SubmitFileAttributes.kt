package top.kanetah.planhv2.backend.entity

import top.kanetah.planhv2.backend.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class SubmitFileAttributes(
        val resourceId: Int,
        val formerName: String,
        val saveName: String,
        val size: Double,
        val path: String
)
