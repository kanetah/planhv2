package top.kanetah.planhv2.api.repository

import org.apache.ibatis.annotations.Param
import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Submission
import java.sql.Timestamp

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface SubmissionRepository {
    
    fun save(
            @Param("taskId") taskId: Int,
            @Param("userId") userId: Int,
            @Param("teamId") teamId: Int?,
            @Param("submitDate") submitDate: Timestamp,
            @Param("formerName") formerName: String,
            @Param("saveName") saveName: String,
            @Param("size") size: Double,
            @Param("path") path: String
    ): Int
    
    fun findByUserId(userId: Int): Array<Submission>?
}
