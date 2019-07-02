package com.github.tools.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by renhongqiang on 2019-07-02 14:23
 */
public class TestBigFileRead {

    public static void main(String[] args) {
        AtomicLong counter = new AtomicLong(0);
        String smallFilePath = "/Users/renhongqiang/Downloads/work-doc/2000W/demo.txt";
        String bigFilePath = "/Users/renhongqiang/Downloads/work-doc/2000W/test.csv";
        BigFileReader.Builder builder = new BigFileReader.Builder(smallFilePath, line -> System.out.println(String.format("total record: %s,line is: %s", counter.incrementAndGet(), line)));
        BigFileReader bigFileReader = builder
                .threadPoolSize(2)
                .charset(StandardCharsets.UTF_8)
                .bufferSize(1024).build();
        bigFileReader.start();
    }

    public static long getLineNumber(File file) {
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                long lines = lineNumberReader.getLineNumber() + 1;
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
