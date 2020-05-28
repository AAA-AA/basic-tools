## 使用说明

### 获取当天零点
```java
Date today = Dates.today();
```
### 将日期类型格式化为字符串
```java
Date date = Dates.addDays(custDate, 32);
String format = Dates.format(date);
```

### 将字符串类型parse为日期类型
```java
Date custDate = Dates.parse("2020-04-23 25", Dates.DATE_FORMAT);
```
### 对日期时间的各类操作
```java
Date date = Dates.addDays(custDate, 32);
Date addM = Dates.addMinutes(date,5);

```