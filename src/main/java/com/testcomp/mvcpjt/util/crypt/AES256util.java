package com.testcomp.mvcpjt.util.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.testcomp.mvcpjt.util.ApiUtil;
import com.testcomp.mvcpjt.util.ConfigUtil;
import com.testcomp.mvcpjt.util.db.UserDTO;



public class AES256util {
	/* 변수 */
	private static ApiUtil aUtil = new ApiUtil();
	// mvc.properties > ConfigProp > ConfigUtil
	private static String secret = ConfigUtil.AESkey;
	private static String iv = ConfigUtil.AESiv;
	private static String alg = ConfigUtil.AESalg;

	/* 생성자 */
	// 기본 - 생략
	
	/* 함수 */
    // 암호화
    public String encode(String str) {
    	String result = "";
    	try {
        	Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes("utf-8"), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes("utf-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(str.getBytes("utf-8"));
            result = Base64.getEncoder().encodeToString(encrypted);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }
    // 복호화
    public String decode(String str) {
    	String result = "";
    	try {
    		Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes("utf-8"), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes("utf-8"));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(str);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            result = new String(decrypted, "utf-8");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //////////Controller 역할 축소 //////////
    // 텍스트 받아서 암호화
    public Map<String,Object> aes256enc(HttpServletRequest request) {
    	Map<String,Object> result = new HashMap<String,Object>();
		String msg = "Unable to Encrypt";
		String enc = "";

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			try {
				JSONParser parser = new JSONParser();
				JSONObject jObj2 = (JSONObject) parser.parse(body);
				String plaintext = (String) jObj2.get("plaintext");
				enc = encode(plaintext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		if(enc!=null && !enc.equals("")) {
			result.put("result",true);
			result.put("enc", enc);
		} else {
			result.put("result",false);
			result.put("msg",msg);
		}

    	return result;
    }
    // 텍스트 받아서 복호화
    public Map<String,Object> aes256dec(HttpServletRequest request) {
    	Map<String,Object> result = new HashMap<String,Object>();
		String msg = "Unable to Decrypt";
		String dec = "";

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			try {
				JSONParser parser = new JSONParser();
				JSONObject jObj2 = (JSONObject) parser.parse(body);
				String enc = (String) jObj2.get("enctext");
				dec = decode(enc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		if(dec!=null && !dec.equals("")) {
			result.put("result",true);
			result.put("dec", dec);
		} else {
			result.put("result",false);
			result.put("msg",msg);
		}

    	return result;
    }
}
