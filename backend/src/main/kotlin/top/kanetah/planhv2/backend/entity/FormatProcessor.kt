package top.kanetah.planhv2.backend.entity

import top.kanetah.planhv2.backend.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class FormatProcessor(
        val formatProcessorId: Int = Int.MIN_VALUE,
        val formatProcessorName: String,
        val formatProcessorClassName: String
)
