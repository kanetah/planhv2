package top.kanetah.planhv2.backend.format.processor

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.annotation.FormatProcessorName
import top.kanetah.planhv2.backend.APP_CONTEXT
import top.kanetah.planhv2.backend.entity.*
import top.kanetah.planhv2.backend.format.FormatProcessorClass
import top.kanetah.planhv2.backend.format.uncompress
import top.kanetah.planhv2.backend.format.typeBy
import top.kanetah.planhv2.backend.repository.SubjectRepository
import top.kanetah.planhv2.backend.service.ResourceService
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

    /**
     * 保存作业文件
     */
    override fun saveFile(
            user: User, task: Task, team: Team?, file: MultipartFile
    ): SubmitFileAttributes {
        val subjectName = subjectRepository.find(task.subjectId)!!.subjectName
        val path = File(
                "$storePath$subjectName/${task.taskId}-${task.title}"
        ).apply { if (!exists()) mkdirs() }.canonicalPath
        // 数据写入目标文件
        val target = File(
                "$path/${getFormatName(user, task, team, file)}${file typeBy task}"
        ).apply { if (!exists()) createNewFile(); file.transferTo(this); uncompress(); }
        return resourceService.createResource(
                target.name,
                file.size.toDouble(),
                "task/$subjectName/${task.title}-${task.title}/${target.name}"
        )?.let {
            SubmitFileAttributes(
                    resourceId = it.resourceId,
                    formerName = file.originalFilename,
                    saveName = target.name,
                    size = it.resourceSize,
                    path = target.canonicalPath
            )
        } ?: throw Exception("资源创建失败")
    }
}
