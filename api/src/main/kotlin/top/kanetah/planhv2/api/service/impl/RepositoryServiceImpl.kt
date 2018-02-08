package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.repository.*
import top.kanetah.planhv2.api.service.RepositoryService

/**
 * created by kane on 2018/1/26
 */
@Service
class RepositoryServiceImpl @Autowired constructor(
        override val adminRepository: AdminRepository,
        override val authRepository: AuthRepository,
        override val userRepository: UserRepository,
        override val tokenRepository: TokenRepository,
        override val teamRepository: TeamRepository,
        override val subjectRepository: SubjectRepository,
        override val taskRepository: TaskRepository,
        override val submissionRepository: SubmissionRepository,
        override val resourceRepository: ResourceRepository
) : RepositoryService
