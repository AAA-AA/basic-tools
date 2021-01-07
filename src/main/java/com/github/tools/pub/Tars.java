package com.github.tools.pub;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @Author: renhongqiang
 * @Date: 2020/7/29 7:46 下午
 **/
@Slf4j
public final class Tars {

    private static final int BUFFER_SIZE = 1024 * 100;

    private Tars() {
    }

    public static boolean tarCompression(List<File> srcFiles, String destPath) throws Exception {
        log.info(" tarCompression -> Compression start!");
        FileOutputStream fos = null;
        TarArchiveOutputStream taos = null;
        try {
            fos = new FileOutputStream(new File(destPath));
            taos = new TarArchiveOutputStream(fos);
            for (File file : srcFiles) {
                BufferedInputStream bis = null;
                FileInputStream fis = null;
                try {
                    TarArchiveEntry tae = new TarArchiveEntry(file);
                    // 此处指明 每一个被压缩文件的名字,以便于解压时TarArchiveEntry的getName()方法获取到的直接就是这里指定的文件名
                    // 以(左边的)GBK编码将file.getName()“打碎”为序列,再“组装”序列为(右边的)GBK编码的字符串
                    tae.setName(new String(file.getName().getBytes("GBK"), "GBK"));
                    taos.putArchiveEntry(tae);
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int count;
                    byte data[] = new byte[1024];
                    while ((count = bis.read(data, 0, 1024)) != -1) {
                        taos.write(data, 0, count);
                    }
                } finally {
                    taos.closeArchiveEntry();
                    if (bis != null)
                        bis.close();
                    if (fis != null)
                        fis.close();
                }
            }
        } finally {
            if (taos != null)
                taos.close();
            if (fos != null)
                fos.close();

        }
        log.info(" tarCompression -> Compression end!");
        return true;
    }


    /**
     * @param decompressFilePath 要被解压的压缩文件 全路径
     * @param resultDirPath      解压文件存放绝对路径(目录)
     * @description tar拆包解压
     * @author: peizhouyu
     * @date: 2018/12/11
     * @return:
     */
    public static boolean tarDecompression(String decompressFilePath, String resultDirPath) throws Exception {
        System.out.println(" tarDecompression -> Decompression start!");
        TarArchiveInputStream tais = null;
        FileInputStream fis = null;
        try {
            File file = new File(decompressFilePath);
            fis = new FileInputStream(file);
            tais = new TarArchiveInputStream(fis);
            TarArchiveEntry tae = null;
            while ((tae = tais.getNextTarEntry()) != null) {
                BufferedOutputStream bos = null;
                FileOutputStream fos = null;
                try {
                    System.out.println("  already decompression file -> " + tae.getName());
                    String dir = resultDirPath + File.separator + tae.getName();// tar档中文件
                    File dirFile = new File(dir);
                    fos = new FileOutputStream(dirFile);
                    bos = new BufferedOutputStream(fos);
                    int count;
                    byte data[] = new byte[1024];
                    while ((count = tais.read(data, 0, 1024)) != -1) {
                        bos.write(data, 0, count);
                    }
                } finally {
                    if (bos != null)
                        bos.close();
                    if (fos != null)
                        fos.close();
                }
            }
        } finally {
            if (tais != null)
                tais.close();
            if (fis != null)
                fis.close();
        }
        System.out.println(" tarDecompression -> Decompression end!");
        return true;
    }

    /**
     * 对.tar文件进行gzip压缩
     * 说明:我们一般先把多个文件tar打包为一个,然后再使用gzip进行压缩; 进而获得形如“abc.tar.gz”这样的压缩文件
     * 注:这里暂时不再深入学习,以后有闲暇时间可深入了解如何压缩多个文件等
     * 注:如果明确知道解压后的是什么类型的文件;那么可以直接指定解压后的文件类型(实际上也需要这么做);
     * .tar.gz 解压后就是.tar文件,所以我们在解压时,给出的解压后的文件的全路径名就是以.tar结尾的
     *
     * @param filePath       要被压缩的压缩文件 全路径
     * @param resultFilePath 压缩后的文件(全文件名 .gz)
     * @description
     * @author: peizhouyu
     * @date: 2018/12/11
     * @return:
     */
    public static boolean gzipCompression(String filePath, String resultFilePath) throws IOException {
        log.info(" gzipCompression -> Compression start!");
        InputStream fin = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        GzipCompressorOutputStream gcos = null;
        try {
            fin = Files.newInputStream(Paths.get(filePath));
            bis = new BufferedInputStream(fin);
            fos = new FileOutputStream(resultFilePath);
            bos = new BufferedOutputStream(fos);
            gcos = new GzipCompressorOutputStream(bos);
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = bis.read(buffer)) != -1) {
                gcos.write(buffer, 0, read);
            }
        } finally {
            if (gcos != null)
                gcos.close();
            if (bos != null)
                bos.close();
            if (fos != null)
                fos.close();
            if (bis != null)
                bis.close();
            if (fin != null)
                fin.close();
        }
        log.info(" gzipCompression -> Compression end!");
        return true;
    }

    public static File gzip(List<File> srcFiles, File target) throws IOException {
        FileOutputStream out = null;
        out = new FileOutputStream(target);
        TarArchiveOutputStream os = new TarArchiveOutputStream(out);
        for (File srcFile : srcFiles) {
            os.putArchiveEntry(new TarArchiveEntry(srcFile.getName(), false));
            IOUtils.copy(new FileInputStream(srcFile), os);
            os.closeArchiveEntry();
        }
        if (os != null) {
            os.flush();
            os.close();
        }
        return target;
    }

    /**
     * @param filesPathArray 要压缩的文件的全路径(数组)
     * @param resultFilePath 压缩后的文件全文件名(.tar)
     * @description zip压缩(注 : 与tar类似)
     * @author: peizhouyu
     * @date: 2018/12/11
     * @return:
     */
    public static boolean zipCompression(String[] filesPathArray, String resultFilePath) throws Exception {
        System.out.println(" zipCompression -> Compression start!");
        ZipArchiveOutputStream zaos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(resultFilePath));
            zaos = new ZipArchiveOutputStream(fos);
            for (String filePath : filesPathArray) {
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    File file = new File(filePath);
                    // 第二个参数如果是文件全路径名,那么压缩时也会将路径文件夹也缩进去;
                    // 我们之压缩目标文件,而不压缩该文件所处位置的相关文件夹,所以这里我们用file.getName()
                    ZipArchiveEntry zae = new ZipArchiveEntry(file, file.getName());
                    zaos.putArchiveEntry(zae);
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int count;
                    byte data[] = new byte[1024];
                    while ((count = bis.read(data, 0, 1024)) != -1) {
                        zaos.write(data, 0, count);
                    }
                } finally {
                    zaos.closeArchiveEntry();
                    if (bis != null)
                        bis.close();
                    if (fis != null)
                        fis.close();
                }

            }
        } finally {
            if (zaos != null)
                zaos.close();
            if (fos != null)
                fos.close();
        }
        System.out.println(" zipCompression -> Compression end!");
        return true;
    }


    /**
     * @param decompressFilePath 要被解压的压缩文件 全路径
     * @param resultDirPath      解压文件存放绝对路径(目录)
     * @description zip解压(注 : 与tar类似)
     * @author: peizhouyu
     * @date: 2018/12/11
     * @return:
     */
    public static boolean zipDecompression(String decompressFilePath, String resultDirPath) throws Exception {
        System.out.println(" zipDecompression -> Decompression start!");
        ZipArchiveInputStream zais = null;
        FileInputStream fis = null;
        try {
            File file = new File(decompressFilePath);
            fis = new FileInputStream(file);
            zais = new ZipArchiveInputStream(fis);
            ZipArchiveEntry zae = null;
            while ((zae = zais.getNextZipEntry()) != null) {
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try {
                    System.out.println("  already decompression file -> " + zae.getName());
                    String dir = resultDirPath + File.separator + zae.getName();// tar档中文件
                    File dirFile = new File(dir);
                    fos = new FileOutputStream(dirFile);
                    bos = new BufferedOutputStream(fos);
                    int count;
                    byte data[] = new byte[1024];
                    while ((count = zais.read(data, 0, 1024)) != -1) {
                        bos.write(data, 0, count);
                    }
                } finally {
                    if (bos != null)
                        bos.close();
                    if (fos != null)
                        fos.close();
                }
            }
        } finally {
            if (zais != null)
                zais.close();
            if (fis != null)
                fis.close();
        }
        log.info(" zipDecompression -> Decompression end!");
        return true;
    }

    public static boolean decompress(String filePath, String outputDir, boolean isDeleted) {
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("decompress file not exist.");
            return false;
        }
        try {
            if (filePath.endsWith(".zip")) {
                unZip(file, outputDir);
            }
            if (filePath.endsWith(".tar.gz") || filePath.endsWith(".tgz")) {
                decompressTarGz(file, outputDir, true);
            }
            filterFile(new File(outputDir));
            if (isDeleted) {
                FileUtils.forceDelete(file);
            }
            return true;
        } catch (IOException e) {
            log.error("decompress occur error.");
        }
        return false;
    }

    /**
     * 删除Mac压缩再解压产生的 __MACOSX 文件夹和 .开头的其他文件
     *
     * @param filteredFile
     */
    public static void filterFile(File filteredFile) {
        if (filteredFile != null) {
            File[] files = filteredFile.listFiles();
            for (File file : files) {
                if (file.getName().startsWith(".") ||
                        (file.isDirectory() && file.getName().equals("__MACOSX"))) {
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 解压 .zip 文件
     *
     * @param file      要解压的zip文件对象
     * @param outputDir 要解压到某个指定的目录下
     * @throws IOException
     */
    public static void unZip(File file, String outputDir) throws IOException {
        try (ZipFile zipFile = new ZipFile(file, StandardCharsets.UTF_8)) {
            //创建输出目录
            createDirectory(outputDir, null);
            Enumeration<?> enums = zipFile.entries();
            while (enums.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enums.nextElement();
                if (entry.isDirectory()) {
                    //创建空目录
                    createDirectory(outputDir, entry.getName());
                } else {
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        try (OutputStream out = new FileOutputStream(
                                new File(outputDir + File.separator + entry.getName()))) {
                            writeFile(in, out);
                        }
                    }
                }
            }
        }
    }

    /**
     * 解压缩tar.gz文件
     *
     * @param file      压缩包文件
     * @param outputDir 目标文件夹
     * @param delete    解压后是否删除原压缩包文件
     */
    public static void decompressTarGz(File file, String outputDir, boolean delete) {
        try {
            try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
                    new GzipCompressorInputStream(
                            new BufferedInputStream(
                                    new FileInputStream(file))))) {
                //创建输出目录
                createDirectory(outputDir, null);
                TarArchiveEntry entry = null;
                while ((entry = tarIn.getNextTarEntry()) != null) {
                    //是目录
                    if (entry.isDirectory()) {
                        //创建空目录
                        createDirectory(outputDir, entry.getName());
                    } else {
                        //是文件
                        try (OutputStream out = new FileOutputStream(
                                new File(outputDir + File.separator + entry.getName()))) {
                            writeFile(tarIn, out);
                        }
                    }
                }
            }
            if (delete) {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写文件
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void writeFile(InputStream in, OutputStream out) throws IOException {
        int length;
        byte[] b = new byte[BUFFER_SIZE];
        while ((length = in.read(b)) != -1) {
            out.write(b, 0, length);
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir 输出目录
     * @param subDir    子目录
     */
    private static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }

    public static void main(String[] args) throws Exception {
        String tarGz = "/Users/renhongqiang/Downloads/builds/openjdk8-alpine.tar.gz";
        String tar = tarGz.substring(0, tarGz.lastIndexOf(".gz"));
        String content = "lalalal";
        String tmpCompose = "/Users/renhongqiang/Downloads/builds/tmpCompose.yaml";
        String finalTar = "/Users/renhongqiang/Downloads/builds/test4.tar";
        FileUtils.copyInputStreamToFile(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8)), new File(tmpCompose));
        List<File> srcPaths = Lists.newArrayList(new File(tarGz), new File(tmpCompose));
        tarCompression(srcPaths, finalTar);
        gzipCompression(finalTar, finalTar + ".gz");

    }

    private static void renameFile(File file) {
        String[] split = file.getName().split("-");
        String suffix = split[split.length - 1];
        String projectName = file.getName().substring(0, file.getName().indexOf(suffix) - 1);
        File dest = new File(file.getParentFile().getAbsolutePath() + "/" + projectName);
        file.renameTo(dest);
    }

}
