package top.kanetah.planhv2.api.format.processor

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.annotation.FormatProcessorName
import top.kanetah.planhv2.api.APP_CONTEXT
import top.kanetah.planhv2.api.entity.SubmitFileAttributes
import top.kanetah.planhv2.api.entity.Task
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.format.FormatProcessorClass
import top.kanetah.planhv2.api.service.ResourceService

/**
 * created by kane on 2018/1/31
 */
@FormatProcessorName("最外层原文件名存储处理器")
object OutsideFileNameFormatProcessor:FormatProcessorClass {
    
    override val id = 1
    
    private val resourceService: ResourceService by lazy {
        APP_CONTEXT.getBean(ResourceService::class.java)!!
    }
    
    override fun saveFile(
            user: User, task: Task, file: MultipartFile
    ): SubmitFileAttributes {
        return SubmitFileAttributes(
                resourceId = resourceService.createResource(file)!!.resourceId,
                formerName = file.originalFilename,
                saveName = file.originalFilename,
                size = file.size.toDouble() / 100,
                path = savePath + file.originalFilename
        )
    }
    
    override fun sendEMail(taskId: Int): Boolean {
        TODO("not implemented")
    }
}