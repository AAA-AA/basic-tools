## 随机工具类说明

### 指定概率命中
```java
@Test
public void testHit() {
    int count = 1;
    for (int i = 0; i < 1000000; i++) {
        boolean hit = Randoms.isScoreAHit(1, 1000);
        if (hit) {
            System.out.println(count++);
        }
    }
}
```
### 随机生成指定长度数字、字符串或数字字符串混合
```java
@Test
public void testRandomNumOrA() {
    for (int i = 0; i < 10; i++) {
        //指定5-8位长度字符串
        System.out.println(Randoms.randomOfA(5, 8));
        //指定4-7位长度数字
        System.out.println(Randoms.randomOfD(4,7));
        //指定4个长度的数字和字母混合字符串
        System.out.println(Randoms.randomOfDA(4));
    }
}
```
### 生成8-16位含数字、小写和大写字母的随机密码，有校验
```java
@Test
public void testRandomRegStr() {
    System.out.println(Randoms.checkAndGetResultString());
}
```