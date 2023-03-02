package com.lilittlecat.chatgpt.offical.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * Response body for ChatGPT API.
 * </p>
 * see: <a href="https://platform.openai.com/docs/api-reference/chat">https://platform.openai.com/docs/api-reference/chat</a>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Data
@Builder
public class ChatCompletionResponseBody {

    @JsonProperty(value = "id")
    public String id;
    @JsonProperty(value = "object")
    public String object;
    @JsonProperty(value = "created")
    public int created;
    @JsonProperty(value = "choices")
    public List<Choice> choices;
    @JsonProperty(value = "usage")
    public Usage usage;

    @Data
    public static class Choice {
        @JsonProperty(value = "index")
        public Integer index;
        @JsonProperty(value = "message")
        public List<Message> message;
        @JsonProperty(value = "finish_reason")
        public String finishReason;
    }


    @Data
    public static class Usage {
        @JsonProperty(value = "prompt_tokens")
        public Integer promptTokens;
        @JsonProperty(value = "completion_tokens")
        public Integer completionTokens;
        @JsonProperty(value = "total_tokens")
        public Integer totalTokens;
    }
}
