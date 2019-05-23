package top.kanetah.planhv2.backend.format

import com.github.junrar.Archive
import com.github.junrar.rarfile.FileHeader
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * 压缩文件
 */
fun File.zip() = File("${this.path}.zip") zipFrom this

infix fun File.zipFrom(srcFile: File): File {
    // 若压缩文件已存在，则将其删除
    if (this.exists())
        if (!this.delete())
            throw RuntimeException("Can not delete file '" + this.name + "'.")
    // 创建新的压缩文件
    ZipOutputStream(FileOutputStream(this)).use { out ->
        if (srcFile.isFile) {
            zipFile(srcFile, out, "")
        } else {
            for (elem in srcFile.listFiles().filter {
                if (it.isDirectory) true else {
                    val suffix = it.name.substring(it.name.lastIndexOf(".")).toLowerCase()
                    !arrayOf(".rar", ".zip", ".7z").contains(suffix)
                }
            }) {
                compress(elem, out, srcFile.name + File.separator)
            }
        }
    }
    return this
}

private fun compress(file: File, out: ZipOutputStream, basedir: String) {
    if (file.isDirectory)
        zipDirectory(file, out, basedir)
    else
        zipFile(file, out, basedir)
}

private fun zipFile(srcFile: File, out: ZipOutputStream, basedir: String) {
    if (!srcFile.exists()) return

    val buf = ByteArray(1024)
    var `in`: FileInputStream? = null
    try {
        var len: Int
        `in` = FileInputStream(srcFile)
        out.putNextEntry(ZipEntry(basedir + srcFile.name))
        while (`in`.read(buf).also { len = it } > 0)
            out.write(buf, 0, len)
    } finally {
        out.closeEntry()
        `in`?.close()
    }
}

private fun zipDirectory(dir: File, out: ZipOutputStream, basedir: String) {
    if (!dir.exists()) return

    val files = dir.listFiles()!!
    for (file in files)
        compress(file, out, basedir + dir.name + "/")
}

/**
 * 删除文件夹
 */
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
    when (name.substring(name.lastIndexOf(".")).toLowerCase()) {
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
//    delete()
}

private fun unRar(file: File, descPath: String): String {
    val archive = Archive(file)
    var fh: FileHeader?
    while (null != archive.nextFileHeader().also { fh = it }) {
        val fileName = if (fh!!.fileNameW.isEmpty()) fh!!.fileNameString else fh!!.fileNameW
        if (fh!!.isDirectory)
            File(descPath + fileName).mkdirs()
        else {
            val out = File(descPath + fileName.trim { it <= ' ' }).apply {
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
        entries.iterator().forEach { entry ->
            val zipEntryName = entry.name
            val outPath = (descPath + zipEntryName)
                    .replace("\\*".toRegex(), "/")
            File(outPath.substring(0, outPath.lastIndexOf(File.separator))).apply { if (!exists()) mkdirs() }
            if (File(outPath).isDirectory) return@forEach
            val out = FileOutputStream(outPath)
            val buff = ByteArray(1024)
            zip.getInputStream(entry).apply {
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
        else
            e.printStackTrace()
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
