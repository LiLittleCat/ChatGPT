package com.lilittlecat.chatgpt.offical.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Message
 * </p>
 *
 * @author Liu Yi
 * @since 2023/3/2
 */
@Data
@Builder
public class Message {
    @JsonProperty(value = "role")
    public String role;
    @JsonProperty(value = "content")
    public String content;

}
