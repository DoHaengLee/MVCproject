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
import java.security.InvalidAlgorithmParameterException;
import org.apache.commons.codec.binary.Base64;



public class AES256Cipher {
    
    private static byte[] secretKey; //32bit
    private static byte[] IV; //16bit
        
    public AES256Cipher(String hexKey){
        this.secretKey = hexToByteArray(hexKey.substring(0,64));
        this.IV = hexToByteArray(hexKey.substring(64, 96));
    }
    //암호화
    public static String AES256_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        byte[] keyData = secretKey;
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64URLSafeString(encrypted));
        
        return enStr;
    }
    
    //복호화
    public static String AES256_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        //byte[] keyData = secretKey.getBytes();
        byte[] keyData = secretKey;
        
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV));
        //byte[] byteStr = Base64.decodeBase64(str.getBytes());
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        
        return new String(c.doFinal(byteStr),"UTF-8");
    }
    
    public static byte[] hexToByteArray(String hex) {
        hex = hex.length()%2 != 0?"0"+hex:hex;

        byte[] b = new byte[hex.length() / 2];

        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
