package top.kanetah.planhv2.api.service

import top.kanetah.planhv2.api.entity.Team

/**
 * created by kane on 2018/1/29
 */
interface TeamService {
    fun getAllTeam(token: String?): ArrayList<Team>?
    fun createTeam(subjectId: Int, teamName: String?, memberUserIdArray: IntArray, leaderUserIdArray: IntArray): Boolean
    fun deleteTeam(token: String, teamId: Int): Boolean
    fun updateTeam(token: String, teamId: Int, subjectId: Int, teamName: String?, memberUserIdArray: IntArray, leaderUserIdArray: IntArray): Boolean
    fun find(teamId: Int): Team?
}
