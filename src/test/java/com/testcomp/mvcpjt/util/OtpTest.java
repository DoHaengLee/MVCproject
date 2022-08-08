package com.testcomp.mvcpjt.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.db.UserDTO;



public class OtpTest {

	private static final Logger logger = LoggerFactory.getLogger(OtpTest.class);

	private static String uid = "userid1";
	private static String pwd = "password1";
	private static UserDTO dto = new UserDTO(uid, pwd);
	private static OtpUtil oUtil = new OtpUtil();
	private static UserUtil uUtil = new UserUtil();
	
	
	@Test
	public void testOtp() {
		logger.info("*** testOtp START ***");

		// when
		// ����� ��� (otp seed�� �����ص�)
		Map<String,Object> regMap = uUtil.regUserIfNeeded(dto);
		if(!(boolean)regMap.get("result")) {
			assertEquals("Already Registered", (String)regMap.get("msg"));
		} else {
			assertEquals(true, (boolean)regMap.get("result"));
		}
		logger.info("[0] REG DONE");
		
		// OTP �߱�
		List<String> usrOtpList = oUtil.genOtpNupdate(dto);
		logger.info("usrOtpList : "+usrOtpList);
		
		// when - Ʋ�� OTP ���� (false)
		String otp = "12345678";
		Map<String,Object> chkWrongMap = oUtil.checkOTPnUpdate(dto, otp);
		logger.info("chkWrongMap : "+chkWrongMap);
		boolean chkWrongRes = (boolean)chkWrongMap.get("result");
		logger.info("[1] chkWrongRes (Must be false) : "+chkWrongRes);
		assertEquals(false,chkWrongRes);
		
		// when - ���� OTP ���� (true)
		for (String typed : usrOtpList) {
            Map<String,Object> chkCorrectRes = oUtil.checkOTPnUpdate(dto, typed);
            logger.info("[2] chkCorrectRes (Must be true) : "+chkCorrectRes.get("result"));
    		assertEquals(true,chkCorrectRes.get("result"));
        }
		
		// when - ���� OTP ���� (false)
		for (String typed : usrOtpList) {
            Map<String,Object> chkCorrectResAgain = oUtil.checkOTPnUpdate(dto, typed);
            logger.info("[3] chkCorrectResAgain (Must be false) : "+chkCorrectResAgain.get("result"));
    		assertEquals(false,chkCorrectResAgain.get("result"));
        }

	}
}