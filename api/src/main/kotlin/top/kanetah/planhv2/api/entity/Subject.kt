package top.kanetah.planhv2.api.entity

import top.kanetah.planhv2.api.annotation.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Subject(
        val subjectId: Int = Int.MIN_VALUE,
        val subjectName: String,
        val teacherName: String,
        val emailAddress: String,
        val recommendProcessorId: Int
)
