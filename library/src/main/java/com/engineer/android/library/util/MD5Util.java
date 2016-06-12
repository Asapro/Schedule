package com.engineer.android.library.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Created by L.J on 2016/4/20.
 */
public class MD5Util {
    private static final char[] HEX = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    public static String makeMd5(String source){
        String target = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(source.getBytes());
            StringBuilder md5Builder = new StringBuilder();
            for(byte b : bytes){
                md5Builder.append(HEX[b >>> 4 & 0xF]).append(HEX[b & 0xF]);
            }
            target = md5Builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return target;
    }
}
