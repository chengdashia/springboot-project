package com.onlineExam.utils.safe;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @author 成大事
 * @date 2022/2/17 17:31
 */
public class SecureDESUtil {
    /**
     * 注意：通过方法 SecureDESUtil.generateSecretKey() 生产一个秘钥Key
     */
    private static final String secretKey = "bigDataStudio666666";

    /**
     * 生成秘钥Key
     */
    public static String generateSecretKey() {
        byte[] keyBytes = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
        return new BASE64Encoder().encodeBuffer(keyBytes);
    }

    /**
     * 获取秘钥Key的byte[]数组
     */
    private static byte[] getKeyBytes() {
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = new BASE64Decoder().decodeBuffer(secretKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyBytes;
    }

    /**
     * 加密字符串
     */
    public static String encrypt(String info) {
        DES des = SecureUtil.des(getKeyBytes());
        return des.encryptHex(info);
    }

    /**
     * 解密字符串
     */
    public static String decrypt(String encrypt) {
        DES des = SecureUtil.des(getKeyBytes());
        return des.decryptStr(encrypt);
    }





}
