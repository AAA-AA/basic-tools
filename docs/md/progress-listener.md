## 速率监控工具说明

### 初始化一个监控工具
```java
ProgressListener downProgress = new ProgressListener("下载计数器", datas.size())

```
### 添加统计维度
```java
picProgress.addCount("下载成功数");
picProgress.addCount("下载失败数");
```

### 开启统计
```java
picProgress.startLog();
```

### 说明
1.默认10秒钟打印一次日志(可以指定，避免大量日志过多)
2.可以统计平均速率，和当前的速率