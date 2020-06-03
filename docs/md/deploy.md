## jar包部署说明
若想在私服内使用该工具包，可执行命令
```bash
mvn clean deploy -e -U -Dmaven.test.skip=true
```

然后引入maven依赖即可

```java

dependency>
    <groupId>github.com</groupId>
    <artifactId>basic-tools</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```