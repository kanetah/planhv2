package top.kanetah.planhv2.api.repository

import top.kanetah.planhv2.api.annotation.DataAccess
import top.kanetah.planhv2.api.entity.Team

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface TeamRepository {
    
    fun save(team: Team): Int
    
    fun delete(id: Int)
    
    fun update(team: Team)
    
    fun findByIndex(teamIndex: Int): Team?
}
