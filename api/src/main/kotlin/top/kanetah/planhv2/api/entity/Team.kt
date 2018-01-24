package top.kanetah.planhv2.api.entity

import java.util.*

/**
 * created by kane on 2018/1/23
 */
data class Team(
        val teamId: Int,
        val subjectId: Int,
        val teamIndex: Int,
        val teamName: String?,
        val memberUserIdArray: IntArray,
        val leaderUserIdArray: IntArray
) {
    constructor(teamId: Int, subjectId: Int, teamIndex: Int, teamName: String?,
                memberUserIdArray: String, leaderUserIdArray: String
    ) : this(teamId, subjectId, teamIndex, teamName,
            memberUserIdArray.toIntArray(), leaderUserIdArray.toIntArray())
    
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

fun String.toIntArray(): IntArray {
    val stringArray = this.slice(IntRange(1, this.length - 2)).split(", ")
    val intArray = IntArray(stringArray.size)
    for ((index, value) in stringArray.withIndex())
        intArray[index] = value.toInt()
    print(intArray.contentToString())
    return intArray
}
