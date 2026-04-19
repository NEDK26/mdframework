#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.utils;

import ${package}.common.constant.SystemConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成 token 并存入 Redis
     * 登录成功后调用，把 token 返回给前端
     *
     * @param userId 登录用户的 ID
     * @return token 字符串，前端放入请求头 "Authorization: Bearer {token}"
     */
    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String redisKey = SystemConstants.TOKEN_REDIS_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, userId, SystemConstants.TOKEN_TTL_SECONDS, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 根据 token 获取 userId
     * 返回 null 表示 token 不存在或已过期
     *
     * @param token 前端传来的原始 token（不含 "Bearer " 前缀）
     */
    public Long getUserId(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        String redisKey = SystemConstants.TOKEN_REDIS_PREFIX + token;
        Object value = redisTemplate.opsForValue().get(redisKey);
        if (value == null) {
            return null;
        }
        // Redis 存的是 Long，JSON 反序列化后可能是 Integer，统一转换
        return ((Number) value).longValue();
    }

    /**
     * 刷新 token 过期时间（滑动过期）
     * 在每次请求通过拦截器时调用，活跃用户不会掉线
     *
     * @param token 前端传来的原始 token
     */
    public void refreshToken(String token) {
        String redisKey = SystemConstants.TOKEN_REDIS_PREFIX + token;
        redisTemplate.expire(redisKey, SystemConstants.TOKEN_TTL_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 删除 token（退出登录）
     * 删除后该 token 立即失效，无法再通过拦截器
     *
     * @param token 前端传来的原始 token
     */
    public void removeToken(String token) {
        String redisKey = SystemConstants.TOKEN_REDIS_PREFIX + token;
        redisTemplate.delete(redisKey);
    }
}
