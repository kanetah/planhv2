package top.kanetah.planhv2.backend.format;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("unused")
public class CompactTool {

    private File targetFile;

    private CompactTool(File target) {
        targetFile = target;
        if (targetFile.exists())
            if (!targetFile.delete())
                throw new RuntimeException("Can nor delete file '" + targetFile.getName() + "'.");
    }

    public CompactTool(String targetPath) {
        this(new File(targetPath));
    }

    private File zip(File srcFile) {
        try {
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFile))) {
                if (srcFile.isFile())
                    zipFile(srcFile, out, "");
                else {
                    File[] list = srcFile.listFiles();
                    assert list != null;
                    for (File aList : list)
                        compress(aList, out, srcFile.getName() + File.separator);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }

    public File zip(String srcPath) {
        return zip(new File(srcPath));
    }

    private void compress(File file, ZipOutputStream out, String basedir) {
        if (file.isDirectory())
            this.zipDirectory(file, out, basedir);
        else
            this.zipFile(file, out, basedir);
    }

    private void zipFile(File srcFile, ZipOutputStream out, String basedir) {
        if (!srcFile.exists())
            return;

        byte[] buf = new byte[1024];
        FileInputStream in = null;
        try {
            try {
                int len;
                in = new FileInputStream(srcFile);
                out.putNextEntry(new ZipEntry(basedir + srcFile.getName()));
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
            } finally {
                if (out != null)
                    out.closeEntry();
                if (in != null)
                    in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void zipDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files)
            compress(file, out, basedir + dir.getName() + "/");
    }

    public static void unZip(File zipFile, String descDir) {
        try {
            unZip(zipFile, descDir, "utf-8");
        } catch (IOException e) {
            try {
                unZip(zipFile, descDir, "GBK");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void unZip(File zipFile, String descDir, String charSet) throws IOException {
        ZipFile zip = new ZipFile(zipFile, Charset.forName(charSet));
        String name = zip.getName().substring(
                zip.getName().lastIndexOf('\\') + 1, zip.getName().lastIndexOf('.'));

        File pathFile = new File(descDir + name);
        if (!pathFile.exists())
            if (!pathFile.mkdirs())
                throw new RuntimeException("can not mkdirs.");

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + name + "/" + zipEntryName)
                    .replaceAll("\\*", "/");

            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists())
                if (!file.mkdirs())
                    throw new RuntimeException("can not mkdirs.");
            if (new File(outPath).isDirectory())
                continue;

            FileOutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            for (int len; (len = in.read(buf1)) > 0; )
                out.write(buf1, 0, len);
            in.close();
            out.close();
        }
        zip.close();
    }

    public static void unRar(File srcRarFile, String descPath) {
        File descDir = new File(descPath);
        if (!descDir.exists())
            if (!descDir.mkdirs())
                throw new RuntimeException("can not mkdirs.");
        Archive archive;
        try {
            archive = new Archive(srcRarFile);
            FileHeader fh = archive.nextFileHeader();
            while (fh != null) {
                String fileName = fh.getFileNameW().isEmpty() ? fh.getFileNameString() : fh.getFileNameW();
                if (fh.isDirectory()) {
                    File fol = new File(descPath + File.separator + fileName);
                    if (!fol.mkdirs())
                        throw new RuntimeException("can not mkdirs.");
                } else {
                    File out = new File(descPath + File.separator + fileName.trim());
                    if (!out.exists()) {
                        if (!out.getParentFile().exists())
                            if (!out.getParentFile().mkdirs())
                                throw new RuntimeException("can not mkdirs.");
                        if (!out.createNewFile())
                            throw new RuntimeException("can not create new file.");
                    }
                    FileOutputStream os = new FileOutputStream(out);
                    archive.extractFile(fh, os);
                    os.close();
                }
                fh = archive.nextFileHeader();
            }
            archive.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
