package top.kanetah.planhv2.backend.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.entity.Submission
import top.kanetah.planhv2.backend.format.FormatProcessorClass
import top.kanetah.planhv2.backend.service.RepositoryService
import top.kanetah.planhv2.backend.service.ResourceService
import top.kanetah.planhv2.backend.service.SubmissionService

/**
 * created by kane on 2018/1/31
 */
@Service
class SubmissionServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService,
        private val resourceService: ResourceService
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
            findByTokenAndTaskId(token, taskId)?.apply {
                repositoryService.submissionRepository.delete(submissionId)
                resourceService.deleteResource(getResourceId())
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

//    fun deleteSubmission(
//            token: String, taskId: Int
//    ) = findByTokenAndTaskId(token, taskId)?.let {
//        repositoryService.submissionRepository.delete(it.submissionId) > 0 &&
//                repositoryService.resourceRepository.delete(it.getResourceId()) > 0
//    }

    override fun findByTokenAndTaskId(
            token: String, taskId: Int
    ) = repositoryService.submissionRepository.findByTokenAndTaskId(token, taskId)

    override fun findByUserId(
            userId: Int
    ) = repositoryService.submissionRepository.findByUserId(userId)
}
