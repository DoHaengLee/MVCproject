package com.testcomp.mvcpjt.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.db.UserDTO;



public class UserTest {

	private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
	
	// given
	private static String uid = "userid1";
	private static String pwd = "password1";
	private static UserDTO dto = new UserDTO(uid, pwd);
	private static UserUtil uUtil = new UserUtil();
	
			
	@Test
	public void testReg() throws Exception {
		logger.info("*** testReg ***");
		// when - 등록되지 않은 사용자는 등록
		Map<String,Object> regdoneObj = uUtil.regUserIfNeeded(dto);
		boolean regdone = (boolean) regdoneObj.get("result");
		logger.info("[1] regdone : "+regdone);
		
		// when - 동일 정보로 재등록 (false / "Already Registered")
		regdone = (boolean) regdoneObj.get("result");
		logger.info("[2] regdone (Must be false) : "+regdone);
		assertEquals(false,regdone);
	}
	
	@Test
	public void testPw() throws Exception {
		logger.info("*** testPw ***");
		
		// when - 틀린 패스워드
		dto.setPw(pwd+"1");
		// then - false
		logger.info("[1] ID/PW (Must be false) : "+uUtil.correctUser(dto));
        assertEquals(false,uUtil.correctUser(dto));
        
        // when - 올바른 패스워드
        dto.setPw(pwd);
		// then - true
        logger.info("[2] ID/PW (Must be true) : "+uUtil.correctUser(dto));
        assertEquals(true,uUtil.correctUser(dto));
	}
	
}
