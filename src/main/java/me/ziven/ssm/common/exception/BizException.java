package me.ziven.ssm.common.exception;

/**
 * Created by ziven on 2017/6/30.
 */
public class BizException extends RuntimeException {
    public enum Type {
        page,
        json
    }

    private final Type type;
    private final int code;


    public BizException(String message) {
        this(message, null, Type.json, -1);
    }

    public BizException(String message, Type type) {
        this(message, null, type, -1);
    }

    public BizException(String message, Type type, int code) {
        this(message, null, type, code);
    }

    public BizException(String message, Throwable cause, Type type, int code) {
        super(message, cause);
        this.type = type;
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "BizException{" +
                "type=" + type +
                ", code=" + code +
                '}';
    }
}
