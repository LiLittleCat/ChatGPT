import com.lilittlecat.chatgpt.offical.ChatGPT;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * Test
 * </p>
 *
 * @author LiLittleCat
 * @link <a href="https://github.com/LiLittleCat">https://github.com/LiLittleCat</a>
 * @since 2022/12/8
 */
class ChatGPTTest {
    @Test
    void test() {
        ChatGPT chatGPT = new ChatGPT("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");
        String hello = chatGPT.ask("Hello");
        System.out.println(hello);
    }
}

