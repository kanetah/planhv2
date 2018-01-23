package top.kanetah.planhv2.api.entity

import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class SubmitFileAttributes(
        val formerName: String,
        val saveName: String,
        val size: Number,
        val path: String
)
