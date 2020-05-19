import com.github.tools.pub.Randoms;
import org.junit.Test;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/19 4:31 下午
 **/
public class TestRandoms {

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

    @Test
    public void testRandomNumOrA() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Randoms.randomOfA(5, 8));
        }
    }



}
