package top.kanetah.planhv2.api.entity

import top.kanetah.planhv2.api.annotation.Entity
import top.kanetah.planhv2.api.format.FormatProcessorClass
import java.sql.Timestamp

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Task(
        val taskId: Int = Int.MIN_VALUE,
        val subjectId: Int,
        val title: String,
        val content: String,
        val isTeamTask: Boolean,
        val deadline: Timestamp,
        val type: String,
        val formatProcessorId: Int,
        val format: String? = null
)

fun Task.send() = FormatProcessorClass[taskId].sendEMail(this)
