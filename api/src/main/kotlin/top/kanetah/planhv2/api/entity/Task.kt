package top.kanetah.planhv2.api.entity

import java.util.*
import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Task(
        val taskId: Number,
        val subjectId: Number,
        val title: String,
        val content: String,
        val isTeamTask: Boolean,
        val deadline: Date,
        val type: String,
        val formatProcessorId: Number,
        val format: String?
)