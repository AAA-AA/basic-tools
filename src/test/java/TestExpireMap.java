import com.github.tools.collection.ExpireMap;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author : hongqiangren.
 * @since: 2022/6/8 21:57
 */
public class TestExpireMap {

    private static ExpireMap expireMap = new ExpireMap();

    @Test
    public void testExpireSecond() throws InterruptedException {
        expireMap.put("1", "val", 10, TimeUnit.SECONDS);
        Object o = expireMap.get("1");
        for (int i = 1; i < 15; i++) {
            Thread.sleep(1000);
            System.out.println(String.format("第%d秒后，map中的值为：%s", i, expireMap.get("1")));
        }
    }

}
