package com.wpp.devtools.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: volvo-server
 * @description:
 * @author: wpp
 * @create: 2020-07-07
 **/
public class MD5Util {

    /**
     * MD5加密字符串（32位大写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（大写）
     */
    public static String md5Encrypt32Upper(String string) {
        byte[] hash;
        try {
            //创建一个MD5算法对象，并获得MD5字节数组,16*8=128位
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        //转换为十六进制字符串
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }

    /**
     * MD5加密字符串（32位小写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（小写）
     */
    public static String md5Encrypt32Lower(String string) {
        //直接上面的方法转换成小写就可以了
        return md5Encrypt32Upper(string).toLowerCase();
    }

    /**
     * 将二进制字节数组转换为十六进制字符串
     *
     * @param bytes 二进制字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    /**
     * Unicode中文编码转换成字符串
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

}