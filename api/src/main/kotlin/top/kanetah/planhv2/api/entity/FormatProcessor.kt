package top.kanetah.planhv2.api.entity

import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class FormatProcessor(
        val formatProcessorId: Long,
        val formatProcessorName: String,
        val formatProcessorClassName: String
)
