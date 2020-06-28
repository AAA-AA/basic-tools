import com.github.tools.pub.IdCards;

public class IdCardGenerator {

    public static void main(String[] args) {
        IdCardGenerator g = new IdCardGenerator();
        for (int i = 0; i < 100; i++) {
            String idcard = IdCards.generate();
            System.out.println(String.format("idCard: %s, valid result: %s",idcard ,IdCards.checkCardId(idcard)));
        }
        System.out.println(IdCards.generateByAreaName("黄陂区"));
    }
}