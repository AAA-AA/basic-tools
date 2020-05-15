import com.github.tools.image.Images;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/15 6:40 下午
 **/
@Slf4j
public class TestImages {

    @Test
    public void testFromLocal() {
        String uri = "/Users/renhongqiang/Downloads/images/capture/1.jpg";
        String base64ByImgUri = Images.getBase64ByImgUri(uri);
        System.out.println(base64ByImgUri);
    }

    @Test
    public void testFromRemote() {
        String httpUri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589550112946&di=41b89e179711747cf01082f18ffa4cf5&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F0064sfU0jw1f663hur247j30m80xcq63.jpg";
        log.info("httpUri: {}", httpUri);
        String base64ByImgUri = Images.getBase64ByImgUri(httpUri);
        System.out.println(base64ByImgUri);
    }
}
