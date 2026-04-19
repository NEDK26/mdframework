package it.pkg.common.context;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    // 存入当前请求的用户 ID（由拦截器调用）
    public static void set(Long userId) {
        USER_ID.set(userId);
    }

    // 在 Service/Controller 中随时获取当前登录用户 ID
    public static Long get() {
        return USER_ID.get();
    }

    // 请求结束后清除，防止线程池复用时数据污染（由拦截器 afterCompletion 调用）
    public static void remove() {
        USER_ID.remove();
    }
}
