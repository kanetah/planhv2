package top.kanetah.planhv2.api.format

import com.github.junrar.Archive
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipFile
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
import java.io.FileInputStream
import java.io.BufferedOutputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import java.io.BufferedInputStream
import java.util.ArrayList
import java.io.File.separator
import com.sun.org.apache.xerces.internal.util.DOMUtil.getParent
import org.apache.commons.compress.utils.IOUtils
import org.codehaus.plexus.util.StringUtils.isBlank


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
    if (!isDirectory) return delete()
    listFiles()?.forEach {
        if (!it.deleteAll())
            return false
    }
    return delete()
}

fun File.compact() {
    val descPath = with(canonicalPath) {
        (substring(0, lastIndexOf(".")) + java.io.File.separator).also {
            File(it).apply { if (!exists()) mkdirs() }
        }
    }
    when (name.substring(name.lastIndexOf("."))) {
        ".rar" -> unRar(this, descPath)
        ".zip" -> unZip(this, descPath)
        ".7z" -> unZip(this, descPath)
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

private fun unZip(file: File, descPath: String) {
    val zipArchiveInputStream = ZipArchiveInputStream(BufferedInputStream(FileInputStream(file), 1024))
    var entry = zipArchiveInputStream.nextZipEntry
    File(descPath, entry.name).mkdirs()
    while (entry != null) {
        if (entry.isDirectory)
            File(descPath, entry.name).mkdirs()
        else
            File(descPath, entry.name).apply {
                createNewFile()
            }.let {
                BufferedOutputStream(FileOutputStream(it), 1024)
                        .let {
                            IOUtils.copy(zipArchiveInputStream, it)
                            IOUtils.closeQuietly(it)
                        }
            }
        entry = zipArchiveInputStream.nextZipEntry
    }
    IOUtils.closeQuietly(zipArchiveInputStream)
}

fun main(args: Array<String>) {
    File("D:/planh/nico.zip").compact()
}
