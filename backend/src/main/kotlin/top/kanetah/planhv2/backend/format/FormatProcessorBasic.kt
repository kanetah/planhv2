package top.kanetah.planhv2.backend.format

import com.github.junrar.Archive
import com.github.junrar.rarfile.FileHeader
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.backend.entity.SubmitFileAttributes
import top.kanetah.planhv2.backend.format.processor.OutsideFileNameFormatProcessor
import top.kanetah.planhv2.backend.APP_CONTEXT
import top.kanetah.planhv2.backend.entity.Task
import top.kanetah.planhv2.backend.entity.Team
import top.kanetah.planhv2.backend.entity.User
import top.kanetah.planhv2.backend.format.FormatProcessorClass.FormatValue.*
import top.kanetah.planhv2.backend.property.PropertyListener
import top.kanetah.planhv2.backend.repository.SubjectRepository
import top.kanetah.planhv2.backend.service.RepositoryService
import java.io.*
import java.text.SimpleDateFormat
import java.io.FileOutputStream
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.nio.charset.Charset
import java.util.zip.ZipFile


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

fun File.deleteAll(): Boolean {
    listFiles()?.forEach {
        if (!it.deleteAll())
            return false
    }
    return delete()
}

    /**
     * 解压缩文件
     */
    fun File.uncompress() {
        // 使用懒加载，只有在确定需要时才创建新的作业存放目录
        val descPath by lazy {
            with(canonicalPath) {
                (substring(0, lastIndexOf(".")) + File.separator).also {
                    File(it).apply { if (!exists()) mkdirs() }
                }
            }
        }
        when (name.substring(name.lastIndexOf("."))) {
            ".rar" -> unRar(this, descPath)
            ".zip" -> unZip(this, descPath)
            ".7z" -> un7z(this, descPath)
            else -> return
        }
        File(descPath).listFiles()?.apply {
            get(0)?.let { dir ->
                if (size == 1 && dir.isDirectory) {
                    dir.listFiles()?.forEach {
                        it.renameTo(File(descPath + "/" + it.name))
                        it.deleteAll()
                    }
                    dir.delete()
                }
            }
        }
        delete()
    }

private fun unRar(file: File, descPath: String): String {
    val archive = Archive(file)
    var fh: FileHeader?
    while (null != archive.nextFileHeader().also { fh = it }) {
        val fileName = if (fh!!.fileNameW.isEmpty()) fh!!.fileNameString else fh!!.fileNameW
        if (fh!!.isDirectory)
            File(descPath + fileName).mkdirs()
        else {
            val out = File(descPath + fileName.trim({ it <= ' ' })).apply {
                parentFile.apply { if (!exists()) mkdirs() }
                if (!exists()) createNewFile()
            }
            val os = FileOutputStream(out)
            archive.extractFile(fh, os)
            os.close()
        }
    }
    archive.close()
    return descPath
}

private fun unZip(file: File, descPath: String, charSet: String = "utf-8") {
    val zip = ZipFile(file, Charset.forName(charSet))
    try {
        File(descPath).apply { if (!exists()) mkdirs() }
        val entries = zip.entries()
        entries.iterator().forEach {
            val zipEntryName = it.name
            val outPath = (descPath + zipEntryName)
                    .replace("\\*".toRegex(), "/")
            File(outPath.substring(0, outPath.lastIndexOf('/'))).apply { if (!exists()) mkdirs() }
            if (File(outPath).isDirectory) return@forEach
            val out = FileOutputStream(outPath)
            val buff = ByteArray(1024)
            zip.getInputStream(it).apply {
                var len: Int
                while (0 < read(buff).also { len = it })
                    out.write(buff, 0, len)
                close()
            }
            out.close()
        }
    } catch (e: Exception) {
        if (charSet == "utf-8")
            unZip(file, descPath, "GBK")
    } finally {
        zip.close()
    }
}

private fun un7z(file: File, descPath: String) {
    val sevenZFile = SevenZFile(file)
    var entry: SevenZArchiveEntry? = sevenZFile.nextEntry
    while (entry != null) {
        if (entry.isDirectory) {
            File((descPath + entry.name)).mkdirs()
            entry = sevenZFile.nextEntry
            continue
        } else File((descPath + entry.name)).also {
            if (!it.parentFile.exists()) it.parentFile.mkdirs()
        }.createNewFile()
        val out = FileOutputStream(descPath
                + File.separator + entry.name)
        val content = ByteArray(entry.size.toInt())
        sevenZFile.read(content, 0, content.size)
        out.write(content)
        out.close()
        entry = sevenZFile.nextEntry
    }
    sevenZFile.close()
}
