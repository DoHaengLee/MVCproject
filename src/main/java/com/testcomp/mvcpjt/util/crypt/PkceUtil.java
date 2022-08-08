package com.testcomp.mvcpjt.util.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64; 							//java1.8���� ������
//import org.apache.commons.codec.binary.Base64;	//java1.7��



public class PkceUtil {
	/* ���� */
	// ����

	/* ������ */
	// �⺻ - ����

	/* �Լ� */
	// code verifier ����
    public String generateCodeVerifier() {
    	String result = "";
    	try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] codeVerifier = new byte[32];
            secureRandom.nextBytes(codeVerifier);
            result = Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
            //result = Base64.encodeBase64URLSafeString(codeVerifier);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }

    // code verifier�� �̿��� code challenge ����
    public String generateCodeChallange(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        //return Base64.encodeBase64URLSafeString(digest);
    }
}