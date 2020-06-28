## 身份证工具类说明

### 有效身份证号自动生成
```java
@Test
public void testGenerate() {
    System.out.println(IdCards.generate());
    System.out.println(IdCards.generateByAreaName("东城区"));
}
```
### 年龄提取
```java
IdCards.parseAge("420102199401015244")
```
### 出生年月提取
```java
IdCards.parseBirthday("420102199401015244")
```
### 身份证合法性校验
```java
boolean checkCardId = IdCards.checkCardId("420102199401015244");
```

### 15到18位身份证转换

```java
String idCard = IdCards.trans15To18("420102199401015")
```