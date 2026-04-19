package com.nedk.example.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端级别错误 (4xxx)
    PARAM_ERROR(4000, "参数校验失败"),
    UNAUTHORIZED(4001, "暂未登录或token已经过期"),
    FORBIDDEN(4003, "没有相关权限"),
    NOT_FOUND(4004, "请求资源不存在"),

    // 服务端级别错误 (5xxx)
    SYSTEM_ERROR(5000, "系统繁忙，请稍后再试"),
    OPERATE_FAILED(5001, "操作失败");


    private final Integer code;
    private final String message;
}
