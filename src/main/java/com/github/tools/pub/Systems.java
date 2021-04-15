package com.github.tools.pub;

import com.google.common.collect.EvictingQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: renhongqiang
 * @Date: 2020/8/12 11:24 上午
 **/
@Slf4j
public final class Systems {

    public static class Std {
        int exitValue;
        //正常信息输出
        private StringBuilder std;
        private StringBuilder errorStd;

        //当输出内容非常多时，用这个固定容器保存，只保留最后500条
        private EvictingQueue<String> msgQueue;

        public Std() {
            this.std = new StringBuilder();
            this.errorStd = new StringBuilder();
            this.msgQueue = EvictingQueue.create(500);
        }

        public Std(StringBuilder std, StringBuilder errorStd) {
            this.std = std;
            this.errorStd = errorStd;
        }

        public String print() {
            return std.toString() + System.lineSeparator() + errorStd.toString();
        }

        public int getExitValue() {
            return exitValue;
        }

        public void setExitValue(int exitValue) {
            this.exitValue = exitValue;
        }

        public StringBuilder getStd() {
            return std;
        }

        public void setStd(StringBuilder std) {
            this.std = std;
        }

        public StringBuilder getErrorStd() {
            return errorStd;
        }

        public void setErrorStd(StringBuilder errorStd) {
            this.errorStd = errorStd;
        }

        public EvictingQueue<String> getMsgQueue() {
            return msgQueue;
        }

        public void setMsgQueue(EvictingQueue<String> msgQueue) {
            this.msgQueue = msgQueue;
        }
    }

    public static void exec(String[] cmds, long timeout, TimeUnit timeUnit, Std std, Runnable callback) throws InterruptedException, IOException {
        Process p = Runtime.getRuntime().exec(cmds);
        readProcessOutput(p, std, callback);
        p.waitFor(timeout, timeUnit);
        std.setExitValue(p.exitValue());
    }

    public static void exec(String[] cmds, String exeDir, long timeout, TimeUnit timeUnit, Std std, Runnable callback) throws InterruptedException, IOException {
        Process p = Runtime.getRuntime().exec(cmds, null, new File(exeDir));
        readProcessOutput(p, std, callback);
        p.waitFor(timeout, timeUnit);
        std.setExitValue(p.exitValue());
    }

    public static void exec(String cmds, long timeout, TimeUnit timeUnit, Std std, Runnable callback) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("sh")
                .redirectErrorStream(true);
        Process p = processBuilder.start();
        OutputStream os = p.getOutputStream();
        os.write(cmds.getBytes());
        os.flush();
        os.close();
        readProcessOutput(p, std, callback);
        p.waitFor(timeout, timeUnit);
        std.setExitValue(p.exitValue());
    }

    public static void exec(String cmds, String dir, long timeout, TimeUnit timeUnit, Std std, Runnable callback) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("sh")
                .directory(new File(dir))
                .redirectErrorStream(true);
        Process p = processBuilder.start();
        OutputStream os = p.getOutputStream();
        os.write(cmds.getBytes());
        os.flush();
        os.close();
        readProcessOutput(p, std, callback);
        p.waitFor(timeout, timeUnit);
        std.setExitValue(p.exitValue());
    }

    public static Std exec(String cmds, String exeDir, long timeout, TimeUnit timeUnit) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("sh")
                .command(cmds)
                .directory(new File(exeDir))
                .redirectErrorStream(true);
        Process p = processBuilder.start();
        Std msg = readProcessOutput(p);
        p.waitFor(timeout, timeUnit);
        msg.setExitValue(p.exitValue());
        return msg;
    }

    public static Std exec(String cmds, long timeout, TimeUnit timeUnit) throws InterruptedException, IOException {
        Process p = Runtime.getRuntime().exec(cmds);
        Std msg = readProcessOutput(p);
        p.waitFor(timeout, timeUnit);
        msg.setExitValue(p.exitValue());
        return msg;
    }

    public static Std exec(String[] cmds, long timeout, TimeUnit timeUnit) throws InterruptedException, IOException {
        Process p = Runtime.getRuntime().exec(cmds);
        Std msg = readProcessOutput(p);
        p.waitFor(timeout, timeUnit);
        msg.setExitValue(p.exitValue());
        return msg;
    }

    /**
     * 打印进程输出
     *
     * @param process 进程
     */
    private static void readProcessOutput(final Process process, Std std, Runnable callback) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        CompletableFuture.runAsync(() -> read(process.getInputStream(), std, callback));
        CompletableFuture.runAsync(() -> read(process.getErrorStream(), std.getErrorStd()));
    }

    /**
     * 打印进程输出
     *
     * @param process 进程
     */
    private static Std readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        Std msg = new Std();
        read(process.getInputStream(), msg.getStd());
        read(process.getErrorStream(), msg.getErrorStd());
        return msg;
    }


    //每5行记录执行一次异步刷盘
    // 读取输入流
    private static void read(InputStream inputStream, Std std, Runnable callback) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 20480);
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                std.getMsgQueue().add(line + System.lineSeparator());
                std.getStd().append(line).append(System.lineSeparator());
                if (i++ % 5 == 0) {
                    callback.run();
                }
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取输入流
    private static void read(InputStream inputStream, StringBuilder std) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                std.append(line).append(System.lineSeparator());
                System.out.println(std.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1. 单工程单模块类
     * 2. 单工程多模块类，深度2级，如：auto-ci-platform
     * 3. 单工程多模块类，深度3级，如：https://git-pd.megvii-inc.com/GDC-Tech/general-docking-platform
     *
     * @param executeRootPath
     */
    public static void extractJars(String executeRootPath) throws IOException {
        log.info("execute root path: {}", executeRootPath);
        File file = new File(executeRootPath);
        List<File> destJars = new ArrayList<>();
        findJar(file.listFiles(File::isDirectory), destJars);
        //都提取到docker目录
        for (File jar : destJars) {
            FileUtils.copyFile(jar, new File(String.format("%s/docker/%s", executeRootPath, jar.getName())));
            FileUtils.copyFile(jar, new File(executeRootPath, jar.getName()));
        }
    }

    private static void findJar(File[] fileDirs, List<File> jars) {
        for (File fileDir : fileDirs) {
            if (fileDir.getName().equals("target")) {
                File[] files = fileDir.listFiles(File::isFile);
                for (File rootTargetChild : files) {
                    if (rootTargetChild.getName().endsWith(".jar") && !rootTargetChild.getName().contains("-source")) {
                        jars.add(rootTargetChild);
                    }
                }
            }
            File[] childDirs = fileDir.listFiles(File::isDirectory);
            findJar(childDirs, jars);
        }
    }

    //抽取编译后的pro目录或者dist目录
    public static void extractNode(String executeRootPath) throws IOException {
        log.info("execute root path: {}", executeRootPath);
        File file = new File(executeRootPath);
        for (File dirFile : file.listFiles(File::isDirectory)) {
            if (Dates.now().getTime() - dirFile.lastModified() < 30 * 1000 && !dirFile.getName().equals("node_modules")) {
                FileUtils.copyDirectory(dirFile, new File(String.format("%s/docker/%s", executeRootPath, dirFile.getName())));
            }
        }
    }

    //抽取golang编译后的可执行文件
    public static void extractExe(String executeRootPath) throws IOException {
        log.info("execute root path: {}", executeRootPath);
        File file = new File(executeRootPath);
        for (File exeFile : file.listFiles(File::isFile)) {
            if (Dates.now().getTime() - exeFile.lastModified() < 30 * 1000 ) {
                FileUtils.copyFile(exeFile, new File(String.format("%s/docker/%s", executeRootPath, exeFile.getName())));
            }
        }
    }

    public static String fixedStdMsg(Std std) {
        String msg = std.print();
        String[] lines = msg.split(System.lineSeparator());
        if (lines.length >= 1000) {
            StringBuilder tailLines = new StringBuilder();
            for (int i = lines.length - 500; i < lines.length - 1; i++) {
                tailLines.append(lines[i] + System.lineSeparator());
            }
            return tailLines.toString();
        }
        return msg;
    }

    public static String fixedMsg(String msg) {
        String[] lines = msg.split(System.lineSeparator());
        if (lines.length >= 1000) {
            StringBuilder tailLines = new StringBuilder();
            for (int i = lines.length - 500; i <= lines.length - 1; i++) {
                tailLines.append(lines[i] + System.lineSeparator());
            }
            return tailLines.toString();
        }
        return msg;
    }

    public static String fixedMsg(Std std) {
        EvictingQueue<String> msgQueue = std.getMsgQueue();
        List<String> lines = msgQueue.parallelStream().collect(Collectors.toList());
        return String.join("", lines);
    }
}
