package com.lilittlecat.chatgpt.offical.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * a proxy
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Data
@Builder
@Accessors(chain = true)
public class Proxy {
    private String proxy;
    private Integer port;
    private String username;
    private String password;
}
