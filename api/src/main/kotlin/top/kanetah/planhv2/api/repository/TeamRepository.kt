package top.kanetah.planhv2.api.repository

import org.apache.ibatis.annotations.Param
import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Team

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface TeamRepository {
    
    fun save(
            @Param("subjectId")subjectId: Int,
            @Param("teamIndex")teamIndex: Int,
            @Param("teamName")teamName: String? = null,
            @Param("memberUserIdArray")memberUserIdArray: IntArray,
            @Param("leaderUserIdArray")leaderUserIdArray: IntArray
    ): Int
    
    fun findByIndex(teamIndex: Int): Team?
}
