package com.lilittlecat.chatgpt.offical.exception;

/**
 * <p>
 * BizException
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
public class BizException extends RuntimeException {

    private final Integer code;

    private final String msg;

    public BizException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(Error error) {
        super(error.getMsg());
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

}
