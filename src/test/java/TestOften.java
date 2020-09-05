import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

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

    }
}
