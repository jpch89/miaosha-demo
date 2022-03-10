package tech.tuanzi.miaosha.utils;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author Patrick Ji
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}