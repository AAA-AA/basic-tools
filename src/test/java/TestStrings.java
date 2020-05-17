import com.github.tools.pub.Strings;
import org.junit.Test;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/17 10:30 下午
 **/
public class TestStrings {

    @Test
    public void testLineToHump() {
        String authUser = "auth_user";
        System.out.println(Strings.lineToHump(authUser));
    }

    @Test
    public void testHumpToLine() {
        String authUser = "authUser";
        System.out.println(Strings.humpToLine(authUser));
    }

    @Test
    public void testSubString() {
        String demoString = "demo_string_123";
        System.out.println(Strings.substring(demoString,1,3));
    }


}
