package com.testcomp.mvcpjt.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.db.UserDTO;

public class OtpTest {

	private static final Logger logger = LoggerFactory.getLogger(OtpTest.class);

	private static String uid = "userid1";
	private static String pwd = "password1";
	private static UserDTO dto = new UserDTO(uid, pwd);
	private static OtpUtil oUtil = new OtpUtil();

	
	@Test
	public void testOtp() {
		logger.info("*** testOtp START ***");
		
		// OTP 발급
		ArrayList<String> usrOtpList = oUtil.genOtpNupdate(dto);
		logger.info("usrOtpList : "+usrOtpList);
		
		// when - 틀린 OTP 인증 (false)
		String otp = "12345678";
		Map<String,Object> chkWrongMap = oUtil.checkOTPnUpdate(dto, otp);
		logger.info("chkWrongMap : "+chkWrongMap);
		boolean chkWrongRes = (boolean)chkWrongMap.get("result");
		logger.info("[1] chkWrongRes (Must be false) : "+chkWrongRes);
		assertEquals(false,chkWrongRes);
		
		// when - 정상 OTP 인증 (true)
		for (String typed : usrOtpList) {
            Map<String,Object> chkCorrectRes = oUtil.checkOTPnUpdate(dto, typed);
            logger.info("[2] chkCorrectRes (Must be true) : "+chkCorrectRes.get("result"));
    		assertEquals(true,chkCorrectRes.get("result"));
        }
		
		// when - 동일 OTP 인증 (false)
		for (String typed : usrOtpList) {
            Map<String,Object> chkCorrectResAgain = oUtil.checkOTPnUpdate(dto, typed);
            logger.info("[3] chkCorrectResAgain (Must be true) : "+chkCorrectResAgain.get("result"));
    		assertEquals(false,chkCorrectResAgain.get("result"));
        }
	}
}