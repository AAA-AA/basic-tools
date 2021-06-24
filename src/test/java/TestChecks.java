import com.github.tools.pub.Checks;
import org.junit.Test;

/**
 * @Author: renhongqiang
 * @Date: 2020/6/3 1:52 下午
 **/
public class TestChecks {

    @Test
    public void testNullOrEmpty() {
        String sortFieldTwo = "";
        if (Checks.isNotBlank(sortFieldTwo) && sortFieldTwo.equals("null")) {
            //doBiz
        }



    }
}
