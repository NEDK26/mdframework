package it.pkg.common.exception;

import it.pkg.common.result.CommonResult;
import it.pkg.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 1. 拦截我们自定义的业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<?> handleBusinessException(BusinessException e) {
        // 业务级别的异常通常是符合预期的，打印 Warn 即可
        log.warn("业务逻辑异常: 错误码[{}], 错误信息[{}]", e.getCode(), e.getMessage());
        return CommonResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 2. 拦截 Spring Validation 的参数校验异常（非常实用）
     * 比如传过来的 DTO 某个字段为空，直接拦截并返回友好的提示
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public CommonResult<?> handleValidationException(Exception e) {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        }

        String errorMsg = "参数校验异常";
        if (bindingResult != null && bindingResult.hasErrors()) {
            // 获取验证失败的第一个错误提示文案
            errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        log.warn("参数校验未通过: {}", errorMsg);
        // 使用参数错误的枚举码 4000
        return CommonResult.error(ResultCode.PARAM_ERROR.getCode(), errorMsg);
    }

    /**
     * 3. 兜底拦截其他所有未知的异常（如 NullPointerException, SQLException 等）
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleException(Exception e) {
        // 未知异常属于系统级错误，必须打印 Error 堆栈，方便去日志系统报警和排查
        log.error("系统内部未捕获异常: ", e);
        // 对外统一口径，不要暴露数据库字段或代码行号
        return CommonResult.error(ResultCode.SYSTEM_ERROR);
    }
}
