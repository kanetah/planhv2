package top.kanetah.planhv2.api.entity

import top.kanetah.planhv2.api.annotation.Entity
import java.util.*

/**
 * created by kane on 2018/1/23
 */
@Entity
data class Team(
        val teamId: Int = Int.MIN_VALUE,
        val subjectId: Int,
        val teamIndex: Int,
        val teamName: String?,
        val memberUserIdArray: IntArray,
        val leaderUserIdArray: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Team
        
        if (teamId != other.teamId) return false
        if (subjectId != other.subjectId) return false
        if (teamIndex != other.teamIndex) return false
        if (teamName != other.teamName) return false
        if (!Arrays.equals(memberUserIdArray, other.memberUserIdArray)) return false
        if (!Arrays.equals(leaderUserIdArray, other.leaderUserIdArray)) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = teamId.hashCode()
        result = 31 * result + subjectId.hashCode()
        result = 31 * result + teamIndex.hashCode()
        result = 31 * result + (teamName?.hashCode() ?: 0)
        result = 31 * result + Arrays.hashCode(memberUserIdArray)
        result = 31 * result + Arrays.hashCode(leaderUserIdArray)
        return result
    }
}
