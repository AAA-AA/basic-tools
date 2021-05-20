import com.github.tools.collection.ExtCollections;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/1 9:57 下午
 **/
public class TestExtCollections {


    @Test
    public void testKvFieldBean() {
        Order order = new Order("Carli","12112");
        Order order2 = new Order("Michale","11223");
        Order order3 = new Order("Marry","642321");
        List<Order> list = new ArrayList<>();
        list.add(order);
        list.add(order2);
        list.add(order3);

        Map<String, Order> kvFieldBean = ExtCollections.kvFieldBean(Order::getOwner, list);

        Map<String, Order> orderMap = list.stream().collect(Collectors.toMap(Order::getOwner, e -> e));
        System.out.println(kvFieldBean.size() == orderMap.size());
        byte b = (byte)128;
        System.out.println(b);
        byte c = 5;
        byte d = (byte) 200;
        //float e = 1.5;
    }


    @Test
    public void testKvFieldBeans() {
        Order order = new Order("Carli","12112");
        Order order2 = new Order("Carli","11223");
        Order order3 = new Order("Marry","642321");
        List<Order> list = new ArrayList<>();
        list.add(order);
        list.add(order2);
        list.add(order3);

        Map<String, List<Order>> kvFieldBeans = ExtCollections.kvFieldBeans(Order::getOwner, list);
        System.out.println(kvFieldBeans);

    }

    @Test
    public void testKV() {
        Order order = new Order("Carli","12112");
        Order order2 = new Order("Carli","11223");
        Order order3 = new Order("Marry","642321");
        List<Order> list = new ArrayList<>();
        list.add(order);
        list.add(order2);
        list.add(order3);
        Map<String, String> map = ExtCollections.kv(Order::getId, Order::getOwner, list);
        System.out.println(map);


    }







    @Data
    public static class Order {
        private String owner;
        private String id;

        public Order() {
        }

        public Order(String owner, String id) {
            this.owner = owner;
            this.id = id;
        }
    }
}
