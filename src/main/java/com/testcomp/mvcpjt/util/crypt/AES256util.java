package com.testcomp.mvcpjt.util.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.security.InvalidAlgorithmParameterException;




public class AES256util {
    
	// 랜덤UUID에서 -만 뺀 값
	private static String secret = "dd3b996548a446f0a5ee7d0291c5d7d6";
	private static String iv = secret.substring(0,16);
	private static String alg = "AES/CBC/PKCS5Padding";

	
    //암호화
    public static String encode(String str) throws Exception {
    	String enc = "";
    	Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes("utf-8"), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        byte[] encrypted = cipher.doFinal(str.getBytes("utf-8"));
        enc = Base64.getEncoder().encodeToString(encrypted);
        return enc;
    }
    
    //복호화
    public static String decode(String str) throws Exception {
    	Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes("utf-8"), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes("utf-8"));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(str);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "utf-8");
    }
}
