package top.kanetah.planhv2.backend.format

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.APP_CONTEXT
import top.kanetah.planhv2.backend.entity.SubmitFileAttributes
import top.kanetah.planhv2.backend.entity.Task
import top.kanetah.planhv2.backend.entity.Team
import top.kanetah.planhv2.backend.entity.User
import top.kanetah.planhv2.backend.format.FormatProcessorClass.FormatValue.*
import top.kanetah.planhv2.backend.format.processor.OutsideFileNameFormatProcessor
import top.kanetah.planhv2.backend.property.PropertyListener
import top.kanetah.planhv2.backend.repository.SubjectRepository
import top.kanetah.planhv2.backend.service.RepositoryService
import java.text.SimpleDateFormat


/**
 * created by kane on 2018/1/31
 */
interface FormatProcessorClass {
    val id: Int
    val storePath: String
        get() = PropertyListener.getProperty("submission-path")!!

    fun saveFile(user: User, task: Task, team: Team?, file: MultipartFile): SubmitFileAttributes

    private enum class FormatValue(private val key: String) {
        Code("code"),
        Code2("code2"),
        Code3("code3"),
        Name("name"),
        Title("title"),
        Subject("subject"),
        Index("index"),
        TeamName("teamname"),
        Original("original"),
        Date("date");

        companion object {
            operator fun get(key: String?) = values().firstOrNull { it.key == key }
        }
    }

    fun getFormatName(
            user: User, task: Task, team: Team?, file: MultipartFile
    ) = if (task.format.isNullOrEmpty()) throw Exception("任务指定的格式化处理器不合适")
    else task.format.let { format ->
        fun replace(
                source: String
        ): String = Regex("\\[[\\w]*]").find(source).let { result ->
            if (result == null || result.value.isEmpty()) source
            else replace(source.replace(
                    result.value, when (FormatValue[Regex("[\\w]+").find(result.value)!!.value]
                ) {
                Code -> user.userCode
                Code2 -> user.userCode.let { it.substring(it.length - 2) }
                Code3 -> user.userCode.let { it.substring(it.length - 3) }
                Name -> user.userName
                Title -> task.title
                Subject -> subjectRepository.find(task.subjectId)!!.subjectName
                Index -> team?.teamIndex.toString()
                TeamName -> team?.teamName.toString()
                Original -> file.originalFilename
                Date -> SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
                null -> result.value
            }))
        }
        replace(format)
    }

    companion object {
        private val repositoryService by lazy {
            APP_CONTEXT.getBean(RepositoryService::class.java)!!
        }
        private val subjectRepository by lazy {
            APP_CONTEXT.getBean(SubjectRepository::class.java)!!
        }

        operator fun get(taskId: Int): FormatProcessorClass {
            val processorId = repositoryService.taskRepository.find(taskId)?.formatProcessorId
                    ?: throw Exception("Task: $taskId not found.")
            println("应使用 FormatProcessorClass: $processorId 处理，" +
                    "暂用OutsideFileNameFormatProcessor代替。")
            return OutsideFileNameFormatProcessor
        }
    }
}

infix fun MultipartFile.typeBy(
        task: Task
) = originalFilename.let { it.substring(it.lastIndexOf(".")) }.let {
    if (task.type.contains(it)) it else throw Exception("非法文件类型")
}
