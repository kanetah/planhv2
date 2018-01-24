package top.kanetah.planhv2.api.entity

/**
 * created by kane on 2018/1/23
 */
data class Subject(
        val subjectId: Int,
        val subjectName: String,
        val teacherName: String,
        val emailAddress: String,
        val recommendProcessorId: Int
)
