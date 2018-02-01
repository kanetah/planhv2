package top.kanetah.planhv2.api.format

import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.SubmitFileAttributes
import top.kanetah.planhv2.api.format.processor.OutsideFileNameFormatProcessor
import top.kanetah.planhv2.api.APP_CONTEXT
import top.kanetah.planhv2.api.entity.Task
import top.kanetah.planhv2.api.entity.Team
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.format.FormatProcessorClass.FormatValue.*
import top.kanetah.planhv2.api.property.PropertyListener
import top.kanetah.planhv2.api.service.RepositoryService
import java.io.File

/**
 * created by kane on 2018/1/31
 */
interface FormatProcessorClass {
    val id: Int
    val storePath: String
        get() = PropertyListener.getProperty("submission-path")!!
    
    fun saveFile(user: User, task: Task, team: Team?, file: MultipartFile): SubmitFileAttributes
    fun sendEMail(taskId: Int): Boolean
    
    private enum class FormatValue(val key: String) {
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
            operator fun get(key: String?) = key?.run {
                values().filter { it.key === key }[0]
            }
        }
    }
    
    fun getFormatName(user: User, task: Task, team: Team?): String {
        if (task.format.isNullOrEmpty()) throw Exception("任务指定的格式化处理器不合适")
        val name = StringBuilder(task.format)
        val formatRegex = Regex("#\\{[\\w]*}")
        val valueNameRegex = Regex("[\\w]+")
        formatRegex.findAll(task.format!!).forEach {
            Regex(it.value).replace(name, when (FormatValue[valueNameRegex.find(it.value)?.value]) {
                Code -> ""
                Code2 -> TODO()
                Code3 -> TODO()
                Name -> TODO()
                Title -> TODO()
                Subject -> TODO()
                Index -> TODO()
                TeamName -> TODO()
                Original -> TODO()
                Date -> TODO()
                null -> TODO()
            })
        }
        return ""
    }
    
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

infix fun MultipartFile.typeBy(
        task: Task
) = originalFilename.let { it.substring(it.lastIndexOf(".")) }.let {
    if (task.type.contains(it)) it else throw Exception("非法文件类型")
}

fun File.compact() {
}

fun main(args: Array<String>) {
    val formatRegex = Regex("#\\{[\\w]*}")
    formatRegex.findAll("asfasdsa#{nico}  #{poi}-#{duang2}").forEach {
        println(Regex("[\\w]+").find(it.value)?.value)
    }
}
