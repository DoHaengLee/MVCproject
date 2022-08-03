package com.testcomp.mvcpjt;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.ApiUtil;
import com.testcomp.mvcpjt.util.UserUtil;
import com.testcomp.mvcpjt.util.db.UserDTO;



@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static UserUtil uUtil = new UserUtil();
	private static ApiUtil aUtil = new ApiUtil();
	
	// 사용자 등록 API
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public JSONObject signup(HttpServletRequest request) throws Exception {
		logger.info("signup BEGEIN");
		JSONObject jObj =  new JSONObject(uUtil.signup(request));
		logger.info("signup Res : "+jObj);
		return jObj;
	}
}
