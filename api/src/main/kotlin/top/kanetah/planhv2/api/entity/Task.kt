package top.kanetah.planhv2.api.entity

import java.util.*

/**
 * created by kane on 2018/1/23
 */
data class Task(
        val taskId: Int,
        val subjectId: Int,
        val title: String,
        val content: String,
        val isTeamTask: Boolean,
        val deadline: Date,
        val type: String,
        val formatProcessorId: Int,
        val format: String?
)