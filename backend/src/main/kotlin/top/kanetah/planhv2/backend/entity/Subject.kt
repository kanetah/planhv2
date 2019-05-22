package top.kanetah.planhv2.backend.entity

import top.kanetah.planhv2.backend.annotation.Entity
import java.util.*

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Subject(
        val subjectId: Int = Int.MIN_VALUE,
        val subjectName: String,
        val teacherName: String,
        val emailAddress: String,
        val teamLimit: IntArray? = null,
        val recommendProcessorId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subject

        if (subjectId != other.subjectId) return false
        if (subjectName != other.subjectName) return false
        if (teacherName != other.teacherName) return false
        if (emailAddress != other.emailAddress) return false
        if (!Arrays.equals(teamLimit, other.teamLimit)) return false
        if (recommendProcessorId != other.recommendProcessorId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = subjectId
        result = 31 * result + subjectName.hashCode()
        result = 31 * result + teacherName.hashCode()
        result = 31 * result + emailAddress.hashCode()
        result = 31 * result + Arrays.hashCode(teamLimit)
        result = 31 * result + recommendProcessorId
        return result
    }
}
