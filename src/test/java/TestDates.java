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
        Date today = Dates.today();
        Dates.addDays(new Date(),32);
        Date custDate = Dates.parse("2020-04-23 25", Dates.DATE_FORMAT);
        Date date = Dates.addDays(custDate, 32);
        Dates.addMinutes(date,5);
        String format = Dates.format(date);
        System.out.println(format);
        System.out.println(date);
    }
}
