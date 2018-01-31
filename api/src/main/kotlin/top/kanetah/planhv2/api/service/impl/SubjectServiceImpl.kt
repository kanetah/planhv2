package top.kanetah.planhv2.api.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.kanetah.planhv2.api.entity.Subject
import top.kanetah.planhv2.api.service.RepositoryService
import top.kanetah.planhv2.api.service.SubjectService

/**
 * created by kane on 2018/1/30
 */
@Service
class SubjectServiceImpl @Autowired constructor(
        private val repositoryService: RepositoryService
) : SubjectService {
    
    override fun getAllSubject(
    ) = repositoryService.subjectRepository.findAll()
    
    override fun createSubject(
            subjectName: String,
            teacherName: String,
            emailAddress: String,
            teamLimit: IntArray?,
            recommendProcessorId: Int
    ) = repositoryService.subjectRepository.save(Subject(
            subjectName = subjectName,
            teacherName = teacherName,
            emailAddress = emailAddress,
            teamLimit = teamLimit,
            recommendProcessorId = recommendProcessorId
    )) > 0
    
    override fun deleteSubject(
            id: Int
    ) = repositoryService.subjectRepository.delete(id) > 0
    
    override fun updateSubject(
            id: Int,
            subjectName: String,
            teacherName: String,
            emailAddress: String,
            teamLimit: IntArray?,
            recommendProcessorId: Int
    ) = with(repositoryService.subjectRepository) {
        find(id)?.let {
            update(it.copy(
                    subjectName = subjectName,
                    teacherName = teacherName,
                    emailAddress = emailAddress,
                    teamLimit = teamLimit,
                    recommendProcessorId = recommendProcessorId
            )) > 0
        }
    } ?: false
    
    override fun findSubject(
            id: Int
    ) = repositoryService.subjectRepository.find(id)
}
