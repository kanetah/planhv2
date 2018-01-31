package top.kanetah.planhv2.api.format

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.SubmitFileAttributes
import top.kanetah.planhv2.api.format.processor.OutsideFileNameFormatProcessor
import top.kanetah.planhv2.api.APP_CONTEXT
import top.kanetah.planhv2.api.entity.Task
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.property.PropertyListener
import top.kanetah.planhv2.api.service.RepositoryService


/**
 * created by kane on 2018/1/31
 */
interface FormatProcessorClass {
    val id: Int
    val savePath: String
        get() = PropertyListener.getProperty("submission-path")!!
    
    fun saveFile(user: User, task: Task, file: MultipartFile): SubmitFileAttributes
    fun sendEMail(taskId: Int): Boolean
    
    companion object {
        private val repositoryService: RepositoryService by lazy {
            APP_CONTEXT.getBean(RepositoryService::class.java)
        }
        
        operator fun get(taskId: Int): FormatProcessorClass {
            val processorId = repositoryService.taskRepository.find(taskId)?.formatProcessorId
                    ?: throw Exception("Task: $taskId not found.")
            println("应使用 FormatProcessorClass: $processorId 处理，暂用OutsideFileNameFormatProcessor代替。")
            return OutsideFileNameFormatProcessor
        }
    }
}
