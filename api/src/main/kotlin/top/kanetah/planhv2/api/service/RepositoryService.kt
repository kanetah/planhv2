package top.kanetah.planhv2.api.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.repository.*

/**
 * created by kane on 2018/1/25
 */
@Service
class RepositoryService @Autowired constructor(
        val adminRepository: AdminRepository,
        val userRepository: UserRepository,
        val teamRepository: TeamRepository,
        val subjectRepository: SubjectRepository,
        val taskRepository: TaskRepository,
        val submissionRepository: SubmissionRepository,
        val resourceRepository: ResourceRepository,
        val formatProcessorRepository: FormatProcessorRepository
)
