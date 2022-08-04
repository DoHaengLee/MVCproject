package com.testcomp.mvcpjt.util;

import static org.junit.Assert.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.crypt.RSAutil;
import com.testcomp.mvcpjt.util.crypt.AES256util;



public class CryptTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptTest.class);
	private RSAutil rUtil = new RSAutil();
	private AES256util aes256Util = new AES256util();
	private String plainText = "암호화 할 스트링";
	

	@Test
	public void testRSA() throws Exception {
		logger.info("[0] plainText : "+plainText);
		
		logger.info("*** testRSA ***");
		KeyPair kp = rUtil.genKeyPair();
		String encText = rUtil.encPub(plainText, kp.getPublic());
		logger.info("[1] encText : "+encText);
		String decText = rUtil.decPriv(encText, kp.getPrivate());
		logger.info("[2] decText : "+decText);
		assertEquals(plainText,decText);
		
		Map<String,String> base64Map = rUtil.getBase64keys(kp);
		Map<String,Object> reKpMap = rUtil.getOriginkeys(base64Map);
		String reEnc = rUtil.encPub(plainText, (PublicKey)reKpMap.get("public"));
		logger.info("[3] reEnc : "+reEnc);
		//assertEquals(encText,reEnc); //암호화 했을 때 값이 같지는 않음.
		String reDec = rUtil.decPriv(reEnc, (PrivateKey)reKpMap.get("private"));
		logger.info("[4] reDec : "+reDec);
		assertEquals(decText,reDec);


		rUtil.storeKeys(kp);
		//rUtil.storeBase64Keys(base64Map);
		Map<String,Object> fileKeyMap = rUtil.getByteKeyFromFile();
		
		String reEncFile = rUtil.encPub(plainText, (PublicKey)fileKeyMap.get("public"));
		String reDecFile = rUtil.decPriv(reEncFile, (PrivateKey)fileKeyMap.get("private"));
		logger.info("[5] reDecFile : "+reDecFile);
		assertEquals(decText,reDecFile);
	}
	
	@Test
	public void testAES256() throws Exception {
		logger.info("*** testAES256 ***");
		String encText = aes256Util.encode(plainText);
		logger.info("[1] encText : "+encText);
		String decText = aes256Util.decode(encText);
		logger.info("[2] decText : "+decText);
		assertEquals(plainText,decText);
	}

}
