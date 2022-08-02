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
	public JSONObject otpgen(HttpServletRequest request, HttpServletResponse response) {
		logger.info("otpgen BEGEIN");
		JSONObject jObj = new JSONObject();
		boolean result = false;
		String msg = "";

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			try {
				String body = jic.get("body").toString();
				UserDTO uDTO = uUtil.getUserFromStr(body);
				if(uUtil.correctUser(uDTO)) {
					ArrayList<String> usrOtpList = oUtil.genOtpNupdate(uDTO);
					result = true;
					jObj.put("otp_info", usrOtpList);
				} else {
					msg = "Wrong PW / Unregistered User";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		jObj.put("result", result);
		if(!result) {
			jObj.put("msg", msg);
		}
		logger.info("otpgen Res : "+jObj);
		return jObj;
	}
	
	// OTP 인증 API
	@RequestMapping(value = "/otpchk", method = RequestMethod.POST)
	public JSONObject otpchk(HttpServletRequest request, HttpServletResponse response) {
		logger.info("otpchk BEGEIN");
		JSONObject jObj = new JSONObject();
		boolean result = false;
		String msg = "";

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			try {
				String body = jic.get("body").toString();
				UserDTO uDTO = uUtil.getUserFromStr(body);
				if(uUtil.correctUser(uDTO)) {
					
					JSONParser parser = new JSONParser();
					JSONObject jObj2 = (JSONObject) parser.parse(body);
					String typed = (String) jObj2.get("otp");
					
					Map<String,Object> chkCorrectRes = oUtil.checkOTPnUpdate(uDTO, typed);
					result = (boolean) chkCorrectRes.get("result");
					if(!result) {
						msg = chkCorrectRes.get("msg").toString();
					}
				} else {
					msg = "Wrong PW / Unregistered User";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		jObj.put("result", result);
		if(!result) {
			jObj.put("msg", msg);
		}
		logger.info("otpchk Res : "+jObj);
		return jObj;
	}
}
