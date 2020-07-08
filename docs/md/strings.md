## 字符串工具类说明

### 下划线变驼峰(LineToHump)
```java
@Test
public void testLineToHump() {
    String authUser = "auth_user";
    System.out.println(Strings.lineToHump(authUser));
}
```

### 驼峰变下划线(HumpToLine)
```java
@Test
public void testHumpToLine() {
    String authUser = "authUser";
    System.out.println(Strings.humpToLine(authUser));
}
```
### 截取字符串(Cut string)
```java
@Test
public void testSubString() {
    String demoString = "demo_string_123";
    System.out.println(Strings.substring(demoString,1,3));
}
```