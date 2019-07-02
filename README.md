# 常用工具集合 basic-tool

> *latest version: 1.0-SNAPSHOT*

## 目录

- [需求背景](#需求背景)
- [功能模块](#功能模块)
    - [日期工具类](#日期工具类)
    - [图片工具类](#图片工具类)
    - [验证工具类](#验证工具类)
    - [文件工具类](#文件工具类)
- [参考列表](#参考列表)
- [FAQ](#FAQ)


## 需求背景
共性工具类在日常开发中是非常常见的，如果没有一个统一的外部工具类，势必会在多个项目里存在各种util、utils，代码过于冗余。常见的有

- date-utils
- image-utils
- validate-utils
- file-utils

对于一些共性的基础工具类是有必要进行收集和融合的，因此，为解决此类问题，开发出依赖少，功能强大，可公用的工具包

## 功能模块

### 日期工具类

### 图片工具类

### 验证工具类

### 文件工具类

#### 特性
- 支持超大文件多线程高速读取，经测试，7min 跑完 3000w+数据(MacOs，硬件配置为：i5处理器 8GB内存 128GB固态)
- 文件切割可配置化，使用简洁

#### 快速开始
1. 获取代码 
`git clone git@github.com:AAA-AA/basic-tools.git`
2. 改造代码，配置大文件路径，设置处理线程数，handle自行处理

```
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
3. 执行代码

## 参考列表

- [多线程读取大文件](https://www.cnblogs.com/metoy/p/4470418.html)

## FAQ


