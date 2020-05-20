# 超大文本极速读取工具类

## 使用说明

```java
public static void main(String[] args) {
        AtomicLong counter = new AtomicLong(0);
        String bigFilePath = "/Users/renhongqiang/Downloads/work-doc/2000W/test.csv";
        BigFileReader.Builder builder = new BigFileReader.Builder(bigFilePath, line -> System.out.println(String.format("total record: %s,line is: %s", counter.incrementAndGet(), line)));
        BigFileReader bigFileReader = builder
                .threadPoolSize(100)
                .charset(StandardCharsets.UTF_8)
                .bufferSize(1024 * 1024).build();
        bigFileReader.start();
}
```
## 实际效果见博客

[多线程极速读取超大文件](https://blog.csdn.net/Justnow_/article/details/95505899)