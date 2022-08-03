package com.testcomp.mvcpjt;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.testcomp.mvcpjt.util.UserUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import com.testcomp.mvcpjt.util.db.UserDTO;
import com.testcomp.mvcpjt.util.ApiUtil;
import com.testcomp.mvcpjt.util.OtpUtil;



@RestController
public class OtpController {
	
	private static final Logger logger = LoggerFactory.getLogger(OtpController.class);
	private static UserUtil uUtil = new UserUtil();
	private static ApiUtil aUtil = new ApiUtil();
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
