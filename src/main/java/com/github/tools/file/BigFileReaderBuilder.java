package com.github.tools.file;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by renhongqiang on 2019-07-02 14:20
 */
public class BigFileReaderBuilder {
    private int threadSize = 1;
    private Charset charset = null;
    private int bufferSize = 1024 * 1024;
    private IFileHandle handle;
    private File file;

    public BigFileReaderBuilder(String file, IFileHandle handle) {
        this.file = new File(file);
        if (!this.file.exists())
            throw new IllegalArgumentException("文件不存在！");
        this.handle = handle;
    }

    public BigFileReaderBuilder withTreahdSize(int size) {
        this.threadSize = size;
        return this;
    }

    public BigFileReaderBuilder withCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public BigFileReaderBuilder withBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public BigFileReader build() {
        return new BigFileReader(this.file, this.handle, this.charset, this.bufferSize, this.threadSize);
    }
}
