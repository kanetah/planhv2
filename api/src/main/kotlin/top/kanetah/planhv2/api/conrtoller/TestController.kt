package top.kanetah.planhv2.api.conrtoller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.kanetah.planhv2.api.entity.*
import top.kanetah.planhv2.api.service.RepositoryService
import java.sql.Timestamp
import java.util.*

/**
 * created by kane on 2018/1/23
 */
@RestController
@RequestMapping("/test")
class TestController @Autowired constructor(
        private val repositoryService: RepositoryService
) {
    
    @RequestMapping("/poi")
    fun poi() = "poi"
    
    @RequestMapping("/admin")
    fun admin(): Admin? {
        return repositoryService.adminRepository.findByPassword("poi")
    }
    
    @RequestMapping("/user")
    fun user(): User? {
        return repositoryService.userRepository.findByCode("1521192213")
    }
    
    @RequestMapping("/team")
    fun team(): Team? {
        repositoryService.taskRepository.save(
                Task(subjectId = 1, title = "poi", content = "nico", deadline = Timestamp(Date().time),
                        type = ".htm", formatProcessorId = 1)
        )
        return repositoryService.teamRepository.findByIndex(6)
    }
    
    @RequestMapping("/subject")
    fun subject(): Subject? {
        return repositoryService.subjectRepository.findBySubjectName("测试")
    }
    
    @RequestMapping("/task")
    fun task(): Array<Task>? {
        repositoryService.taskRepository.delete(2)
        return repositoryService.taskRepository.findByTitleLike("测")
    }
    
    @RequestMapping("/resource")
    fun resource(): Array<Resource>? {
        return repositoryService.resourceRepository.findByNameLike("测")
    }
    
    @RequestMapping("format")
    fun format(): FormatProcessor? {
        return repositoryService.formatProcessorRepository.findByName("Test")
    }
    
    @RequestMapping("/submission")
    fun submission(): Array<Submission>? {
        return repositoryService.submissionRepository.findByUserId(1)
    }
}
