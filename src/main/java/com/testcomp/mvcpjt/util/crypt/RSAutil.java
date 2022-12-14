package com.testcomp.mvcpjt.util.crypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

import com.testcomp.mvcpjt.util.ConfigUtil;
import com.testcomp.mvcpjt.util.FileUtil;



public class RSAutil {
	/* 변수 */
	// mvc.properties > ConfigProp > ConfigUtil
	public static String privfile = ConfigUtil.RSAprivfile;
	public static String pubfile = ConfigUtil.RSApubfile;
	public static String alg = ConfigUtil.RSAalg;
	private static FileUtil fUtil = new FileUtil();

	/* 생성자 */
	// 기본 - 생략

	/* 함수 */
	// 키페어 생성
	// keyPair.getPublic().getFormat() 등으로 확인해보면 public은 PKCS8, private는 X509
	public KeyPair genKeyPair() {
		KeyPair keyPair = null;
		try {
	        SecureRandom secureRandom = new SecureRandom();
	        KeyPairGenerator gen = KeyPairGenerator.getInstance(alg);
	        gen.initialize(2048, secureRandom);
	        keyPair = gen.genKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return keyPair;
    }
	
	// 공개키로 암호화
    public String encPub(String plainText, PublicKey publicKey) {
    	String result = "";
    	try {
            Cipher cipher = Cipher.getInstance(alg);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytePlain = cipher.doFinal(plainText.getBytes("utf-8"));
            result = Base64.getEncoder().encodeToString(bytePlain);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }
    // 개인키로 복호화
    public String decPriv(String encrypted, PrivateKey privateKey) {
    	String result = "";
    	try {
    		Cipher cipher = Cipher.getInstance(alg);
            byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes("utf-8"));
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytePlain = cipher.doFinal(byteEncrypted);
            result = new String(bytePlain, "utf-8");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return result;
    }
    
    ///// base64 string에서 key 획득 /////
    // 전처리
    private byte[] pretreatment(String string) throws Exception {
    	String plainstr = string.replaceAll("\\n",  "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
    	byte[] keybyte = Base64.getDecoder().decode(plainstr);
        return keybyte;
    }
    // byte에서 개인키 획득
    private PrivateKey getPrivFromByte(byte[] bytePriv) throws Exception {
       PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(bytePriv);
       KeyFactory keyFactory = KeyFactory.getInstance(alg);
       return keyFactory.generatePrivate(keySpecPKCS8);
    }
    // byte에서 공개키 획득
    private PublicKey getPubFromByte(byte[] bytePub) throws Exception {
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(bytePub);
        KeyFactory keyFactory = KeyFactory.getInstance(alg);
        return keyFactory.generatePublic(keySpecX509);
    }
    // base64로 키페어 인코딩
    public Map<String,String> getBase64keys(KeyPair kp) {
    	Map<String, String> result = new HashMap<String,String>();
    	try {
        	byte[] bytePub = kp.getPublic().getEncoded();
        	byte[] bytePriv = kp.getPrivate().getEncoded();
            String base64Pub = Base64.getEncoder().encodeToString(bytePub);
            String base64Priv = Base64.getEncoder().encodeToString(bytePriv);
            result.put("public", base64Pub);
        	result.put("private", base64Priv);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
    // base64에서 키페어 획득
    public Map<String,Object> getOriginkeys(Map<String,String> base64keys) {
    	Map<String, Object> result = new HashMap<String,Object>();
    	try {
        	PrivateKey priv = getPrivFromByte(pretreatment(base64keys.get("private").toString()));
        	PublicKey pub = getPubFromByte(pretreatment(base64keys.get("public").toString()));
        	result.put("private", priv);
        	result.put("public", pub);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
    
    ////////// 파일 읽기/쓰기 //////////
    // 각 파일에 키 저장
 	public void storeKeys (KeyPair kp) {
		Key pub = kp.getPublic();
 		Key priv = kp.getPrivate();
 		fUtil.createFile(privfile,priv.getEncoded());
 		fUtil.createFile(pubfile,pub.getEncoded());
 	}
 	// 각 파일 읽고 키페어 획득
 	public Map<String,Object> getByteKeyFromFile() {
 		Map<String,Object> result = new HashMap<String,Object>();
		byte[] bytePub = fUtil.readByteFile(pubfile);
		byte[] bytePriv = fUtil.readByteFile(privfile);
		if(bytePub!=null && bytePriv!=null) {
			try {
	 			result.put("public", getPubFromByte(bytePub));
	 			result.put("private", getPrivFromByte(bytePriv));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 		return result;
 	}
}