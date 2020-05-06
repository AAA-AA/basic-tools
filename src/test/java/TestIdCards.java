import com.github.tools.pub.IdCards;
import org.junit.Test;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/5 9:08 下午
 **/
public class TestIdCards {

    @Test
    public void testFemale() {
        String idcard = "420102199401015244";

        boolean checkCardId = IdCards.checkCardId(idcard);
        int age = IdCards.parseAge(idcard);
        String birthday = IdCards.parseBirthday(idcard);
        System.out.println(checkCardId);
        System.out.println(age);
        System.out.println(birthday);

    }
}
