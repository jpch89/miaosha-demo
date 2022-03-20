package tech.tuanzi.miaosha.common;

import tech.tuanzi.miaosha.entity.User;

/**
 * 用户上下文
 *
 * @author Patrick Ji
 */
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
