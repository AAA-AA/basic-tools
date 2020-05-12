import com.github.tools.annotation.HideAnn;
import com.github.tools.annotation.HideCollection;
import com.github.tools.annotation.HideImg;
import com.github.tools.pub.LogFormats;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/12 4:00 下午
 **/
public class TestLogFormats {


    @Test
    public void testHide() {
        Person person = new Person();
        System.out.println(LogFormats.formatLog(person));
    }

    @Data
    public static class Person {

        @HideImg
        private String img = "/9jhfsdhajfksjdfhuhiuwbjbdfoasuhrttttttthhhhhhhhhhhhhhhhhhdfuf";

        @HideAnn
        private String password = "128397287827802hhfashfkjhasfh";

        @HideCollection
        private List<String> cellphones = new ArrayList<>();
    }


}
