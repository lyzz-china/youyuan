package com.abi.eb.common.util;


import com.youyuan.common.util.StringUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class EncryptUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
    private static final String DEFAULT_SALT = "xmsbuy";
    private static final String DEFAULT_AES_KEY = "xms-aes-key";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/NoPadding";//AES加密算法及填充方式

    public static String md5Encrypt(String source) {
        return DigestUtils.sha1DigestAsHex(source);
    }

    public static String md5Encrypt(String source, String charset) {
        if (charset == null || "".equals(charset)) {
            return "";
        }
        try {
            return DigestUtils.sha1DigestAsHex(String.valueOf(source.getBytes(charset)));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
        return "";
    }

    public static String md5EncryptWithSalt(String source) {
        return md5EncryptWithSalt(source, DEFAULT_SALT);
    }

    public static String md5EncryptWithSalt(String source, String salt) {
        return DigestUtils.sha1DigestAsHex(saltOriginalContent(source, salt));
    }

    public static String sha1Encrypt(String source) {
        return DigestUtils.sha1DigestAsHex(source);
    }

    public static String sha1EncryptWithSalt(String source) {
        return sha1EncryptWithSalt(source, DEFAULT_SALT);
    }

    public static String sha1EncryptWithSalt(String source, String salt) {
        return DigestUtils.sha1DigestAsHex(saltOriginalContent(source, salt));
    }

    public static String base64Encode(String source) {
        return Base64.encodeBase64String(source.getBytes());
    }

    public static String base64EncodeUrlSafe(String source) {
        return Base64.encodeBase64URLSafeString(source.getBytes());
    }

    public static String base64Decode(String base64Str) {
        try {
            return new String(Base64.decodeBase64(base64Str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("EncryptUtil.base64Decode exception occurs.", e);
        }
        return "";
    }

    private static String saltOriginalContent(String srcStr, String salt) {
        return srcStr + salt;
    }


    public static String aesCbcEncrypt(String data, String keyStr) {
        try{
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            String key = StringUtil.isEmpty(keyStr) ? DEFAULT_AES_KEY : keyStr;
            key = md5Encrypt(key).toLowerCase().substring(0,16);
            String iv = md5Encrypt(key).toLowerCase().substring(0,16);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new Base64().encodeToString(encrypted);
        }catch (Exception e) {
            LOGGER.warn("EncryptUtil.aesCbcEncrypt exception occurs.", e);
        }
        return null;
    }

    public static String aesCbcDecrypt(String message, String keyStr) {
        try{
            byte[] encrypted = new Base64().decode(message);
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            String key = StringUtil.isEmpty(keyStr) ? DEFAULT_AES_KEY : keyStr;
            key = md5Encrypt(key).toLowerCase().substring(0,16);
            String iv = md5Encrypt(key).toLowerCase().substring(0,16);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original,"UTF-8");
            //移除ZeroPadding中添加的\u0000字符
            int sections = originalString.length()%16;
            int strLength = originalString.length();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < strLength; i++) {
                if(i%16 < (sections-1)){
                    sb.append(originalString.charAt(i));
                }else{
                    if(originalString.charAt(i) != '\u0000'){
                        sb.append(originalString.charAt(i));
                    }
                }
            }
            return sb.toString();
        }catch (Exception e) {
            LOGGER.warn("EncryptUtil.aesCbcDecrypt exception occurs.", e);
        }
        return null;
    }
    public static String PBKDF2Encrypt(String source, String salt) {
        int HASH_SIZE = 32;// 生成密文的长度
        int PBKDF2_ITERATIONS = 1000;// 迭代次数
        try {
            byte[] bytes = salt.getBytes();//DatatypeConverter.parseBase64Binary(salt);
            KeySpec spec = new PBEKeySpec(source.toCharArray(), bytes, PBKDF2_ITERATIONS, HASH_SIZE * 4);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = new byte[0];
            hash = secretKeyFactory.generateSecret(spec).getEncoded();
            return DatatypeConverter.printHexBinary(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOGGER.warn("EncryptUtil.PBKDF2Encrypt exception occurs.", e);
        }
        return "";
    }

    public static void main(String... arg) {
//        String src = "test";
//        String keystr = "spider-extra-aes";
//        String s1 = aesCbcEncrypt(src, keystr);
//        String s2 = aesCbcDecrypt("/wzGfHPhP7MlS6IGb4nGxw==", keystr);
//        System.out.println(s1);
//        System.out.println(s2.length());

        String pwd = "a123456";
        String salt = "Hj0vWrGzSPtoT4js";
        //String v1 = "4C22A7FD1378F5D1DF5E8C1A86786248D46F89F7";
        String v2 = sha1EncryptWithSalt(pwd, salt);
        System.out.println(v2.toUpperCase());
        //System.out.println(v1.equalsIgnoreCase(v2));

    }
}
