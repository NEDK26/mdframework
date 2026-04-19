package it.pkg.common.result;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 业务状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private long timestamp;


    // 私有化全参构造，强制大家通过静态方法调用
    private CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ============================ 成功响应 ============================

    /**
     * 成功：无数据返回 (例如 Delete 操作)
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功：带数据返回 (例如 Get / Post 操作)
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功：自定义提示信息并带数据 (例如 "登录成功！")
     */
    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    // ============================ 失败响应 ============================

    /**
     * 失败：指定枚举错误
     */
    public static <T> CommonResult<T> error(IResultCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败：使用默认系统错误，并自定义具体报错文案
     */
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(ResultCode.SYSTEM_ERROR.getCode(), message, null);
    }

    /**
     * 失败：完全自定义状态码和提示信息
     */
    public static <T> CommonResult<T> error(Integer code, String message) {
        return new CommonResult<>(code, message, null);
    }

}
