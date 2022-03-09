package tech.tuanzi.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5 工具类
 *
 * @author Patrick Ji
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str;
        // 警惕两个 char 类型直接相加，会作为整数相加
        str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str;
        str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        // d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToFormPass("123456"));
        // b7797cce01b4b131b433b6acf4add449
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9", "1a2b3c4d"));
        // b7797cce01b4b131b433b6acf4add449
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
    }
}
