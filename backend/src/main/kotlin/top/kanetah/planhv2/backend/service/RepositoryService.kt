package top.kanetah.planhv2.backend.service

import top.kanetah.planhv2.backend.repository.*

/**
 * created by kane on 2018/1/25
 */
interface RepositoryService {
    val adminRepository: AdminRepository
    val authRepository: AuthRepository
    val userRepository: UserRepository
    val tokenRepository: TokenRepository
    val teamRepository: TeamRepository
    val subjectRepository: SubjectRepository
    val taskRepository: TaskRepository
    val submissionRepository: SubmissionRepository
    val resourceRepository: ResourceRepository
}
