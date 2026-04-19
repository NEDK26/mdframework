#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.constant;

public final class SystemConstants {
    private SystemConstants() {}

    // 逻辑删除标志
    public static final Integer DELETED = 1;
    public static final Integer NOT_DELETED = 0;

    // 分页默认值
    public static final Long DEFAULT_PAGE_NUM = 1L;
    public static final Long DEFAULT_PAGE_SIZE = 10L;

    // HTTP 请求头相关
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // Token 相关
    public static final String TOKEN_REDIS_PREFIX = "login:token:";  // Redis key 前缀
    public static final long TOKEN_TTL_SECONDS = 7 * 24 * 60 * 60L; // 7 天，单位秒
}