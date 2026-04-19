package com.nedk.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登录请求参数
 * Controller 方法加 @Valid 注解后，校验失败会被 GlobalExceptionHandler 自动拦截并返回 4000 错误码
 */
@Data
public class UserLoginDTO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为 6~20 位")
    private String password;
}
