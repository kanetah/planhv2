package top.kanetah.planhv2.api.format.processor

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.annotation.FormatProcessorName
import top.kanetah.planhv2.api.APP_CONTEXT
import top.kanetah.planhv2.api.entity.*
import top.kanetah.planhv2.api.format.FormatProcessorClass
import top.kanetah.planhv2.api.format.compact
import top.kanetah.planhv2.api.format.typeBy
import top.kanetah.planhv2.api.repository.SubjectRepository
import top.kanetah.planhv2.api.service.ResourceService
import java.io.File

/**
 * created by kane on 2018/1/31
 */
@FormatProcessorName("最外层文件名格式化处理器")
object OutsideFileNameFormatProcessor : FormatProcessorClass {
    
    override val id = 1
    
    private val resourceService by lazy {
        APP_CONTEXT.getBean(ResourceService::class.java)!!
    }
    private val subjectRepository by lazy {
        APP_CONTEXT.getBean(SubjectRepository::class.java)!!
    }
    
    override fun saveFile(
            user: User, task: Task, team: Team?, file: MultipartFile
    ): SubmitFileAttributes {
        val path = with(subjectRepository) {
            File(
                    "$storePath${find(task.subjectId)!!.subjectName}/${task.title}"
            ).apply { if (!exists()) mkdirs() }.canonicalPath
        }
        val target = File(
                "$path/${getFormatName(user, task, team)}${file typeBy task}"
        ).apply { if (!exists()) createNewFile(); file.transferTo(this); compact(); }
        return resourceService.createResource(
                target.name, target.length().toDouble(), "task/${target.name}"
        )?.let {
            SubmitFileAttributes(
                    resourceId = it.resourceId,
                    formerName = file.originalFilename,
                    saveName = target.name,
                    size = it.resourceSize,
                    path = target.canonicalPath
            )
        } ?: throw Exception("资源创建失败，无法下载、查看")
    }
    
    override fun sendEMail(taskId: Int): Boolean {
        //TODO("not implemented")
        return false
    }
}
