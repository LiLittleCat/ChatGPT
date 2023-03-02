package com.lilittlecat.chatgpt.offical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilittlecat.chatgpt.offical.entity.ChatCompletionRequestBody;
import com.lilittlecat.chatgpt.offical.entity.Constant;
import com.lilittlecat.chatgpt.offical.entity.Message;
import com.lilittlecat.chatgpt.offical.entity.Model;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.lilittlecat.chatgpt.offical.entity.Constant.DEFAULT_USER;

/**
 * <p>
 * a Java client for ChatGPT uses official API.
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Slf4j
public class ChatGPT {
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected OkHttpClient client = new OkHttpClient();

    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }


    public String ask(String input) {
        return ask(Model.GPT_3_5_TURBO.getName(), DEFAULT_USER, input);
    }

    private String buildRequestBody(String model, String role, String content) {
        try {
            List<Message> message = new ArrayList<>();
            message.add(Message.builder().role(role).content(content).build());
            ChatCompletionRequestBody requestBody = ChatCompletionRequestBody.builder()
                    .model(model)
                    .messages(objectMapper.writeValueAsString(message))
                    .build();
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String ask(String model, String role, String content) {
        RequestBody body = RequestBody.create(buildRequestBody(model, role, content), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Constant.CHAT_COMPLETION_API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                log.error("Request failed: {}, please try again", response.body().string());
            } else {
                return response.body().toString();
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
