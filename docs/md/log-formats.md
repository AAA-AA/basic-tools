## 日志格式化工具说明

### 敏感或超长字符串截断
在需要的字段上加 @HideImg
```java
@Test
    public void testHide() {
        Person person = new Person();
        System.out.println(LogFormats.formatLog(person));
    }

    @Data
    public static class Person {

        @HideImg
        private String img = "/9jhfsdhajfksjdfhuhiuwbjbdfoasuhrttttttthhhhhhhhhhhhhhhhhhdfuf";

        @HideAnn
        private String password = "128397287827802hhfashfkjhasfh";

        @HideCollection
        private List<String> cellphones = new ArrayList<>();
    }
```

### 敏感信息直接置为******
直接在字段上加@HideAnn注解

### 敏感字段集合直接过滤
直接在集合字段上加@HideCollection注解
