import com.github.tools.pub.Dates;
import org.junit.Test;

import java.util.Date;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/9 4:05 下午
 **/
public class TestDates {

    @Test
    public void testDates() {
        System.out.println(Dates.format(new Date().getTime()));
    }
}
