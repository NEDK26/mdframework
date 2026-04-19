package it.pkg.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pkg.common.constant.SystemConstants;
import it.pkg.common.context.UserContext;
import it.pkg.common.result.CommonResult;
import it.pkg.common.result.ResultCode;
import it.pkg.common.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final TokenUtil tokenUtil;
    private final ObjectMapper objectMapper;  // Spring 自动注册，直接注入即可

    /**
     * 请求到达 Controller 之前执行
     * 返回 true = 放行；返回 false = 拦截，不再往后走
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头取 token，去掉 "Bearer " 前缀
        String authHeader = request.getHeader(SystemConstants.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(SystemConstants.TOKEN_PREFIX)) {
            writeUnauthorized(response);
            return false;
        }
        String token = authHeader.substring(SystemConstants.TOKEN_PREFIX.length());

        // 2. 去 Redis 校验 token，拿 userId
        Long userId = tokenUtil.getUserId(token);
        if (userId == null) {
            writeUnauthorized(response);
            return false;
        }

        // 3. 写入 UserContext，后续 Service 层可通过 UserContext.get() 获取
        UserContext.set(userId);

        // 4. 滑动过期：每次有效请求都刷新 TTL，活跃用户不会掉线
        tokenUtil.refreshToken(token);

        return true;
    }

    /**
     * 请求完成后执行（无论是否发生异常）
     * 必须清除 ThreadLocal，防止线程池复用时数据污染
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.remove();
    }

    // 统一返回 401 JSON，与 CommonResult 格式一致
    private void writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(CommonResult.error(ResultCode.UNAUTHORIZED))
        );
    }
}
