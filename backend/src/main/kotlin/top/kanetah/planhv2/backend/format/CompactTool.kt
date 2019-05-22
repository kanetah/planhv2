package top.kanetah.planhv2.backend.format

import com.github.junrar.Archive
import com.github.junrar.rarfile.FileHeader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * created by kane on 2019/5/22
 */
@Suppress("unused")
class CompactTool private constructor(private val targetFile: File) {

    init {
        if (targetFile.exists())
            if (!targetFile.delete())
                throw RuntimeException("Can nor delete file '" + targetFile.name + "'.")
    }

    constructor(targetPath: String) : this(File(targetPath))

    private fun zip(srcFile: File): File {
        ZipOutputStream(FileOutputStream(targetFile)).use { out ->
            if (srcFile.isFile)
                zipFile(srcFile, out, "")
            else {
                val list = srcFile.listFiles()!!
                for (aList in list)
                    compress(aList, out, srcFile.name + File.separator)
            }
        }

        return targetFile
    }

    fun zip(srcPath: String) = zip(File(srcPath))

    private fun compress(file: File, out: ZipOutputStream, basedir: String) {
        if (file.isDirectory)
            this.zipDirectory(file, out, basedir)
        else
            this.zipFile(file, out, basedir)
    }

    private fun zipFile(srcFile: File, out: ZipOutputStream?, basedir: String) {
        if (!srcFile.exists()) return

        val buf = ByteArray(1024)
        var `in`: FileInputStream? = null
        try {
            var len: Int
            `in` = FileInputStream(srcFile)
            out!!.putNextEntry(ZipEntry(basedir + srcFile.name))
            while (`in`.read(buf).also { len = it } > 0)
                out.write(buf, 0, len)
        } finally {
            out?.closeEntry()
            `in`?.close()
        }
    }

    private fun zipDirectory(dir: File, out: ZipOutputStream, basedir: String) {
        if (!dir.exists()) return

        val files = dir.listFiles()!!
        for (file in files)
            compress(file, out, basedir + dir.name + "/")
    }

    companion object {

        fun unZip(zipFile: File, descDir: String) {
            try {
                unZip(zipFile, descDir, "utf-8")
            } catch (e: IOException) {
                try {
                    unZip(zipFile, descDir, "GBK")
                } catch (ex: Exception) {
                    throw RuntimeException(ex)
                }

            }

        }

        @Throws(IOException::class)
        private fun unZip(zipFile: File, descDir: String, charSet: String) {
            val zip = ZipFile(zipFile, Charset.forName(charSet))
            val name = zip.name.substring(
                    zip.name.lastIndexOf('\\') + 1, zip.name.lastIndexOf('.'))

            val pathFile = File(descDir + name)
            if (!pathFile.exists())
                if (!pathFile.mkdirs())
                    throw RuntimeException("can not mkdirs.")

            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val zipEntryName = entry.name
                val `in` = zip.getInputStream(entry)
                val outPath = "$descDir$name/$zipEntryName"
                        .replace("\\*".toRegex(), "/")

                val file = File(outPath.substring(0, outPath.lastIndexOf('/')))
                if (!file.exists())
                    if (!file.mkdirs())
                        throw RuntimeException("can not mkdirs.")
                if (File(outPath).isDirectory)
                    continue

                val out = FileOutputStream(outPath)
                val buf1 = ByteArray(1024)
                var len: Int
                while (`in`.read(buf1).also { len = it } > 0)
                    out.write(buf1, 0, len)
                `in`.close()
                out.close()
            }
            zip.close()
        }

        fun unRar(srcRarFile: File, descPath: String) {
            val descDir = File(descPath)
            if (!descDir.exists())
                if (!descDir.mkdirs())
                    throw RuntimeException("can not mkdirs.")
            val archive: Archive
            try {
                archive = Archive(srcRarFile)
                var fh: FileHeader? = archive.nextFileHeader()
                while (fh != null) {
                    val fileName = if (fh.fileNameW.isEmpty()) fh.fileNameString else fh.fileNameW
                    if (fh.isDirectory) {
                        val fol = File(descPath + File.separator + fileName)
                        if (!fol.mkdirs())
                            throw RuntimeException("can not mkdirs.")
                    } else {
                        val out = File(descPath + File.separator + fileName.trim { it <= ' ' })
                        if (!out.exists()) {
                            if (!out.parentFile.exists())
                                if (!out.parentFile.mkdirs())
                                    throw RuntimeException("can not mkdirs.")
                            if (!out.createNewFile())
                                throw RuntimeException("can not create new file.")
                        }
                        val os = FileOutputStream(out)
                        archive.extractFile(fh, os)
                        os.close()
                    }
                    fh = archive.nextFileHeader()
                }
                archive.close()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}
