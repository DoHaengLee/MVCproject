package com.testcomp.mvcpjt;

import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testcomp.mvcpjt.util.JwtUtil;



@RestController
public class JwtController {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtController.class);
	private static JwtUtil jUtil = new JwtUtil();

	
	// �α������� JWT ȹ�� API
	@RequestMapping(value = "/tokenpw", method = RequestMethod.POST)
	public JSONObject tokenpw(HttpServletRequest request) throws Exception {
		logger.info("tokenpw BEGEIN");
		JSONObject jObj =  new JSONObject(jUtil.tokenpw(request));
		logger.info("tokenpw Res : "+jObj);
		return jObj;
	}
	
	// refresh token���� JWT ��ȹ�� API
	@RequestMapping(value = "/tokenref", method = RequestMethod.POST)
	public JSONObject tokenref(HttpServletRequest request) throws Exception {
		logger.info("tokenref BEGEIN");
		JSONObject jObj =  new JSONObject(jUtil.tokenref(request));
		logger.info("tokenref Res : "+jObj);
		return jObj;
	}
	
	// �׽�ƮAPI - ����� �׼�����ū �а� ������ �۾� ����
	@RequestMapping(value = "/testapi", method = RequestMethod.POST)
	public JSONObject testapi(HttpServletRequest request) throws Exception {
		logger.info("testapi BEGEIN");
		JSONObject jObj =  new JSONObject(jUtil.testapi(request));
		logger.info("testapi Res : "+jObj);
		return jObj;
	}
}
