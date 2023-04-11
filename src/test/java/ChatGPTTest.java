import com.lilittlecat.chatgpt.offical.ChatGPT;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * <p>
 * Test with usage examples.
 * </p>
 *
 * @author LiLittleCat
 * @link <a href="https://github.com/LiLittleCat">https://github.com/LiLittleCat</a>
 * @since 2022/12/8
 */
@Disabled
class ChatGPTTest {
    @Test
    void test() {
        ChatGPT chatGPT = new ChatGPT("YOUR API KEY");
        String hello = chatGPT.ask("Hello");
        System.out.println(hello);
    }

    @Test
    void testUseProxy() {
        ChatGPT chatGPT = new ChatGPT("YOUR API KEY", "127.0.0.1", 7890);
        String hello = chatGPT.ask("Hello");
        System.out.println(hello);
    }

    @Test
    void testUseProxy1() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        ChatGPT chatGPT = new ChatGPT("YOUR API KEY", proxy);
        String hello = chatGPT.ask("Hello");
        System.out.println(hello);
    }

    @Test
    void testUseOfferedClient() {
        ChatGPT chatGPT = new ChatGPT("YOUR API KEY", new OkHttpClient());
        String hello = chatGPT.ask("Hello");
        System.out.println(hello);
    }

    @Test
    void useCustomHost() {
        ChatGPT chatGPT = new ChatGPT("https://your.api.host","YOUR API KEY");
        String hello = chatGPT.ask("Hello");
        System.out.println(hello);
    }
}

