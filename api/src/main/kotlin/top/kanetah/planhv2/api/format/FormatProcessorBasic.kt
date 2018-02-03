package top.kanetah.planhv2.api.format

import com.github.junrar.Archive
import org.springframework.web.multipart.MultipartFile
import top.kanetah.planhv2.api.entity.SubmitFileAttributes
import top.kanetah.planhv2.api.format.processor.OutsideFileNameFormatProcessor
import top.kanetah.planhv2.api.APP_CONTEXT
import top.kanetah.planhv2.api.entity.Task
import top.kanetah.planhv2.api.entity.Team
import top.kanetah.planhv2.api.entity.User
import top.kanetah.planhv2.api.format.FormatProcessorClass.FormatValue.*
import top.kanetah.planhv2.api.property.PropertyListener
import top.kanetah.planhv2.api.repository.SubjectRepository
import top.kanetah.planhv2.api.service.RepositoryService
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
    fun sendEMail(taskId: Int): Boolean
    
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
            operator fun get(key: String?) = key?.run {
                values().filter { it.key === key }[0]
            }
        }
    }
    
    fun getFormatName(
            user: User, task: Task, team: Team?, file: MultipartFile
    ) = if (task.format.isNullOrEmpty()) throw Exception("任务指定的格式化处理器不合适")
    else StringBuilder(task.format).let { name ->
        Regex("#\\{[\\w]*}").findAll(task.format!!).forEach {
            it.value.let {
                Regex(it).replace(name, when (FormatValue[Regex("[\\w]+").find(it)!!.value]) {
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
                    null -> it
                })
            }
        }
    }.toString()
    
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
//    if (!isDirectory) return delete()
    listFiles()?.forEach {
        if (!it.deleteAll())
            return false
    }
    return delete()
}

fun File.uncompress() {
    val descPath = with(canonicalPath) {
        (substring(0, lastIndexOf(".")) + File.separator).also {
            File(it).apply { if (!exists()) mkdirs() }
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
    var fh = archive.nextFileHeader()
    while (fh != null) {
        val fileName = if (fh.fileNameW.isEmpty()) fh.fileNameString else fh.fileNameW
        if (fh.isDirectory)
            File(descPath + fileName).mkdirs()
        else {
            val out = File(descPath + fileName.trim({ it <= ' ' }))
            if (!out.exists()) {
                if (!out.parentFile.exists())
                    out.parentFile.mkdirs()
                out.createNewFile()
            }
            val os = FileOutputStream(out)
            archive.extractFile(fh, os)
            os.close()
        }
        fh = archive.nextFileHeader()
    }
    archive.close()
    return descPath
}

private fun unZip(file: File, descPath: String, charSet: String = "utf-8") {
    try {
        val zip = ZipFile(file, Charset.forName(charSet))
        val name = with(zip.name) { substring(lastIndexOf("\\") + 1, lastIndexOf(".")) }
        val pathFile = File(descPath + name)
        if (!pathFile.exists()) pathFile.mkdirs()
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val zipEntryName = entry.name
            val `in` = zip.getInputStream(entry)
            val outPath = (descPath + name + "/" + zipEntryName)
                    .replace("\\*".toRegex(), "/")
            val outputFile = File(outPath.substring(0, outPath.lastIndexOf('/')))
            if (!outputFile.exists())
                if (!outputFile.mkdirs())
                    throw RuntimeException("can not mkdirs.")
            if (File(outPath).isDirectory)
                continue
            val out = FileOutputStream(outPath)
            val buf1 = ByteArray(1024)
            var len = `in`.read(buf1)
            while (len > 0) {
                out.write(buf1, 0, len)
                len = `in`.read(buf1)
            }
            `in`.close()
            out.close()
        }
        zip.close()
    } catch (e: Exception) {
        if (charSet == "utf-8")
            unZip(file, descPath, "GBK")
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

fun main(args: Array<String>) {
    File("D:/planh/nico.zip").uncompress()
}
