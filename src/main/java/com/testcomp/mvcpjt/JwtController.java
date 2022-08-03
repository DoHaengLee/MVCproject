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
	public JSONObject tokenpw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("tokenpw BEGEIN");
		JSONObject jObj =  new JSONObject(jUtil.tokenpw(request));
		logger.info("tokenpw Res : "+jObj);
		return jObj;
	}
	
	// refresh token으로 JWT 재획득 API
	@RequestMapping(value = "/tokenref", method = RequestMethod.POST)
	public JSONObject tokenref(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("tokenref BEGEIN");
		JSONObject jObj =  new JSONObject(jUtil.tokenref(request));
		logger.info("tokenref Res : "+jObj);
		return jObj;
	}
	
	// 테스트API - 헤더의 액세스토큰 읽고 맞으면 작업 수행
	@RequestMapping(value = "/testapi", method = RequestMethod.POST)
	public JSONObject testapi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("testapi BEGEIN");
		JSONObject jObj =  new JSONObject(jUtil.testapi(request));
		logger.info("testapi Res : "+jObj);
		return jObj;
	}
}
