package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.api.annotation.JsonValue
import top.kanetah.planhv2.api.annotation.PlanHApiController
import top.kanetah.planhv2.api.service.AccessSecurityService
import top.kanetah.planhv2.api.service.TeamService
import top.kanetah.planhv2.api.typeHandler.toIntArray

/**
 * created by kane on 2018/1/29
 */
@PlanHApiController
class TeamController @Autowired constructor(
        private val teamService: TeamService,
        private val accessSecurityService: AccessSecurityService
) {
    
    @RequestMapping(value = ["/teams"], method = [RequestMethod.GET])
    fun teams(
            @RequestParam token: String?
    ) = teamService.getAllTeam(token)
    
    @RequestMapping(value = ["/team"], method = [RequestMethod.POST])
    fun createTeam(
            @RequestParam token: String,
            @RequestParam subjectId: Int,
            @RequestParam teamName: String?,
            @RequestParam memberUserIdArrayJsonString: String,
            @RequestParam leaderUserIdArrayJsonString: String
    ) = teamService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.createTeam(subjectId, teamName,
                    memberUserIdArrayJsonString.toIntArray(),
                    leaderUserIdArrayJsonString.toIntArray()).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/team/{id}"], method = [RequestMethod.DELETE])
    fun deleteTeam(
            @RequestParam token: String,
            @PathVariable("id") teamId: Int
    ) = teamService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.deleteTeam(token, teamId).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/team/{id}"], method = [RequestMethod.PUT])
    fun updateTeam(
            @RequestParam token: String,
            @PathVariable("id") teamId: Int,
            @RequestParam subjectId: Int,
            @RequestParam teamName: String?,
            @RequestParam memberUserIdArrayJsonString: String,
            @RequestParam leaderUserIdArrayJsonString: String
    ) = teamService.takeIf { accessSecurityService.tokenCheck(token) }
            ?.updateTeam(token, teamId, subjectId, teamName,
                    memberUserIdArrayJsonString.toIntArray(),
                    leaderUserIdArrayJsonString.toIntArray()).let {
        object {
            @JsonValue
            val success = it
        }
    }
    
    @RequestMapping(value = ["/team/{id}"], method = [RequestMethod.GET])
    fun findTeam(
            @RequestParam token: String,
            @PathVariable("id") teamId: Int
    ) = teamService.takeIf { accessSecurityService.tokenCheck(token) }?.find(teamId)
}
