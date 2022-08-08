package com.testcomp.mvcpjt;

import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.OtpUtil;



@RestController
public class OtpController {
	
	private static final Logger logger = LoggerFactory.getLogger(OtpController.class);
	private static OtpUtil oUtil = new OtpUtil();
	

	// 로그인으로 OTP 발급 API
	@RequestMapping(value = "/otpgen", method = RequestMethod.POST)
	public JSONObject otpgen(HttpServletRequest request) throws Exception {
		logger.info("otpgen BEGEIN");
		JSONObject jObj =  new JSONObject(oUtil.otpgen(request));
		logger.info("otpgen Res : "+jObj);
		return jObj;
	}
	
	// OTP 인증 API
	@RequestMapping(value = "/otpchk", method = RequestMethod.POST)
	public JSONObject otpchk(HttpServletRequest request) throws Exception {
		logger.info("otpchk BEGEIN");
		JSONObject jObj =  new JSONObject(oUtil.otpchk(request));
		logger.info("otpchk Res : "+jObj);
		return jObj;
	}
}
