## 拓展集合工具类

### list转map

```java
Order order = new Order("Carli","12112");
Order order2 = new Order("Michale","11223");
Order order3 = new Order("Marry","642321");
List<Order> list = new ArrayList<>();
list.add(order);
list.add(order2);
list.add(order3);
Map<String, Order> kvFieldBean = ExtCollections.kvFieldBean(Order::getOwner, list);

```
[](../images/ext-collection-1.jpg)

### list中key相同合并操作

```java
Order order = new Order("Carli","12112");
Order order2 = new Order("Carli","11223");
Order order3 = new Order("Marry","642321");
List<Order> list = new ArrayList<>();
list.add(order);
list.add(order2);
list.add(order3);

Map<String, List<Order>> kvFieldBeans = ExtCollections.kvFieldBeans(Order::getOwner, list);
System.out.println(kvFieldBeans);
```
[](../images/ext-collection-2.jpg)
### list中任选k,v字段，重组为map

```java
Order order = new Order("Carli","12112");
Order order2 = new Order("Carli","11223");
Order order3 = new Order("Marry","642321");
List<Order> list = new ArrayList<>();
list.add(order);
list.add(order2);
list.add(order3);
Map<String, String> map = ExtCollections.kv(Order::getId, Order::getOwner, list);
```
[](../images/ext-collection-3.jpg)