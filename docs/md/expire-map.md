## 使用说明

### 过期map
```java
ExpireMap<String, String> map = new ExpireMap<>(1, 10);
map.put("aaa", "test", 3, TimeUnit.SECONDS);
```