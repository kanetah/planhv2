package top.kanetah.planhv2.backend.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.backend.entity.Team
import top.kanetah.planhv2.backend.repository.TeamRepository
import top.kanetah.planhv2.backend.service.RepositoryService
import top.kanetah.planhv2.backend.service.TeamService
import java.util.*

/**
 * created by kane on 2018/1/29
 */
@Service
class TeamServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : TeamService {

    override fun getAllTeam(
            token: String?
    ) = with(repositoryService.teamRepository) {
        if (token === null || token.isEmpty()) findAll() else findAllByUserToken(token)
    }

    override fun createTeam(
            subjectId: Int,
            teamName: String?,
            memberUserIdArray: IntArray,
            leaderUserIdArray: IntArray
    ) = with(repositoryService.teamRepository) {
        save(Team(
                subjectId = subjectId,
                teamIndex = findAllIndexBySubjectId(subjectId)
                        .sortAndGetNextMinTeamIndex(this),
                teamName = teamName,
                memberUserIdArray = memberUserIdArray,
                leaderUserIdArray = leaderUserIdArray
        )) > 0
    }

    override fun deleteTeam(
            token: String, teamId: Int
    ) = repositoryService.teamRepository.findAllByUserToken(token)?.forEach {
        if (it.teamId == teamId)
            return repositoryService.teamRepository.delete(teamId) > 0
    }.let { false }

    override fun updateTeam(
            token: String,
            teamId: Int,
            subjectId: Int,
            teamName: String?,
            memberUserIdArray: IntArray,
            leaderUserIdArray: IntArray
    ) = repositoryService.teamRepository.find(teamId).let {
        if (it !== null &&
                it.leaderUserIdArray.contains(
                        repositoryService.tokenRepository.findByToken(token)!!.userId))
            repositoryService.teamRepository.update(it.copy(
                    teamName = teamName ?: it.teamName,
                    memberUserIdArray = memberUserIdArray,
                    leaderUserIdArray = leaderUserIdArray
            )) > 0
        else false
    }

    override fun find(
            teamId: Int
    ) = repositoryService.teamRepository.find(teamId)
}

fun LinkedList<Int>?.sortAndGetNextMinTeamIndex(
        teamRepository: TeamRepository
) = 1.also {
    if (this !== null)
        (1..this.size).filter { this[it] != it + 1 }
                .forEach { key ->
                    with(teamRepository) {
                        findByIndex(this@sortAndGetNextMinTeamIndex[key])
                                ?.let { update(it.copy(teamIndex = key + 1)) }
                    }
                }
}
