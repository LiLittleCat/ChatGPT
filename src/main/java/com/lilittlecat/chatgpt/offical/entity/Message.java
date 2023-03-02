package com.lilittlecat.chatgpt.offical.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <p>
 * Message
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @JsonProperty(value = "role")
    public String role;
    @JsonProperty(value = "content")
    public String content;

}
