package com.testcomp.mvcpjt.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64; 							//java1.8부터 가능함
//import org.apache.commons.codec.binary.Base64;	//java1.7용



public class PkceUtil {

    public String generateCodeVerifier() throws UnsupportedEncodingException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
        //return Base64.encodeBase64URLSafeString(codeVerifier);
    }

    public String generateCodeChallange(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();

        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        //return Base64.encodeBase64URLSafeString(digest);
    }
}