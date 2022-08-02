package com.testcomp.mvcpjt;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.testcomp.mvcpjt.util.UserUtil;
import com.testcomp.mvcpjt.util.db.UserDTO;
import com.testcomp.mvcpjt.util.ApiUtil;
import com.testcomp.mvcpjt.util.JwtUtil;



@RestController
public class JwtController {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtController.class);
	private static UserUtil uUtil = new UserUtil();
	private static JwtUtil jUtil = new JwtUtil();
	private static ApiUtil aUtil = new ApiUtil();

	
	// 로그인으로 JWT 획득 API
	@RequestMapping(value = "/tokenpw", method = RequestMethod.POST)
	public JSONObject tokenpw(HttpServletRequest request, HttpServletResponse response) {
		logger.info("tokenpw BEGEIN");
		JSONObject jObj = new JSONObject();
		boolean result = false;
		String msg = "";

		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			try {
				String body = jic.get("body").toString();
				UserDTO uDTO = uUtil.getUserFromStr(body);
				if(uUtil.correctUser(uDTO)) {
					//Map<String,Object> jMap = jUtil.createNupdate(uDTO);       //whole_token까지 jwt화
					Map<String,Object> jMap = jUtil.createWOwholeNupdate(uDTO);
					if((boolean)jMap.get("result")) {
						result = true;
						//jObj.put("whole_token", jMap.get("whole_token").toString());
						Map<String,String> wholeMap = (Map<String,String>)jMap.get("whole_token");
						jObj.put("token_info", new JSONObject(wholeMap));
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
		logger.info("tokenpw Res : "+jObj);
		return jObj;
	}
	
	// refresh token으로 JWT 재획득 API
	@RequestMapping(value = "/tokenref", method = RequestMethod.POST)
	public JSONObject tokenref(HttpServletRequest request, HttpServletResponse response) {
		logger.info("tokenref BEGEIN");
		JSONObject jObj = new JSONObject();
		boolean result = false;
		String msg = "";

		// 헤더의 토큰만 체크 - body 읽지 않고
		logger.info("Before chkHeader");
		Map<String,Object> jic = aUtil.chkHeaderToken(request, "refresh_token");
		logger.info("chkHeader : "+jic);
		if((boolean)jic.get("result")) {
			try {
				UserDTO uDTO = new UserDTO();
				uDTO.setId(jic.get("userid").toString());

				//Map<String,Object> jMap = jUtil.createNupdate(uDTO);       //whole_token까지 jwt화
				Map<String,Object> jMap = jUtil.createWOwholeNupdate(uDTO);
				if((boolean)jMap.get("result")) {
					result = true;
					//jObj.put("whole_token", jMap.get("whole_token").toString());
					Map<String,String> wholeMap = (Map<String,String>)jMap.get("whole_token");
					jObj.put("token_info", new JSONObject(wholeMap));
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
		logger.info("tokenref Res : "+jObj);
		return jObj;
	}
	
	// 테스트API - 헤더의 액세스토큰 읽고 맞으면 작업 수행
	@RequestMapping(value = "/testapi", method = RequestMethod.POST)
	public JSONObject testapi(HttpServletRequest request, HttpServletResponse response) {
		logger.info("testapi BEGEIN");
		JSONObject jObj = new JSONObject();
		boolean result = false;
		String msg = "";

		// 헤더의 토큰까지 체크
		Map<String,Object> jic = aUtil.readReq(request,"access_token");
		if((boolean)jic.get("result")) {
			result = true;
			jObj.put("body", "correct");
		} else {
			msg = jic.get("msg").toString();
		}
		
		jObj.put("result", result);
		if(!result) {
			jObj.put("msg", msg);
		}
		logger.info("testapi Res : "+jObj);
		return jObj;
	}
}
