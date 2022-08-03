package com.testcomp.mvcpjt.util;

import static org.junit.Assert.*;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import com.testcomp.mvcpjt.util.db.UserDTO;



public class JwtTest {

	private static final Logger logger = LoggerFactory.getLogger(JwtTest.class);

	//given
	private static String uid = "userid1";
	private static String pwd = "password1";
	private static UserDTO dto = new UserDTO(uid, pwd);
	private static UserUtil uUtil = new UserUtil();
	// 전체토큰 - 2초만 유효 액세스토큰 - 15초만 유효, 리프레시토큰 - 30초만 유효
	private static long tokenValidMilisecond = 1000L * 2;
	private static long accessValidMilisecond = 1000L * 15;
	private static long refreshValidMilisecond = 1000L * 30;
    // 객체 생성 w/ 초기화
	private static JwtUtil jUtil = new JwtUtil(tokenValidMilisecond,accessValidMilisecond,refreshValidMilisecond);

	
	@Test
	public void testJwt() throws Exception {
		logger.info("*** testJwt START ***");

        // when
        // 신규 생성 시나리오 테스트 - DB update 전까지는 validateTokenSub로 검증
        // whole_token 생성
        String whole_token = jUtil.createWholeToken(dto);
        boolean wholeCorrect = jUtil.validateTokenSub(whole_token, dto);
        // then - whole_token 검증
        logger.info("[1] wholeCorrect (Must be true) : "+wholeCorrect);
        assertEquals(true, wholeCorrect);

        if(wholeCorrect) {        	
        	// 방법 2
            Jws<Claims> wholeClaim = jUtil.getClaims(whole_token);

        	// whole_token 만료시키기 - 만료 된 후에 파싱하면 ExpiredJwtException 오류
        	logger.info("[2] Wait 3 sec");
        	try {
        		Thread.sleep(3000l);
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        	// then - whole_token 검증
        	logger.info("[2] wholeCorrect (Must be false) : "+jUtil.validateTokenSub(whole_token, dto));
            assertEquals(false, jUtil.validateTokenSub(whole_token, dto));
            
            // 방법 1
        	Map<String,String> hbMap = jUtil.getHeaderBody(whole_token);
        	logger.info("[3] Method 1");
            logger.info("header : "+hbMap.get("header"));
            logger.info("payload : "+hbMap.get("payload"));
            
            // 방법 2에서 가져온 claim
            if (wholeClaim != null) {
                Map<String,Object> wholeMap = jUtil.getEach(wholeClaim);
                logger.info("[3] Method 2");
                logger.info("issuer : "+wholeMap.get("issuer"));
                logger.info("issuedAt : "+wholeMap.get("issuedAt"));
                logger.info("exp(whole_token) : "+wholeMap.get("expiration"));
                String access_token = wholeMap.get("access_token").toString();
                logger.info("access_token : "+access_token);
                String refresh_token = wholeMap.get("refresh_token").toString();
                logger.info("refresh_token : "+refresh_token);
                Date accExp = (Date) wholeMap.get("accexp");
                logger.info("exp(access_token) : "+accExp);
                Timestamp accExpTs = new Timestamp(accExp.getTime());
                dto.setAccexp(accExpTs);
                Date refExp = (Date) wholeMap.get("refexp");
                logger.info("exp(refresh_token) : "+refExp);
                Timestamp refExpTs = new Timestamp(refExp.getTime());
                dto.setRefexp(refExpTs);

                boolean updone = uUtil.updateUser(dto);
                // then - 사용자 업데이트 정상
                assertEquals(true,updone);
                if(updone) {
                	boolean accessCorrect = jUtil.validateTokenExp(access_token, dto);
                	// then - access_token 검증
                    logger.info("[4] accessCorrect (Must be true) : "+accessCorrect);
                    assertEquals(true, accessCorrect);

                    if(accessCorrect) {
                    	// access_token 만료시키기
                    	logger.info("[5] Wait 13 sec");
                    	try {
                    		Thread.sleep(13000l);
                    	}catch(Exception e) {
                    		e.printStackTrace();
                    	}
                    	// then - access_token 검증
                    	logger.info("[5] accessCorrect (Must be false) : "+jUtil.validateTokenExp(access_token, dto));
                        assertEquals(false, jUtil.validateTokenExp(access_token, dto));
                        
                        boolean refreshCorrect = jUtil.validateTokenExp(refresh_token, dto);
                    	// then - refresh_token 검증
                        logger.info("[6] refreshCorrect (Must be true) : "+jUtil.validateTokenExp(refresh_token, dto));
                        assertEquals(true, jUtil.validateTokenExp(refresh_token, dto));

                        if(refreshCorrect) {
                        	
                        	String whole_tokenJic = jUtil.createWholeToken(dto);
                        	try {
                        		Thread.sleep(1000l);
                        	} catch(Exception e) {
                        		e.printStackTrace();
                        	}
                        	
                        	String whole_token2 = jUtil.createWholeToken(dto);
                        	Jws<Claims> wholeClaim2 = jUtil.getClaims(whole_token2);
                        	Map<String,Object> wholeMap2 = jUtil.getEach(wholeClaim2);
                        	Date accExp2 = (Date) wholeMap2.get("accexp");
                        	Timestamp accExpTs2 = new Timestamp(accExp2.getTime());
                            dto.setAccexp(accExpTs2);
                            Date refExp2 = (Date) wholeMap2.get("refexp");
                            Timestamp refExpTs2 = new Timestamp(refExp2.getTime());
                            dto.setRefexp(refExpTs2);
                            boolean updone2 = uUtil.updateUser(dto);
                            
                            logger.info("[7] reUseToken (Must be false) : "+jUtil.validateTokenExp(whole_tokenJic, dto));
                            assertEquals(false, jUtil.validateTokenExp(whole_tokenJic, dto));
                            
                            String access_token2 = wholeMap2.get("access_token").toString();
                            logger.info("[8] accessCorrect (Must be true) : "+jUtil.validateTokenExp(access_token2, dto));
                            assertEquals(true, jUtil.validateTokenExp(access_token2, dto));
                            
                            String refresh_token2 = wholeMap2.get("refresh_token").toString();
                            logger.info("[9] refreshCorrect (Must be true) : "+jUtil.validateTokenExp(refresh_token2, dto));
                            assertEquals(true, jUtil.validateTokenExp(refresh_token2, dto));
                            
                            // refresh_token 만료시키기
                        	logger.info("[10] Wait 31 sec");
                            try {
                            	Thread.sleep(31000l);
                            } catch(Exception e) {
                            	e.printStackTrace();
                            }
                            logger.info("[10] refreshCorrect (Must be false): "+jUtil.validateTokenExp(refresh_token2, dto));
                            assertEquals(false, jUtil.validateTokenExp(refresh_token2, dto));
                        }
                        
                    }
                }
            }
        }
        logger.info("");
	}

}
