import com.github.tools.pub.Dates;
import com.github.tools.pub.Format;
import com.sun.org.apache.bcel.internal.generic.FSUB;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.tools.pub.Format.DATE_TIME_FORMAT;
import static com.github.tools.pub.Format.DATE_YYYYMMDDHHMMSS;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/9 4:05 下午
 **/
public class TestDates {

    @Test
    public void testDates() {
        System.out.println(Dates.format(new Date().getTime()));
    }

    @Test
    public void testStr2Str() {
        System.out.println(Dates.format("2021-05-20 18:00:00", DATE_TIME_FORMAT,DATE_YYYYMMDDHHMMSS));
        System.out.println(cutByStr(Dates.format(Dates.now(), DATE_YYYYMMDDHHMMSS)));

    }

    @Test
    public void testCostTime(){
        List<String> datesStrs = new ArrayList<>();
        for (int i = 1; i < 500000; i++) {
            Date date = Dates.addHours(Dates.now(), i);
            datesStrs.add(Dates.format(date, DATE_YYYYMMDDHHMMSS));
        }
        long start1 = System.currentTimeMillis();
        for (String datesStr : datesStrs) {
            System.out.println(Dates.format(datesStr,DATE_YYYYMMDDHHMMSS, DATE_TIME_FORMAT));
        }
        long cost1  = (System.currentTimeMillis() - start1);

        long start2 = System.currentTimeMillis() ;

        for (String datesStr : datesStrs) {
            System.out.println(cutByStr(datesStr));
        }
        long cost2 = (System.currentTimeMillis()-start2);
        System.out.println("format cost: "+ cost1+", cut cost: "+ cost2);
    }

    private String cutByStr(String datesStr) {
        String res = datesStr.substring(0, 4) + "-" + datesStr.substring(4, 6) + "-" + datesStr.substring(6, 8) + " " +
                datesStr.substring(8, 10) + ":" + datesStr.substring(10, 12) + ":" + datesStr.substring(12, 14);
        return res;
    }


}
