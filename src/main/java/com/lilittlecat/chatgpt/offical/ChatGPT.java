package com.lilittlecat.chatgpt.offical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilittlecat.chatgpt.offical.entity.*;
import com.lilittlecat.chatgpt.offical.exception.BizException;
import com.lilittlecat.chatgpt.offical.exception.Error;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lilittlecat.chatgpt.offical.entity.Constant.*;

/**
 * <p>
 * a Java client for ChatGPT uses official API.
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Slf4j
@Builder
public class ChatGPT {
    private final String apiKey;
    private String apiHost = DEFAULT_CHAT_COMPLETION_API_URL;
    protected OkHttpClient client;
    private ObjectMapper objectMapper = DEFAULT_OBJECT_MAPPER;

    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    public ChatGPT(String apiKey, OkHttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    public ChatGPT(String apiKey, Proxy proxy) {
        this.apiKey = apiKey;
        client = new OkHttpClient.Builder().proxy(proxy).build();
    }

    public ChatGPT(String apiKey, String proxyHost, int proxyPort) {
        this.apiKey = apiKey;
        client = new OkHttpClient.Builder().
                proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                .build();
    }

    public ChatGPT(String apiHost, String apiKey) {
        this.apiHost = apiHost;
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    public ChatGPT(String apiHost, String apiKey, OkHttpClient client, ObjectMapper objectMapper) {
        this.apiHost = apiHost;
        this.apiKey = apiKey;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public ChatGPT(String apiHost, String apiKey, Proxy proxy) {
        this.apiHost = apiHost;
        this.apiKey = apiKey;
        client = new OkHttpClient.Builder().proxy(proxy).build();
    }

    public ChatGPT(String apiHost, String apiKey, String proxyHost, int proxyPort) {
        this.apiHost = apiHost;
        this.apiKey = apiKey;
        client = new OkHttpClient.Builder().
                proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                .build();
    }


    public String ask(String input) {
        return ask(DEFAULT_MODEL.getName(), DEFAULT_USER, input);
    }

    public String ask(String user, String input) {
        return ask(DEFAULT_MODEL.getName(), user, input);
    }

    public String ask(Model model, String input) {
        return ask(model.getName(), DEFAULT_USER, input);
    }

    public String ask(List<Message> messages) {
        return ask(DEFAULT_MODEL.getName(), messages);
    }

    public String ask(Model model, List<Message> messages) {
        return ask(model.getName(), messages);
    }

    public String ask(String model, List<Message> message) {
        ChatCompletionResponseBody chatCompletionResponseBody = askOriginal(model, message);
        List<ChatCompletionResponseBody.Choice> choices = chatCompletionResponseBody.getChoices();
        StringBuilder result = new StringBuilder();
        for (ChatCompletionResponseBody.Choice choice : choices) {
            result.append(choice.getMessage().getContent());
        }
        return result.toString();
    }

    public String ask(Model model, String user, String input) {
        return ask(model.getName(), user, input);
    }

    private String buildRequestBody(String model, List<Message> messages) {
        try {
            ChatCompletionRequestBody requestBody = ChatCompletionRequestBody.builder()
                    .model(model)
                    .messages(messages)
                    .build();
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ask for response message
     *
     * @param model model
     * @param role  role
     * @param input input
     * @return ChatCompletionResponseBody
     */
    public ChatCompletionResponseBody askOriginal(String model, String role, String input) {
        return askOriginal(model, Collections.singletonList(Message.builder()
                .role(role)
                .content(input)
                .build()));
    }

    /**
     * ask for response message
     *
     * @param model    model
     * @param messages messages
     * @return ChatCompletionResponseBody
     */
    public ChatCompletionResponseBody askOriginal(String model, List<Message> messages) {
        RequestBody body = RequestBody.create(buildRequestBody(model, messages), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(apiHost)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    log.error("Request failed: {}, please try again", response.message());
                    throw new BizException(response.code(), "Request failed");
                } else {
                    log.error("Request failed: {}, please try again", response.body().string());
                    throw new BizException(response.code(), response.body().string());
                }
            } else {
                assert response.body() != null;
                String bodyString = response.body().string();
                return objectMapper.readValue(bodyString, ChatCompletionResponseBody.class);
            }
        } catch (IOException e) {
            log.error("Request failed: {}", e.getMessage());
            throw new BizException(Error.SERVER_HAD_AN_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * ask for response message
     *
     * @param model model
     * @param role role
     * @param content content
     * @return String message
     */
    public String ask(String model, String role, String content) {
        ChatCompletionResponseBody chatCompletionResponseBody = askOriginal(model, Collections.singletonList(Message.builder()
                .role(role)
                .content(content)
                .build()));
        List<ChatCompletionResponseBody.Choice> choices = chatCompletionResponseBody.getChoices();
        StringBuilder result = new StringBuilder();
        for (ChatCompletionResponseBody.Choice choice : choices) {
            result.append(choice.getMessage().getContent());
        }
        return result.toString();
    }

}
