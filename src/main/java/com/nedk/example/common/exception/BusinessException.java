package com.nedk.example.common.exception;

import com.nedk.example.common.core.IResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 接收自定义的 code 和 message
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 接收我们之前写的错误码枚举
     */
    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 接收枚举，并允许覆盖默认的错误信息（更灵活）
     */
    public BusinessException(IResultCode resultCode, String customMessage) {
        super(customMessage);
        this.code = resultCode.getCode();
    }

}
