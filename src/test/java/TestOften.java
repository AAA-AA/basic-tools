import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: renhongqiang
 * @Date: 2020/8/13 12:28 下午
 **/
public class TestOften {

    @Test
    public void testReduce() {
        List<Boolean> result = Lists.newArrayList(true, true, false, true);

        Boolean finalResult = result.stream().reduce((x, y) -> x && y).get();
        System.out.println(finalResult);
        List<Integer> list = Lists.newArrayList(1);
        Set<Integer> containers = list.stream().collect(Collectors.toSet());
        System.out.println(containers.contains(1));
    }
}
