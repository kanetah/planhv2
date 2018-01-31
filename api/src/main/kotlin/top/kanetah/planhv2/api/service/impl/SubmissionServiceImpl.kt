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
    
    override fun createSubmission(
            token: String, taskId: Int, teamId: Int?, file: MultipartFile
    ) = repositoryService.userRepository.findByToken(token)?.let {
        repositoryService.submissionRepository.save(Submission(
                taskId,
                it.userId,
                teamId,
                FormatProcessorClass[taskId].saveFile(it, repositoryService.taskRepository.find(taskId)!!, file)
        )) > 0
    } ?: false
    
    override fun deleteSubmission(token: String, taskId: Int): Boolean {
        TODO("not implemented")
    }
    
    override fun findByTokenAndTaskId(token: String, taskId: Int): Submission? {
        TODO("not implemented")
    }
}