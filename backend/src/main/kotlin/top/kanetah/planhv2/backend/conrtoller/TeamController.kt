package top.kanetah.planhv2.backend.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.kanetah.planhv2.backend.annotation.JsonValue
import top.kanetah.planhv2.backend.annotation.PlanHApiController
import top.kanetah.planhv2.backend.service.AccessSecurityService
import top.kanetah.planhv2.backend.service.TeamService
import top.kanetah.planhv2.backend.typeHandler.toIntArray

/**
 * created by kane on 2018/1/29
 */
@PlanHApiController
class TeamController @Autowired constructor(
        private val teamService: TeamService,
        private val accessSecurityService: AccessSecurityService
) {

    @GetMapping("/teams")
    fun teams(
            @RequestHeader token: String?
    ) = teamService.getAllTeam(token)

    @PostMapping
    fun createTeam(
            @RequestBody values: Map<String, String>
    ) = teamService.takeIf { accessSecurityService.tokenCheck("${values["token"]}") }
            ?.createTeam(
                    values["subjectId"]?.toInt()!!,
                    "${values["teamName"]}",
                    values["memberUserIdArrayJsonString"]?.toIntArray()!!,
                    values["leaderUserIdArrayJsonString"]?.toIntArray()!!
            ).let {
                object {
                    @JsonValue
                    val success = it
                }
            }

    @DeleteMapping("/team/{id}")
    fun deleteTeam(
            @RequestBody values: Map<String, String>,
            @PathVariable("id") teamId: Int
    ) = teamService.takeIf { accessSecurityService.tokenCheck("${values["token"]}") }
            ?.deleteTeam("${values["token"]}", teamId).let {
                object {
                    @JsonValue
                    val success = it
                }
            }

    @PutMapping("/team/{id}")
    fun updateTeam(
            @PathVariable("id") teamId: Int,
            @RequestBody values: Map<String, String>
    ) = teamService.takeIf { accessSecurityService.tokenCheck("${values["token"]}") }
            ?.updateTeam(
                    "${values["token"]}",
                    teamId,
                    values["subjectId"]?.toInt()!!,
                    "${values["teamName"]}",
                    values["memberUserIdArrayJsonString"]?.toIntArray()!!,
                    values["leaderUserIdArrayJsonString"]?.toIntArray()!!
            ).let {
                object {
                    @JsonValue
                    val success = it
                }
            }

    @GetMapping("/team/{id}")
    fun findTeam(
            @RequestHeader values: Map<String, String>,
            @PathVariable("id") teamId: Int
    ) = teamService.takeIf { accessSecurityService.tokenCheck("${values["token"]}") }?.find(teamId)
}
