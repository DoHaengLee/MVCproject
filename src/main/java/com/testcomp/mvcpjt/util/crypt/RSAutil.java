package com.testcomp.mvcpjt.util.crypt;

import java.io.FileOutputStream;
import java.io.OutputStream;
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

public class RSAutil {
	
	private static String keypath = "C:\\Users\\MVCpjt\\";
	private static String keyname = "testkey";
	

	// Ű��� ����
	public KeyPair genKeyPair() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048, secureRandom);
        KeyPair keyPair = gen.genKeyPair();
        return keyPair;
    }
	
	// ���Ϸ� ����
	public void storeKeys (KeyPair kp) throws Exception {
		Key pub = kp.getPublic();
		Key pvt = kp.getPrivate();
		// pub.getFormat() ������ Ȯ���غ��� public�� PKCS8, private�� X509

		String outFile = "public";
		OutputStream out;
		
		out = new FileOutputStream(keypath + keyname + ".key");
		out.write(pvt.getEncoded());
		out.close();
		 
		out = new FileOutputStream(keypath + keyname + ".pub");
		out.write(pvt.getEncoded());
		out.close();
	}

	// ����Ű�� ��ȣȭ
    public String encPub(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plainText.getBytes("utf-8"));
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
        return encrypted;
    }
    // ����Ű�� ��ȣȭ
    public String decPriv(String encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes("utf-8"));
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");
        return decrypted;
    }
    
    ///// base64 string���� key ȹ�� /////
    // ��ó��
    private byte[] pretreatment(String base64key) throws Exception {
    	String plainstr = base64key.replaceAll("\\n",  "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
    	byte[] keybyte = Base64.getDecoder().decode(plainstr);
        return keybyte;
    }
    // ����Ű ȹ��
    public PrivateKey getPrivFromBase64Str(String base64priv) throws Exception {
       PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(pretreatment(base64priv));
       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
       return keyFactory.generatePrivate(keySpecPKCS8);
    }
    //����Ű ȹ��
    public PublicKey getPubFromBase64Str(String base64pub) throws Exception {
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(pretreatment(base64pub));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpecX509);
    }
    // base64�� Ű��� ���ڵ�
    public Map<String,String> getBase64keys(KeyPair kp) throws Exception {
    	byte[] bytePub = kp.getPublic().getEncoded();
        String base64Pub = Base64.getEncoder().encodeToString(bytePub);
        byte[] bytePriv = kp.getPrivate().getEncoded();
        String base64Priv = Base64.getEncoder().encodeToString(bytePriv);
    	Map<String, String> result = new HashMap<String,String>();
    	result.put("private", base64Priv);
    	result.put("public", base64Pub);
    	return result;
    }
    // base64 string���� Ű��� ȹ��
    public Map<String,Object> getOriginkeys(Map<String,String> base64keys) throws Exception {
    	PrivateKey priv = getPrivFromBase64Str(base64keys.get("private").toString());
    	PublicKey pub = getPubFromBase64Str(base64keys.get("public").toString());
    	Map<String, Object> result = new HashMap<String,Object>();
    	result.put("private", priv);
    	result.put("public", pub);
    	return result;
    }
}