import com.github.tools.pub.Names;
import org.junit.Test;

import java.util.Random;

/**
 * @Author: renhongqiang
 * @Date: 2020/6/29 10:25 上午
 **/
public class TestNames {

    @Test
    public void testGenerateName() {
        for (int i = 0; i < 10; i++) {
            String fullName = Names.getFullName();
            System.out.println(fullName);
        }
    }
}
