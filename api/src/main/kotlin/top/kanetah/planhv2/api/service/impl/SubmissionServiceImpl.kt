package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.Submission
import top.kanetah.planhv2.api.format.FormatProcessorClass
import top.kanetah.planhv2.api.service.RepositoryService
import top.kanetah.planhv2.api.service.SubmissionService

/**
 * created by kane on 2018/1/31
 */
@Service
class SubmissionServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : SubmissionService {

    override fun findAllSubmission(
            token: String
    ) = repositoryService.userRepository.findByToken(token)?.let {
        repositoryService.submissionRepository.findAllByUserId(it.userId)
    }

    private fun handlerUserTaskTeam(
            token: String, taskId: Int, teamId: Int?, file: MultipartFile, block: (Submission) -> Int
    ) = repositoryService.userRepository.findByToken(token)?.let { user ->
        repositoryService.taskRepository.find(taskId)?.let { task ->
            with(repositoryService.submissionRepository) {
                val id = findByTokenAndTaskId(token, taskId)?.submissionId ?: return@with
                delete(id)
            }
            block(Submission(
                    task.taskId,
                    user.userId,
                    teamId,
                    FormatProcessorClass[taskId].saveFile(user, task,
                            teamId?.let { repositoryService.teamRepository.find(it) }, file)
            ))
        }
    }.let { if (it === null) false else it > 0 }

    override fun createSubmission(
            token: String, taskId: Int, teamId: Int?, file: MultipartFile
    ) = handlerUserTaskTeam(token, taskId, teamId, file) {
        repositoryService.submissionRepository.save(it)
    }

    override fun updateSubmission(
            token: String, taskId: Int, teamId: Int?, file: MultipartFile
    ) = createSubmission(token, taskId, teamId, file)

    override fun findByTokenAndTaskId(
            token: String, taskId: Int
    ) = repositoryService.submissionRepository.findByTokenAndTaskId(token, taskId)
}
