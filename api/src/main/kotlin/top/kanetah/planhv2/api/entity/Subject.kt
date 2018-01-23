package top.kanetah.planhv2.api.entity

import javax.persistence.Entity

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Subject(
        val subjectId: Number,
        val subjectName: String,
        val teacherName: String,
        val emailAddress: String,
        val recommendProcessorId: Number
)
