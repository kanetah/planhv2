package top.kanetah.planhv2.backend.repository

import top.kanetah.planhv2.backend.annotation.DataAccess
import top.kanetah.planhv2.backend.entity.Team
import java.util.*

/**
 * created by kane on 2018/1/24
 */
@DataAccess
interface TeamRepository {

    fun save(team: Team): Int

    fun delete(id: Int): Int

    fun update(team: Team): Int

    fun findAll(): ArrayList<Team>?

    fun find(id: Int): Team?

    fun findByIndex(teamIndex: Int): Team?

    fun findAllByUserToken(token: String): ArrayList<Team>?

    fun findAllIndexBySubjectId(subjectId: Int): LinkedList<Int>?
}
