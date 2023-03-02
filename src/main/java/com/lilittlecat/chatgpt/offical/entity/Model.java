package com.lilittlecat.chatgpt.offical.entity;

import lombok.Getter;

/**
 * <p>
 * Models of completion
 * </p>
 * see: <a href=https://platform.openai.com/docs/api-reference/models>https://platform.openai.com/docs/api-reference/models</a>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Getter
public enum Model {

    GPT_3_5_TURBO("gpt-3.5-turbo"),
    TEXT_DAVINCI_003("text-davinci-003"),
    TEXT_DAVINCI_002("text-davinci-002"),
    TEXT_DAVINCI_001("text-davinci-001"),

    ;

    private final String name;

    Model(String name) {
        this.name = name;
    }
}
