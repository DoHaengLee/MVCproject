package com.testcomp.mvcpjt.util.crypt;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
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

import com.testcomp.mvcpjt.util.FileUtil;

public class RSAutil {
	
	// pub.getFormat() ������ Ȯ���غ��� public�� PKCS8, private�� X509
	private static String keyname = "testkey";
	private static String privfile = keyname+"_private.key";
	private static String pubfile = keyname+"_public.key";
	private static String alg = "RSA";
	private static FileUtil fUtil = new FileUtil();
	

	// Ű��� ����
	public KeyPair genKeyPair() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048, secureRandom);
        KeyPair keyPair = gen.genKeyPair();
        return keyPair;
    }
	
	// ����Ű�� ��ȣȭ
    public String encPub(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plainText.getBytes("utf-8"));
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
        return encrypted;
    }
    // ����Ű�� ��ȣȭ
    public String decPriv(String encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes("utf-8"));
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");
        return decrypted;
    }
    
    ///// base64 string���� key ȹ�� /////
    // ��ó��
    private byte[] pretreatment(String string) throws Exception {
    	String plainstr = string.replaceAll("\\n",  "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
    	byte[] keybyte = Base64.getDecoder().decode(plainstr);
        return keybyte;
    }
    // byte���� ����Ű ȹ��
    private PrivateKey getPrivFromByte(byte[] bytePriv) throws Exception {
       PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(bytePriv);
       KeyFactory keyFactory = KeyFactory.getInstance(alg);
       return keyFactory.generatePrivate(keySpecPKCS8);
    }
    //����Ű ȹ��
    private PublicKey getPubFromByte(byte[] bytePub) throws Exception {
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(bytePub);
        KeyFactory keyFactory = KeyFactory.getInstance(alg);
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
    	PrivateKey priv = getPrivFromByte(pretreatment(base64keys.get("private").toString()));
    	PublicKey pub = getPubFromByte(pretreatment(base64keys.get("public").toString()));
    	Map<String, Object> result = new HashMap<String,Object>();
    	result.put("private", priv);
    	result.put("public", pub);
    	return result;
    }
    
    ////////// ���� ����/�б� //////////
    // ����
 	public void storeKeys (KeyPair kp) throws Exception {
		Key pub = kp.getPublic();
 		Key priv = kp.getPrivate();
 		
 		FileOutputStream out;
		
 		fUtil.createFile(privfile);
		out = new FileOutputStream(privfile);
		out.write(priv.getEncoded());
		out.close();
		
		fUtil.createFile(pubfile);
		out = new FileOutputStream(pubfile);
		out.write(pub.getEncoded());
		out.close();	
 	}
 	// ���� �а� Ű ȹ��
 	public Map<String,Object> getByteKeyFromFile() throws Exception {
 		Map<String,Object> result = new HashMap<String,Object>();
 		try {
 			byte[] bytePub = fUtil.readByteFile(pubfile);
 			byte[] bytePriv = fUtil.readByteFile(privfile);
 			result.put("public", getPubFromByte(bytePub));
 			result.put("private", getPrivFromByte(bytePriv));
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return result;
 	}
}